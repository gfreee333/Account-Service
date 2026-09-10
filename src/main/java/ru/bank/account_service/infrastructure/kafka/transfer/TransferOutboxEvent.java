package ru.bank.account_service.infrastructure.kafka.transfer;

import lombok.Builder;
import lombok.Data;
import ru.bank.account_service.model.enums.transfer.TransactStatus;
import ru.bank.account_service.model.enums.transfer.TransferEventType;

import java.util.UUID;

@Data
@Builder
public class TransferOutboxEvent {

    private TransferEventType eventType;
    private Long transferId;
    private UUID eventId;
    private TransactStatus status;

    public static TransferOutboxEventBuilder baseBuilder(TransferEventType eventType, TransactStatus status, Long transferId) {
        return TransferOutboxEvent.builder()
                .eventType(eventType)
                .transferId(transferId)
                .eventId(UUID.randomUUID())
                .status(status);
    }

    public static TransferOutboxEvent completedEventGenerated(TransferEventType eventType, Long transferId) {
        return baseBuilder(eventType, TransactStatus.COMPLETED, transferId).build();
    }

    public static TransferOutboxEvent failedEventGenerated(TransferEventType eventType, Long transferId) {
        return baseBuilder(eventType, TransactStatus.FAILED, transferId).build();
    }


}
