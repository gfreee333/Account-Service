package ru.bank.account_service.infrastructure.feign;

import lombok.Data;
import ru.bank.account_service.model.enums.UserStatus;

import java.util.UUID;

@Data
public class UserInformation {
    private UUID userId;
    private String firstName;
    private String lastName;
    private UserStatus userStatus;
    private String email;
}
