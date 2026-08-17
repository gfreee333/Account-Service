package ru.bank.account_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bank.account_service.model.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
