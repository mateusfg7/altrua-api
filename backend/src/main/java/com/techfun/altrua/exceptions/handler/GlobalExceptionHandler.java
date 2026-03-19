package com.techfun.altrua.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.techfun.altrua.dto.common.ErrorResponseDTO;
import com.techfun.altrua.exceptions.EmailAlreadyInUseException;
import com.techfun.altrua.exceptions.InvalidCredentialsException;

import jakarta.servlet.http.HttpServletRequest;

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
     * @param ex      a exceção capturada
     * @param request a requisição HTTP onde a exceção ocorreu
     * @return um objeto {@link ErrorResponseDTO} contendo os detalhes do erro
     * @see HttpStatus#CONFLICT
     */
    @ExceptionHandler(EmailAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDTO handleEmailAlreadyInUse(EmailAlreadyInUseException ex, HttpServletRequest request) {
        return ErrorResponseDTO.of(ex.getMessage(), HttpStatus.CONFLICT, request.getRequestURI());
    }

    /**
     * Trata a exceção {@link BadCredentialsException} lançada pelo Spring Security.
     *
     * @param ex      a exceção capturada
     * @param request a requisição HTTP onde a exceção ocorreu
     * @return um objeto {@link ErrorResponseDTO} com status 401 (Unauthorized) e
     *         mensagem genérica
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDTO handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return ErrorResponseDTO.of("Credenciais inválidas", HttpStatus.UNAUTHORIZED, request.getRequestURI());
    }

    /**
     * Trata a exceção personalizada {@link InvalidCredentialsException}.
     *
     * @param ex      a exceção capturada
     * @param request a requisição HTTP onde a exceção ocorreu
     * @return um objeto {@link ErrorResponseDTO} com status 401 (Unauthorized)
     * @see HttpStatus#UNAUTHORIZED
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDTO handleInvalidCredentials(InvalidCredentialsException ex, HttpServletRequest request) {
        return ErrorResponseDTO.of("Credenciais inválidas", HttpStatus.UNAUTHORIZED, request.getRequestURI());
    }
}
