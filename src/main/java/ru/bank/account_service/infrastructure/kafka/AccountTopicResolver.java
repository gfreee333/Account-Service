package ru.bank.account_service.infrastructure.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.bank.account_service.model.enums.deposit.DepositEventType;
import ru.bank.account_service.model.enums.notification.NotificationOutboxEventType;
import ru.bank.account_service.model.enums.transfer.TransferEventType;
import ru.bank.outbox_library.processor.TopicResolver;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountTopicResolver implements TopicResolver {

    private final ObjectMapper objectMapper;

    @Override
    public String resolver(String payload) {
        try {
            JsonNode root = objectMapper.readTree(payload);
            String eventType = root.path("eventType").asText(null);
            if(eventType == null || eventType.isBlank()){
                throw new IllegalArgumentException("eventType отсутствует в payload");
            }
            for (NotificationOutboxEventType type : NotificationOutboxEventType.values()){
                if(type.name().equals(eventType)){
                    return type.getTopic();
                }
            }
            for (TransferEventType type : TransferEventType.values()){
                if(type.name().equals(eventType)){
                    return type.getTopic();
                }
            }
            for (DepositEventType type : DepositEventType.values()){
                if(type.name().equals(eventType)){
                    return type.getTopic();
                }
            }
            throw new IllegalArgumentException("Неизвестный eventType: " + eventType);
        } catch (Exception ex) {
            log.warn("Не удалось определить топик, ошибка: {}", ex.getMessage());
            throw new RuntimeException("Не удалось определить Topic для записи в Kafka", ex);
        }
    }
}
