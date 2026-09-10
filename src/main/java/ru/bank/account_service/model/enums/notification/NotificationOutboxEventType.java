package ru.bank.account_service.model.enums.notification;

import lombok.Getter;

@Getter
public enum NotificationOutboxEventType {
    ACCOUNT_REGISTRATION_EVENT("account-registration-topic"),
    CLOSE_ACCOUNT_EVENT("account-close-topic"),
    BLOCKED_ACCOUNT_EVENT("account-blocked-topic"),
    UNBLOCKED_ACCOUNT_EVENT("account-unblocked-topic");

    private final String topic;

    NotificationOutboxEventType(String topic){
        this.topic = topic;
    }


}
