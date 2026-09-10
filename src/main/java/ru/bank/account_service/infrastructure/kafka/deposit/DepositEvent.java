package ru.bank.account_service.infrastructure.kafka.deposit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bank.account_service.model.enums.deposit.DepositEventType;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositEvent {
    private DepositEventType eventType;
    private UUID eventId;
    private Long depositId;
    private String toAccountNumber;
    private BigDecimal amount;

}
