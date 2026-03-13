package com.techfun.altrua.exceptions;

/**
 * Exceção lançada quando ocorre uma tentativa de registro com um e-mail já existente.
 *
 * <p>
 * Estende {@link RuntimeException}, indicando que é uma exceção não verificada
 * que ocorre durante a execução da lógica de negócios de cadastro.
 * </p>
 */
public class EmailAlreadyInUseException extends RuntimeException {

    /**
     * Constrói a exceção com uma mensagem padrão formatada com o e-mail duplicado.
     *
     * @param email o e-mail que causou o conflito
     */
    public EmailAlreadyInUseException(String email) {
        super("E-mail já cadastrado: " + email);
    }
}
