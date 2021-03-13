package com.f8.turnera.security.repositories;

import java.util.List;

import com.f8.turnera.security.entities.Profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findAllByOrganizationId(Long onganizationId);
}