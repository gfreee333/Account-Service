package ru.bank.account_service.model.enums.auth;

public enum UserStatus {
    PENDING, ACTIVE, BLOCKED;

    public boolean isBlocked(){
        return this.equals(BLOCKED);
    }

}
