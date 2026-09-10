package ru.bank.account_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bank.account_service.exception.custom.AccountNotFoundException;
import ru.bank.account_service.exception.custom.AlienAccountForbiddenException;
import ru.bank.account_service.exception.custom.InsufficientBalanceException;
import ru.bank.account_service.infrastructure.kafka.transfer.TransferEvent;
import ru.bank.account_service.model.entity.Account;
import ru.bank.account_service.model.entity.TransferProcessed;
import ru.bank.account_service.repository.AccountRepository;
import ru.bank.account_service.repository.IdempotentTransferRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferProcessingService {

    private final AccountRepository accountRepository;
    private final IdempotentTransferRepository idempotentTransferRepository;
    private final ObjectMapper objectMapper;

    // Логика перевода финансов между счетами

    @Transactional
    public void processTransfer(TransferEvent event) throws JsonProcessingException {
        // 1) Проверка, было ли сообщение уже обработано в системе
        if (idempotentTransferRepository.existsByEventId(event.getEventId())) {
            log.debug("Событие уже обработано, либо находиться в процессе обработки");
            return;
        }
        // 2) Получаем счет с которого будет перевод + проверка на его наличие с блокировкой
        Account fromAccount = accountRepository.findByAccountNumberForUpdate(event.getFromAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Счет отправителя не найден в системе"));
        // 3) Получаем счет на который будет перевод + проверка на его наличие с блокировкой
        Account toAccount = accountRepository.findByAccountNumberForUpdate(event.getToAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Счет получателя не найден в системе"));
        // 4) Проверка, на всякий случай, что счет действительно принадлежит пользователю
        if (!fromAccount.getUserId().equals(event.getUserId())) {
            log.warn("Попытка перевода с чужого счета: {}", event.getFromAccountNumber());
            throw new AlienAccountForbiddenException("Попытка совершить перевод с другого счета");
        }
        // 5) Проверка, достаточно ли баланса для данного перевода
        if (fromAccount.getBalance().compareTo(event.getAmount()) < 0) {
            log.warn("Недостаточно средств для перевода со счета: {}", fromAccount.getAccountNumber());
            throw new InsufficientBalanceException("Недостаточно средств");
        }
        // 6) Производим перевод между счетами
        fromAccount.setBalance(fromAccount.getBalance().subtract(event.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(event.getAmount()));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        // 7) Сохраняем факт обработки события в БД, чтобы повторно не обрабатывать
        idempotentTransferRepository.save(TransferProcessed.builder()
                .eventId(event.getEventId())
                .processAt(LocalDateTime.now())
                .payload(objectMapper.writeValueAsString(event))
                .build());
    }

}

