package com.f8.turnera.domain.services;

import org.springframework.data.domain.Page;

import com.f8.turnera.domain.dtos.ResourceTypeDTO;
import com.f8.turnera.domain.dtos.ResourceTypeFilterDTO;

public interface IResourceTypeService {

    Page<ResourceTypeDTO> findAllByFilter(String token, ResourceTypeFilterDTO filter) throws Exception;

    ResourceTypeDTO findById(String token, Long id) throws Exception;

    ResourceTypeDTO create(String token, ResourceTypeDTO resourceTypeDTO) throws Exception;

    ResourceTypeDTO update(String token, ResourceTypeDTO resourceTypeDTO) throws Exception;

    void deleteById(String token, Long id) throws Exception;
}
