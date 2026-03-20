package com.techfun.altrua.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techfun.altrua.entities.RefreshToken;

/**
 * Repositório para operações de persistência da entidade {@link RefreshToken}.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    /**
     * Busca um refresh token pelo seu valor.
     *
     * @param token o valor do refresh token a ser localizado
     * @return um {@link Optional} contendo o token encontrado, ou vazio se não
     *         existir
     */
    public Optional<RefreshToken> findByToken(String token);

}