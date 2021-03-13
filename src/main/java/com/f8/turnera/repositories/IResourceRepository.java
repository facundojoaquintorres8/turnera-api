package com.f8.turnera.repositories;

import java.util.List;

import com.f8.turnera.entities.Resource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findAllByOrganizationId(Long onganizationId);
}