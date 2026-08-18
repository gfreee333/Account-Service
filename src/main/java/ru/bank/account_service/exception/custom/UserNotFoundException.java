package ru.bank.account_service.exception.custom;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
