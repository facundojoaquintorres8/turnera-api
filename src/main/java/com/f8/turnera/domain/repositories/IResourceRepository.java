package com.f8.turnera.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.f8.turnera.domain.entities.Resource;

@Repository
public interface IResourceRepository extends JpaRepository<Resource, Long> {}