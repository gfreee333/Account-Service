package ru.bank.account_service.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bank.account_service.model.enums.AccountStatus;
import ru.bank.account_service.model.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private UUID userId;
    @NotNull
    @Column(unique = true)
    private UUID accountNumber;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AccountStatus accountStatus = AccountStatus.ACTIVE;
    @NotNull
    private BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal dailyLimit = BigDecimal.valueOf(100_000);
    private BigDecimal creditLimit;
    @NotNull
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
