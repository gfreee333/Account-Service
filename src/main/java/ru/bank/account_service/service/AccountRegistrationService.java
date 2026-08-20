package ru.bank.account_service.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bank.account_service.exception.custom.RegistrationForbiddenException;
import ru.bank.account_service.exception.custom.ServiceUnavailableException;
import ru.bank.account_service.exception.custom.UserBlockedInSystemException;
import ru.bank.account_service.exception.custom.UserNotFoundException;
import ru.bank.account_service.infrastructure.feign.AuthServiceFeignClient;
import ru.bank.account_service.infrastructure.feign.UserInformation;
import ru.bank.account_service.infrastructure.kafka.OutboxEvent;
import ru.bank.account_service.infrastructure.mapper.AccountMapper;
import ru.bank.account_service.model.dto.request.RegistrationRequestDto;
import ru.bank.account_service.model.dto.response.RegistrationResponseDto;
import ru.bank.account_service.model.entity.Account;
import ru.bank.account_service.model.enums.OutboxEventType;
import ru.bank.account_service.model.enums.Role;
import ru.bank.account_service.repository.AccountRepository;
import ru.bank.account_service.infrastructure.util.AccountNumberGenerated;
import ru.bank.outbox_library.store.OutboxEventStore;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountRegistrationService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final OutboxEventStore eventStore;
    private final AuthServiceFeignClient authServiceClient;

    @Transactional
    public RegistrationResponseDto accountRegistration(RegistrationRequestDto request,
                                                       UUID targetId,
                                                       Role role){
        if(!role.isManager() && !role.isAdmin()) {
            log.warn("Регистрация счета доступа только MANAGER | ADMIN у пользователя role: {}", role);
            throw new RegistrationForbiddenException("Недостаточно прав для регистрации счета в системе");
        }
        try {
            UserInformation userInformation = authServiceClient.getUserById(targetId);
            if(userInformation.getUserStatus().isBlocked()){
                throw new UserBlockedInSystemException("Запрет на регистрацию счета, для заблокированного пользователя");
            }
            Account account = accountMapper.registrationToEntity(request);
            account.setAccountNumber(AccountNumberGenerated.generatedAccountNumber());
            account.setUserId(targetId);
            accountRepository.save(account);
            OutboxEvent event = OutboxEvent.eventGenerated(
                    OutboxEventType.ACCOUNT_REGISTRATION_EVENT
                    ,userInformation);
            eventStore.save(event, targetId);
            return new RegistrationResponseDto("Успешная регистрация счета для пользователя: " + targetId);
        } catch (FeignException.NotFound ex){
            log.warn("Пользователь с данным id: {} не найден", targetId);
            throw new UserNotFoundException("Пользователь с id: " + targetId + " не найден", ex);
        } catch (FeignException ex){
            log.warn("Ошибка вызова Auth-Service: {}", ex.getMessage());
            throw new ServiceUnavailableException("Ошибка на стороне Auth-Service");
        }
    }

}
