package ru.bank.account_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bank.account_service.exception.custom.AccountNotFoundException;
import ru.bank.account_service.exception.custom.BlockedAccountForbiddenException;
import ru.bank.account_service.exception.custom.ClosedAccountForbiddenException;
import ru.bank.account_service.exception.custom.UnblockedAccountForbiddenException;
import ru.bank.account_service.infrastructure.mapper.AccountMapper;
import ru.bank.account_service.model.dto.response.AccountInformation;
import ru.bank.account_service.model.entity.Account;
import ru.bank.account_service.model.enums.AccountStatus;
import ru.bank.account_service.model.enums.AccountType;
import ru.bank.account_service.model.enums.Role;
import ru.bank.account_service.repository.AccountRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountManagementService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    // todo 1: Получение информации о своих счетах
    public List<AccountInformation> getMyAccountsInfo(UUID userId){
        List<Account> accounts = accountRepository.findAllByUserId(userId);
        return accounts.stream()
                .map(accountMapper::toAccountInformation)
                .toList();
    }

    // todo 2: Получение информации о своих конкретных счетах с типом открытого счета
    public List<AccountInformation> getMyCertainAccountsInfo(UUID userId, AccountType accountType){
        List<Account> accounts = accountRepository.findAllByUserIdAndAccountType(userId, accountType);
        return accounts.stream()
                .map(accountMapper::toAccountInformation)
                .toList();
    }

    // todo 3: Получение информации о своем конкретном счете
    public AccountInformation getCurrentAccountInfo(String accountNumber){
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Данный счет не найден в системе"));
        return accountMapper.toAccountInformation(account);
    }

    // todo 4: Логика блокировки счета в системе (Доступно лишь MANAGER | ADMIN)
    @Transactional
    public void blockedAccountInSystem(String accountNumber, Role role){
        if(role.isAdmin() || role.isManager()) {
            Account account = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new AccountNotFoundException("Данный счет не найден в системе"));
            account.setAccountStatus(AccountStatus.BLOCKED);
            accountRepository.save(account);
        } else {
            log.warn("У пользователя с ролью: {} недостаточно прав для блокировки счета", role);
            throw new BlockedAccountForbiddenException("У пользователя недостаточно прав, для блокировки счета в системе");
        }
    }
    // todo 5: Логика разблокировки счета в системе (Доступно лишь MANAGER | ADMIN)
    @Transactional
    public void unblockedAccountInSystem(String accountNumber, Role role){
        if(role.isAdmin() || role.isManager()) {
            Account account = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new AccountNotFoundException("Данный счет не найден в системе"));
            account.setAccountStatus(AccountStatus.ACTIVE);
            accountRepository.save(account);
        } else {
            log.warn("У пользователя с ролью: {} недостаточно прав для разблокировки счета", role);
            throw new UnblockedAccountForbiddenException("У пользователя недостаточно прав, для снятия блокировки в системе");
        }
    }

    // todo 6: Логика закрытия счета в системе
    public void closedAccountInSystem(String accountNumber, Role role){
        if(role.isAdmin() || role.isManager()) {
            Account account = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new AccountNotFoundException("Данный счет не найден в системе"));
            accountRepository.delete(account);
        } else {
            log.warn("У пользователя с ролью: {} недостаточно прав для закрытия счета", role);
            throw new ClosedAccountForbiddenException("У пользователя недостаточно прав, чтобы закрыть счет");
        }
    }

    // todo *: Обновление баланса для всех счетов
    // todo *: Обновление баланса для конкретного счета


}
