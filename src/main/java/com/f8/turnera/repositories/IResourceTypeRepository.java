package com.f8.turnera.repositories;

import java.util.List;

import com.f8.turnera.entities.ResourceType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IResourceTypeRepository extends JpaRepository<ResourceType, Long> {
    List<ResourceType> findAllByOrganizationId(Long onganizationId);
}