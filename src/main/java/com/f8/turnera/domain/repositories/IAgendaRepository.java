package com.f8.turnera.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.f8.turnera.domain.entities.Agenda;

@Repository
public interface IAgendaRepository extends JpaRepository<Agenda, Long> {
    Optional<Agenda> findByIdAndOrganizationId(Long id, Long orgId);
}