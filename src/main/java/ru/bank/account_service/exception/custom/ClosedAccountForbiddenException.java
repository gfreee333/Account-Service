package ru.bank.account_service.exception.custom;

public class ClosedAccountForbiddenException extends RuntimeException{
    public ClosedAccountForbiddenException(String message){
        super(message);
    }
}
