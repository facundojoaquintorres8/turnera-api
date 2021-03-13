package com.f8.turnera.repositories;

import com.f8.turnera.entities.Agenda;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAgendaRepository extends JpaRepository<Agenda, Long> {}