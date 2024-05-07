package com.f8.turnera.domain.services;

import org.springframework.data.domain.Page;

import com.f8.turnera.domain.dtos.ResourceTypeDTO;
import com.f8.turnera.domain.dtos.ResourceTypeFilterDTO;

public interface IResourceTypeService {

    public Page<ResourceTypeDTO> findAllByFilter(ResourceTypeFilterDTO filter);

    public ResourceTypeDTO findById(Long id);

    public ResourceTypeDTO create(ResourceTypeDTO resourceTypeDTO);

    public ResourceTypeDTO update(ResourceTypeDTO resourceTypeDTO);

    public void deleteById(Long id);
}
