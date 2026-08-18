package ru.bank.account_service.exception.response;

public record ErrorResponse(
        String message,
        Integer status
){}
