package com.f8.turnera.security.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.f8.turnera.security.domain.entities.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByActivationKey(String activationKey);
    Optional<User> findByResetKey(String resetKey);
}