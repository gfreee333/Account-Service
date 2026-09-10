package ru.bank.account_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bank.account_service.model.entity.DepositProcessed;

import java.util.UUID;

@Repository
public interface IdempotentDepositRepository extends JpaRepository<DepositProcessed, Long> {
    boolean existsByEventId(UUID eventId);
}
