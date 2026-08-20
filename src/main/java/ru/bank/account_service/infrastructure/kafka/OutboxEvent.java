package ru.bank.account_service.infrastructure.kafka;

import lombok.Builder;
import lombok.Data;
import ru.bank.account_service.infrastructure.feign.UserInformation;
import ru.bank.account_service.model.enums.OutboxEventType;

import java.util.UUID;

@Data
@Builder
public class OutboxEvent {

    private OutboxEventType eventType;
    private UUID eventId;
    private UUID userId;
    private String email;

    public static OutboxEventBuilder baseBuilder(OutboxEventType type, UserInformation information){
        return OutboxEvent.builder()
                .eventType(type)
                .userId(information.getUserId())
                .email(information.getEmail())
                .eventId(UUID.randomUUID());
    }

    public static OutboxEvent eventGenerated(OutboxEventType type, UserInformation information){
        return baseBuilder(type, information).build();
    }

}
