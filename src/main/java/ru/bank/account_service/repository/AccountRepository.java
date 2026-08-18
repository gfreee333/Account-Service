package ru.bank.account_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.bank.account_service.model.entity.Account;
import ru.bank.account_service.model.enums.AccountType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("from Account a where a.userId = :userId")
    List<Account> findAllByUserId(UUID userId);
    @Query("from Account a where a.userId = :userId and a.accountType = :accountType")
    List<Account> findAllByUserIdAndAccountType(UUID userId, AccountType accountType);
    Optional<Account> findByAccountNumber(String accountNumber);

}
