package ru.bank.account_service.infrastructure.kafka.transfer;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import ru.bank.account_service.exception.custom.AccountNotFoundException;
import ru.bank.account_service.exception.custom.AlienAccountForbiddenException;
import ru.bank.account_service.exception.custom.InsufficientBalanceException;
import ru.bank.account_service.model.enums.transfer.TransferEventType;
import ru.bank.account_service.service.TransferProcessingService;
import ru.bank.outbox_library.store.OutboxEventStore;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransferKafkaListener {

    private final TransferProcessingService transferProcessingService;
    private final OutboxEventStore eventStore;

    // todo 1: Обработка при переводе с одного счета на другой.
    @KafkaListener(
            topics = "transfer-topic",
            containerFactory = "transferEventKafkaListenerFactory"
    )
    public void transferProcessing(TransferEvent event, Acknowledgment ack) {
        try {
            transferProcessingService.processTransfer(event);
            TransferOutboxEvent outboxEvent = TransferOutboxEvent
                    .completedEventGenerated(
                            TransferEventType.TRANSFER_RESULT_EVENT,
                            event.getTransferId()
                    );
            eventStore.save(outboxEvent, event.getUserId());
            ack.acknowledge();
        } catch (AccountNotFoundException | InsufficientBalanceException
                 | AlienAccountForbiddenException | JsonProcessingException ex) {
            TransferOutboxEvent outboxEvent = TransferOutboxEvent
                    .failedEventGenerated(
                            TransferEventType.TRANSFER_RESULT_EVENT,
                            event.getTransferId()
                    );
            eventStore.save(outboxEvent, event.getUserId());
            ack.acknowledge();
        } catch (Exception ex) {
            log.warn("Временная ошибка transfer: {}", event.getEventId(), ex);
            throw ex;
        }
    }
}
