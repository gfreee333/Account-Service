package ru.bank.account_service.infrastructure.kafka.deposit;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import ru.bank.account_service.exception.custom.AccountNotFoundException;
import ru.bank.account_service.model.enums.deposit.DepositEventType;
import ru.bank.account_service.service.AccountManagementService;
import ru.bank.account_service.service.DepositProcessingService;
import ru.bank.outbox_library.store.OutboxEventStore;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DepositKafkaListener {

    private final DepositProcessingService depositProcessingService;
    private final AccountManagementService managementService;
    private final OutboxEventStore eventStore;

    @KafkaListener(
            topics = "deposit-topic",
            containerFactory = "depositEventKafkaListenerFactory"
    )
    public void depositProcessing(DepositEvent event, Acknowledgment ack){
        UUID userId = managementService.getUserIdFromAccount(event.getToAccountNumber());
        try {
            depositProcessingService.processDeposit(event);
            DepositOutboxEvent outboxEvent = DepositOutboxEvent
                    .completedEventGenerated(
                            DepositEventType.DEPOSIT_RESULT_EVENT,
                            event.getDepositId()
                    );
            eventStore.save(outboxEvent, userId);
            ack.acknowledge();
        } catch (AccountNotFoundException | JsonProcessingException ex){
            DepositOutboxEvent outboxEvent = DepositOutboxEvent
                    .failedEventGenerated(
                            DepositEventType.DEPOSIT_RESULT_EVENT,
                            event.getDepositId()
                    );
            eventStore.save(outboxEvent, userId);
            ack.acknowledge();
        } catch (Exception ex){
            log.warn("Временная ошибка deposit: {}", event.getEventId(), ex);
            throw ex;
        }
    }
}
