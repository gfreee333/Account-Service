package ru.bank.account_service.infrastructure.kafka.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bank.account_service.model.enums.transfer.TransferEventType;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferEvent {
    private TransferEventType eventType;
    private UUID eventId;
    private Long transferId;
    private UUID userId;
    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
}
