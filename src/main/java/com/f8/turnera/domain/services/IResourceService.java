package com.f8.turnera.domain.services;

import org.springframework.data.domain.Page;

import com.f8.turnera.domain.dtos.ResourceDTO;
import com.f8.turnera.domain.dtos.ResourceFilterDTO;

public interface IResourceService {

    Page<ResourceDTO> findAllByFilter(String token, ResourceFilterDTO filter) throws Exception;

    ResourceDTO findById(String token, Long id) throws Exception;

    ResourceDTO create(String token, ResourceDTO resourceDTO) throws Exception;

    ResourceDTO update(String token, ResourceDTO resourceDTO) throws Exception;

    void deleteById(String token, Long id) throws Exception;
}
