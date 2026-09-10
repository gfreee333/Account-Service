package ru.bank.account_service.infrastructure.kafka.deposit;

import lombok.Builder;
import lombok.Data;
import ru.bank.account_service.model.enums.deposit.DepositEventType;
import ru.bank.account_service.model.enums.deposit.DepositStatus;

import java.util.UUID;

@Data
@Builder
public class DepositOutboxEvent {
    private DepositEventType eventType;
    private Long depositId;
    private UUID eventId;
    private DepositStatus status;

    public static DepositOutboxEventBuilder baseBuilder(DepositEventType eventType, DepositStatus status, Long depositId){
        return DepositOutboxEvent.builder()
                .eventType(eventType)
                .depositId(depositId)
                .eventId(UUID.randomUUID())
                .status(status);
    }

    public static DepositOutboxEvent completedEventGenerated(DepositEventType eventType, Long depositId){
        return baseBuilder(eventType, DepositStatus.COMPLETED, depositId).build();
    }

    public static DepositOutboxEvent failedEventGenerated(DepositEventType eventType, Long depositId){
        return baseBuilder(eventType, DepositStatus.FAILED, depositId).build();
    }
}
