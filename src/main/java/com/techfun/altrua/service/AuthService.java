package com.techfun.altrua.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techfun.altrua.dto.RegisterRequestDTO;
import com.techfun.altrua.entities.User;
import com.techfun.altrua.exceptions.EmailAlreadyInUseException;
import com.techfun.altrua.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável pelas regras de negócio relacionadas à autenticação e
 * registro de usuários.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registra um novo usuário no sistema.
     *
     * <p>
     * Verifica se o e-mail já está em uso antes de criar o usuário.
     * A senha fornecida é criptografada antes de ser salva.
     * </p>
     *
     * @param dto objeto contendo os dados do registro (nome, e-mail e senha)
     * @throws EmailAlreadyInUseException se o e-mail informado já estiver cadastrado
     */
    public void register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyInUseException(dto.getEmail());
        }

        User user = User.create(dto.getName(), dto.getEmail(), passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
    }
}
