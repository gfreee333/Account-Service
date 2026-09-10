package ru.bank.account_service.model.enums.deposit;

import lombok.Getter;

@Getter
public enum DepositEventType {

    DEPOSIT_EVENT("deposit-topic"),
    DEPOSIT_RESULT_EVENT("deposit-result-topic");

    private final String topic;

    DepositEventType(String topic){
        this.topic = topic;
    }

}
