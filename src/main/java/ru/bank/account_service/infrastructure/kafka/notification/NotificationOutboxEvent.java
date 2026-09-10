package ru.bank.account_service.infrastructure.kafka.notification;

import lombok.Builder;
import lombok.Data;
import ru.bank.account_service.infrastructure.feign.UserInformation;
import ru.bank.account_service.model.enums.notification.NotificationOutboxEventType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NotificationOutboxEvent {

    private NotificationOutboxEventType eventType;
    private UUID eventId;
    private UUID userId;
    private String accountNumber;
    private String email;
    private LocalDateTime timestamp;

    public static NotificationOutboxEventBuilder baseBuilder(NotificationOutboxEventType type, UserInformation information, String accountNumber){
        return NotificationOutboxEvent.builder()
                .eventType(type)
                .userId(information.getUserId())
                .email(information.getEmail())
                .timestamp(LocalDateTime.now())
                .accountNumber(accountNumber)
                .eventId(UUID.randomUUID());
    }

    public static NotificationOutboxEvent eventGenerated(NotificationOutboxEventType type, UserInformation information, String accountNumber){
        return baseBuilder(type, information, accountNumber).build();
    }

}
