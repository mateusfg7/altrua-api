package com.techfun.altrua.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techfun.altrua.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    public boolean existsByEmail(String email);
}
