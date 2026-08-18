package ru.bank.account_service.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.bank.account_service.exception.custom.RegistrationForbiddenException;
import ru.bank.account_service.exception.custom.ServiceUnavailableException;
import ru.bank.account_service.exception.custom.UserBlockedInSystemException;
import ru.bank.account_service.exception.custom.UserNotFoundException;
import ru.bank.account_service.exception.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegistrationForbiddenException.class)
    public ResponseEntity<ErrorResponse> registrationForbiddenHandler(RegistrationForbiddenException ex){
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.FORBIDDEN.value()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundHandler(UserNotFoundException ex){
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UserBlockedInSystemException.class)
    public ResponseEntity<ErrorResponse> userBlockedInSystemHandler(UserBlockedInSystemException ex){
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.FORBIDDEN.value()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> serviceUnavailableHandler(ServiceUnavailableException ex){
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.SERVICE_UNAVAILABLE.value()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

}
