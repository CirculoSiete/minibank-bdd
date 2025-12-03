package com.circulosiete.minibank.account.api;

import com.circulosiete.minibank.account.domain.InsufficientBalanceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandling {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> runtime(RuntimeException ex) {
        var error = ApiError.of(
            "https://errors.acme.com/unexpected-error",
            "Error",
            ex.getMessage()
        );
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handle(IllegalArgumentException ex) {
        var error = ApiError.of(
            "https://errors.acme.com/illegal-argument",
            "Error",
            ex.getMessage()
        );
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiError> insufficientBalance(InsufficientBalanceException ex) {
        var error = ApiError.of(
            "https://errors.acme.com/insufficient-balance",
            "Error",
            ex.getMessage()
        );
        return ResponseEntity
            .status(HttpStatus.UNPROCESSABLE_CONTENT)
            .body(error);
    }
}
