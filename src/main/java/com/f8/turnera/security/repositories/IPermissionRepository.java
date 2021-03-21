package com.f8.turnera.security.repositories;

import java.util.Optional;

import com.f8.turnera.security.entities.Permission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByCode(String code);
}