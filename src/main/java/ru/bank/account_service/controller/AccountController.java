package ru.bank.account_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bank.account_service.model.dto.request.RegistrationRequestDto;
import ru.bank.account_service.model.dto.response.AccountInformation;
import ru.bank.account_service.model.dto.response.RegistrationResponseDto;
import ru.bank.account_service.model.enums.AccountType;
import ru.bank.account_service.model.enums.Role;
import ru.bank.account_service.service.AccountManagementService;
import ru.bank.account_service.service.AccountRegistrationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountManagementService managementService;
    private final AccountRegistrationService registrationService;

    // todo 1: Регистрация счета для пользователя
    @PostMapping("/{targetId}")
    public ResponseEntity<RegistrationResponseDto> accountRegistration(
            @RequestBody @Valid RegistrationRequestDto request,
            @PathVariable("targetId") UUID targetId,
            @RequestHeader("X-User-Role") Role role
    ){
        return ResponseEntity.ok().body(registrationService.accountRegistration(request, targetId, role));
    }

    // todo 2: Получение информации о своих счетах
    @GetMapping("/accounts")
    public ResponseEntity<List<AccountInformation>> getMyAccountsInfo(
            @RequestHeader("X-User-Id") UUID userId
    ){
        List<AccountInformation> accountsInfo = managementService.getMyAccountsInfo(userId);
        return ResponseEntity.ok().body(accountsInfo);
    }

    // todo 3: Получение информации о своих счетах с конкретным типом счета
    @GetMapping("/accounts/type")
    public ResponseEntity<List<AccountInformation>> getMyCertainAccountsInfo(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestParam AccountType accountType
    ){
        List<AccountInformation> accountsInfo = managementService.getMyCertainAccountsInfo(userId, accountType);
        return ResponseEntity.ok().body(accountsInfo);
    }

    // todo 4: Получение информации о счете по accountNumber
    @GetMapping("/accountNumber")
    public ResponseEntity<AccountInformation> getCurrentAccountInfo(
            @RequestParam String accountNumber
    ){
        return ResponseEntity.ok().body(managementService.getCurrentAccountInfo(accountNumber));
    }

    // todo 5: Блокировка конкретного счета в системе по номеру счета
    @PatchMapping("/blocked")
    public ResponseEntity<Void> blockedAccount(
            @RequestParam String accountNumber,
            @RequestHeader("X-User-Role") Role role
    ){
        managementService.blockedAccountInSystem(accountNumber, role);
        return ResponseEntity.noContent().build();
    }

    // todo 6: Разблокировать конкретный счет пользователя в системе
    @PatchMapping("/unblocked")
    public ResponseEntity<Void> unblockedAccount(
            @RequestParam String accountNumber,
            @RequestHeader("X-User-Role") Role role
    ){
        managementService.unblockedAccountInSystem(accountNumber, role);
        return ResponseEntity.noContent().build();
    }

    // todo 7: Закрытие счета в системе
    @DeleteMapping("/close")
    public ResponseEntity<Void> closeAccount(
            @RequestParam String accountNumber,
            @RequestHeader("X-User-Role") Role role
    ){
        managementService.closedAccountInSystem(accountNumber, role);
        return ResponseEntity.noContent().build();
    }

}
