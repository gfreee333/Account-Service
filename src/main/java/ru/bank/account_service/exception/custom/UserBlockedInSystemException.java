package ru.bank.account_service.exception.custom;

public class UserBlockedInSystemException extends RuntimeException{
    public UserBlockedInSystemException(String message){
        super(message);
    }
}
