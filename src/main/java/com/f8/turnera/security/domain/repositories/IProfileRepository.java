package com.f8.turnera.security.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.f8.turnera.security.domain.entities.Profile;

@Repository
public interface IProfileRepository extends JpaRepository<Profile, Long> {}