package com.techfun.altrua.exceptions.handler;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.techfun.altrua.exceptions.EmailAlreadyInUseException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EmailAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleEmailAlreadyInUser(EmailAlreadyInUseException ex) {
        return Map.of("error", ex.getMessage());
    }
}
