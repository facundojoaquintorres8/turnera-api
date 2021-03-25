package com.f8.turnera.repositories;

import com.f8.turnera.entities.Resource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IResourceRepository extends JpaRepository<Resource, Long> {}