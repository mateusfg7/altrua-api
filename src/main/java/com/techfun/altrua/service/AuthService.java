package com.techfun.altrua.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techfun.altrua.dto.RegisterRequestDTO;
import com.techfun.altrua.entities.User;
import com.techfun.altrua.exceptions.EmailAlreadyInUseException;
import com.techfun.altrua.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyInUseException(dto.getEmail());
        }

        User user = User.create(dto.getName(), dto.getEmail(), passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
    }
}
