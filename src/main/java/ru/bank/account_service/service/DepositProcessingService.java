package ru.bank.account_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bank.account_service.exception.custom.AccountNotFoundException;
import ru.bank.account_service.infrastructure.kafka.deposit.DepositEvent;
import ru.bank.account_service.model.entity.Account;
import ru.bank.account_service.model.entity.DepositProcessed;
import ru.bank.account_service.repository.AccountRepository;
import ru.bank.account_service.repository.IdempotentDepositRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepositProcessingService {

    private final AccountRepository accountRepository;
    private final IdempotentDepositRepository idempotentDepositRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void processDeposit(DepositEvent event) throws JsonProcessingException {
        if(idempotentDepositRepository.existsByEventId(event.getEventId())){
            log.debug("Событие уже обработано, либо находиться в процессе обработки");
            return;
        }
        Account account = accountRepository.findByAccountNumberForUpdate(event.getToAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Счет пользователя, не найден"));
        account.setBalance(account.getBalance().add(event.getAmount()));
        accountRepository.save(account);
        idempotentDepositRepository.save(DepositProcessed.builder()
                .eventId(event.getEventId())
                .processAt(LocalDateTime.now())
                .payload(objectMapper.writeValueAsString(event))
                .build());
    }

}
