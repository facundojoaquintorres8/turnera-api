package com.f8.turnera.domain.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.f8.turnera.domain.entities.Holiday;

@Repository
public interface IHolidayRepository extends JpaRepository<Holiday, Long> {
    Optional<Holiday> findByIdAndOrganizationId(Long id, Long orgId);
    Optional<Holiday> findByDateAndOrganizationId(LocalDate date, Long organizationId);
    List<Holiday> findByOrganizationIdAndActiveTrueAndUseInAgendaTrueAndDateBetween(Long organizationId, LocalDate start, LocalDate end);
}