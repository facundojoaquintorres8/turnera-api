package com.f8.turnera.security.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.f8.turnera.security.domain.entities.Permission;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByCode(String code);
}