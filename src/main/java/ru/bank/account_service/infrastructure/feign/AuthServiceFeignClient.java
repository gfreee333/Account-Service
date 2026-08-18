package ru.bank.account_service.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "auth-service")
public interface AuthServiceFeignClient {
    @GetMapping("/internal/users/{targetId}")
    UserInformation getUserById(@PathVariable("targetId") UUID targetId);
}
