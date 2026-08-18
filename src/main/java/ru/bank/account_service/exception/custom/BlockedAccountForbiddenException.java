package ru.bank.account_service.exception.custom;

public class BlockedAccountForbiddenException extends RuntimeException{
    public BlockedAccountForbiddenException(String message){
        super(message);
    }
}
