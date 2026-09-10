package ru.bank.account_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bank.account_service.exception.custom.*;
import ru.bank.account_service.infrastructure.kafka.notification.NotificationOutboxEventHelper;
import ru.bank.account_service.infrastructure.mapper.AccountMapper;
import ru.bank.account_service.model.dto.response.AccountInformation;
import ru.bank.account_service.model.entity.Account;
import ru.bank.account_service.model.enums.AccountStatus;
import ru.bank.account_service.model.enums.AccountType;
import ru.bank.account_service.model.enums.notification.NotificationOutboxEventType;
import ru.bank.account_service.model.enums.auth.Role;
import ru.bank.account_service.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountManagementService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final NotificationOutboxEventHelper eventHelper;

    // todo: Проверка, существует ли указанный счет в системе
    public boolean existsAccount(String accountNumber){
        return accountRepository.existsByAccountNumber(accountNumber);
    }

    // todo: Получение баланса пользователя
    public BigDecimal getBalance(String accountNumber){
        return accountRepository.findBalanceByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Не удалось найти нужный счет для извлечения баланса"));
    }

    // todo: Получение информации о конкретном account через accountNumber, без проверки на принадлежность
    public UUID getUserIdFromAccount(String accountNumber){
        return accountRepository.findUserIdByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Не удалось найти нужный счет для извлечения userId"));
    }


    // todo 1: Получение информации о своих счетах
    public List<AccountInformation> getMyAccountsInfo(UUID userId) {
        List<Account> accounts = accountRepository.findAllByUserId(userId);
        return accounts.stream()
                .map(accountMapper::toAccountInformation)
                .toList();
    }

    // todo 2: Получение информации о своих конкретных счетах с типом открытого счета
    public List<AccountInformation> getMyCertainAccountsInfo(UUID userId, AccountType accountType) {
        List<Account> accounts = accountRepository.findAllByUserIdAndAccountType(userId, accountType);
        return accounts.stream()
                .map(accountMapper::toAccountInformation)
                .toList();
    }

    // todo 3: Получение информации о своем конкретном счете
    public AccountInformation getCurrentAccountInfo(String accountNumber, UUID userId) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Данный счет не найден в системе"));
        if(!account.getUserId().equals(userId)){
            log.warn("Попытка получить информацию о чужом счете");
            throw new AlienAccountForbiddenException("Невозможно получить информацию о чужом счете"); //
        }
        return accountMapper.toAccountInformation(account);
    }

    // todo 4: Логика блокировки счета в системе (Доступно лишь MANAGER | ADMIN)
    @Transactional
    public void blockedAccountInSystem(String accountNumber, Role role) {
        if (role.isAdmin() || role.isManager()) {
            Account account = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new AccountNotFoundException("Данный счет не найден в системе"));
            account.setAccountStatus(AccountStatus.BLOCKED);
            accountRepository.save(account);
            eventHelper.saveOutboxEvent(
                    account.getUserId(),
                    NotificationOutboxEventType.BLOCKED_ACCOUNT_EVENT,
                    accountNumber);
        } else {
            log.warn("У пользователя с ролью: {} недостаточно прав для блокировки счета", role);
            throw new BlockedAccountForbiddenException("У пользователя недостаточно прав, для блокировки счета в системе");
        }
    }

    // todo 5: Логика разблокировки счета в системе (Доступно лишь MANAGER | ADMIN)
    @Transactional
    public void unblockedAccountInSystem(String accountNumber, Role role) {
        if (role.isAdmin() || role.isManager()) {
            Account account = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new AccountNotFoundException("Данный счет не найден в системе"));
            account.setAccountStatus(AccountStatus.ACTIVE);
            accountRepository.save(account);
            eventHelper.saveOutboxEvent(
                    account.getUserId(),
                    NotificationOutboxEventType.UNBLOCKED_ACCOUNT_EVENT,
                    accountNumber);
        } else {
            log.warn("У пользователя с ролью: {} недостаточно прав для разблокировки счета", role);
            throw new UnblockedAccountForbiddenException("У пользователя недостаточно прав, для снятия блокировки в системе");
        }
    }

    // todo 6: Логика закрытия счета в системе
    @Transactional
    public void closedAccountInSystem(String accountNumber, Role role) {
        if (role.isAdmin() || role.isManager()) {
            Account account = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new AccountNotFoundException("Данный счет не найден в системе"));
            accountRepository.delete(account);
            eventHelper.saveOutboxEvent(
                    account.getUserId(),
                    NotificationOutboxEventType.CLOSE_ACCOUNT_EVENT,
                    accountNumber);
        } else {
            log.warn("У пользователя с ролью: {} недостаточно прав для закрытия счета", role);
            throw new ClosedAccountForbiddenException("У пользователя недостаточно прав, чтобы закрыть счет");
        }
    }

    // todo *: Обновление баланса для всех счетов
    // todo *: Обновление баланса для конкретного счета


}
