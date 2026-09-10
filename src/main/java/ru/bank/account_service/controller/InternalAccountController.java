package ru.bank.account_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bank.account_service.service.AccountManagementService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/internal/accounts")
@RequiredArgsConstructor
public class InternalAccountController {

    private final AccountManagementService managementService;

    @GetMapping("/{accountNumber}/exists")
    public ResponseEntity<Boolean> existsAccount(@PathVariable("accountNumber") String accountNumber){
        return ResponseEntity.ok().body(managementService.existsAccount(accountNumber));
    }

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable("accountNumber") String accountNumber){
        return ResponseEntity.ok().body(managementService.getBalance(accountNumber));
    }

    @GetMapping("/{accountNumber}/userId")
    public ResponseEntity<UUID> getUserIdFromAccount(@PathVariable("accountNumber") String accountNumber){
        return ResponseEntity.ok().body(managementService.getUserIdFromAccount(accountNumber));
    }

}
