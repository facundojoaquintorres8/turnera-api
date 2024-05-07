package com.f8.turnera.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.f8.turnera.domain.entities.ResourceType;

@Repository
public interface IResourceTypeRepository extends JpaRepository<ResourceType, Long> {}