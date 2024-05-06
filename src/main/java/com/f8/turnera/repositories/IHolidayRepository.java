package com.f8.turnera.repositories;

import com.f8.turnera.entities.Holiday;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHolidayRepository extends JpaRepository<Holiday, Long> {
    Optional<Holiday> findByDateAndOrganizationId(LocalDate date, Long organizationId);
}