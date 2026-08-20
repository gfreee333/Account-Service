package ru.bank.account_service.infrastructure.kafka;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.bank.account_service.exception.custom.ServiceUnavailableException;
import ru.bank.account_service.exception.custom.UserNotFoundException;
import ru.bank.account_service.infrastructure.feign.AuthServiceFeignClient;
import ru.bank.account_service.infrastructure.feign.UserInformation;
import ru.bank.account_service.model.enums.OutboxEventType;
import ru.bank.outbox_library.store.OutboxEventStore;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxEventHelper {

    private final AuthServiceFeignClient authServiceClient;
    private final OutboxEventStore eventStore;

    public void saveOutboxEvent(UUID targetId, OutboxEventType type){
        try {
            UserInformation information = authServiceClient.getUserById(targetId);
            OutboxEvent event = OutboxEvent.eventGenerated(type, information);
            eventStore.save(event, targetId);
        } catch (FeignException.NotFound ex) {
            log.warn("Пользователь с данным id: {} не найден", targetId);
            throw new UserNotFoundException("Пользователь с id: " + targetId + " не найден", ex);
        } catch (FeignException ex) {
            log.warn("Ошибка вызова Auth-Service: {}", ex.getMessage());
            throw new ServiceUnavailableException("Ошибка на стороне Auth-Service");
        }
    }
}
