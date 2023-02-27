package com.f8.turnera.repositories;

import com.f8.turnera.entities.Holiday;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHolidayRepository extends JpaRepository<Holiday, Long> {}