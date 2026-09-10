package ru.bank.account_service.infrastructure.feign;

import lombok.Data;
import ru.bank.account_service.model.enums.auth.UserStatus;

import java.util.UUID;

@Data
public class UserInformation {
    private UUID userId;
    private UserStatus status;
    private String email;
}
