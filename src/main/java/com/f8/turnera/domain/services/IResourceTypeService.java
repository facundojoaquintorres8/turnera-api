package com.f8.turnera.domain.services;

import org.springframework.data.domain.Page;

import com.f8.turnera.domain.dtos.ResourceTypeDTO;
import com.f8.turnera.domain.dtos.ResourceTypeFilterDTO;

public interface IResourceTypeService {

    public Page<ResourceTypeDTO> findAllByFilter(String token, ResourceTypeFilterDTO filter);

    public ResourceTypeDTO findById(String token, Long id);

    public ResourceTypeDTO create(String token, ResourceTypeDTO resourceTypeDTO);

    public ResourceTypeDTO update(String token, ResourceTypeDTO resourceTypeDTO);

    public void deleteById(String token, Long id);
}
