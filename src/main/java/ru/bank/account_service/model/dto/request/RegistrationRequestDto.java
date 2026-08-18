package ru.bank.account_service.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import ru.bank.account_service.model.enums.AccountType;

import java.math.BigDecimal;

@Data
public class RegistrationRequestDto {
    @NotBlank
    private AccountType accountType;
    @PositiveOrZero
    private BigDecimal dailyLimit;
    @PositiveOrZero
    private BigDecimal creditLimit;
}
