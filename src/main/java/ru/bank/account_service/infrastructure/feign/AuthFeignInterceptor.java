package ru.bank.account_service.infrastructure.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Slf4j
public class AuthFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes == null){
            log.warn("Нет HTTP-контекста, токен в Feign не прокидывается");
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && !authHeader.isBlank()){
            template.header("Authorization", authHeader);
            log.debug("Feign: прокинут Authorization header");
        } else {
            String accessToken = extractToken(request, "access_token");
            if(accessToken != null && !accessToken.isBlank()){
                template.header("Authorization", "Bearer " + accessToken);
                log.debug("Feign: токен взят из cookies access_token");
            } else {
                log.warn("Feign: access токен не найден");
            }
        }
        String clientType = request.getHeader("X-Client-Type");
        if(clientType != null && !clientType.isBlank()){
            template.header("X-Client-Type", clientType);
        } else {
            template.header("X-Client-Type", "INTERNAL");
        }
    }

    private String extractToken(HttpServletRequest request, String name){
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return null;
        }
        for (Cookie cookie : cookies){
            if(name.equals(cookie.getName())){
                return cookie.getValue();
            }
        }
        return null;
    }

}
