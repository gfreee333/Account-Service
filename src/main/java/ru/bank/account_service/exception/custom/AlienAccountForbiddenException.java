package ru.bank.account_service.exception.custom;

public class AlienAccountForbiddenException extends RuntimeException{
    public AlienAccountForbiddenException(String message){
        super(message);
    }
}
