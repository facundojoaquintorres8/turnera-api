package com.f8.turnera.security.repositories;

import java.util.List;
import java.util.Optional;

import com.f8.turnera.security.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByActivationKey(String activationKey);
    Optional<User> findByResetKey(String resetKey);
    List<User> findAllByOrganizationId(Long onganizationId);
}