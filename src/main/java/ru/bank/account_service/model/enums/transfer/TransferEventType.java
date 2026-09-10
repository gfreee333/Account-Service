package ru.bank.account_service.model.enums.transfer;


import lombok.Getter;

@Getter
public enum TransferEventType {
    TRANSFER_EVENT("transfer-topic"),
    TRANSFER_RESULT_EVENT("transfer-result-topic");

    private final String topic;

    TransferEventType(String topic){
        this.topic = topic;
    }

}
