package ru.bank.account_service.exception.custom;

public class RegistrationForbiddenException extends RuntimeException{
    public RegistrationForbiddenException(String message){
        super(message);
    }
}
