package com.techfun.altrua.service;

import com.techfun.altrua.entities.User;

/**
 * Resultado da operação de rotação de refresh token.
 *
 * <p>
 * Encapsula o novo refresh token gerado e o usuário vinculado,
 * permitindo que o {@link com.techfun.altrua.service.AuthService}
 * gere um novo access token sem consultas adicionais ao banco.
 * </p>
 *
 * @param newToken novo refresh token gerado em formato JWT original
 * @param user     usuário vinculado ao token rotacionado
 */
public record RotateResult(String newToken, User user) {
}
