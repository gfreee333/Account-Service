package ru.bank.account_service.exception.custom;

public class UnblockedAccountForbiddenException extends RuntimeException{
    public UnblockedAccountForbiddenException(String message){
        super(message);
    }
}
