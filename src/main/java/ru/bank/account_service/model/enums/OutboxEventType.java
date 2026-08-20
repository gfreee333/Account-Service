package ru.bank.account_service.model.enums;

import lombok.Getter;

@Getter
public enum OutboxEventType {
    ACCOUNT_REGISTRATION_EVENT("account-registration-topic"),
    CLOSE_ACCOUNT_EVENT("account-close-topic"),
    BLOCKED_ACCOUNT_EVENT("account-blocked-topic"),
    UNBLOCKED_ACCOUNT_EVENT("account-unblocked-topic");

    private final String topic;

    OutboxEventType(String topic){
        this.topic = topic;
    }


}
