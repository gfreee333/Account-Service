package ru.bank.account_service.model.dto.response;

import ru.bank.account_service.model.enums.AccountStatus;
import ru.bank.account_service.model.enums.AccountType;

import java.math.BigDecimal;

public record AccountInformation(
    String accountNumber,
    AccountType accountType,
    AccountStatus accountStatus,
    BigDecimal balance,
    BigDecimal creditLimit
){}
