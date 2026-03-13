package com.techfun.altrua.exceptions.handler;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.techfun.altrua.exceptions.EmailAlreadyInUseException;

/**
 * Manipulador global de exceções para a API.
 * 
 * <p>
 * Intercepta exceções lançadas pelos controllers e serviços, retornando
 * respostas HTTP padronizadas com mensagens de erro adequadas.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Trata a exceção {@link EmailAlreadyInUseException}.
     *
     * @param ex a exceção capturada
     * @return um mapa contendo a mensagem de erro
     * @see HttpStatus#CONFLICT
     */
    @ExceptionHandler(EmailAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleEmailAlreadyInUser(EmailAlreadyInUseException ex) {
        return Map.of("error", ex.getMessage());
    }
}
