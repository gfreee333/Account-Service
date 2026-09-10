package ru.bank.account_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bank.account_service.model.entity.TransferProcessed;

import java.util.UUID;

@Repository
public interface IdempotentTransferRepository extends JpaRepository<TransferProcessed, Long>{
    boolean existsByEventId(UUID eventId);
}
