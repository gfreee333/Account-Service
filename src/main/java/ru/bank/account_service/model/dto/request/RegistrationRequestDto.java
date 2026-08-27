package ru.bank.account_service.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import ru.bank.account_service.model.enums.AccountType;

import java.math.BigDecimal;

@Data
public class RegistrationRequestDto {

    @NotNull
    private AccountType accountType;
    @NotNull
    @PositiveOrZero
    private BigDecimal dailyLimit;
    @NotNull
    @PositiveOrZero
    private BigDecimal creditLimit;
}
