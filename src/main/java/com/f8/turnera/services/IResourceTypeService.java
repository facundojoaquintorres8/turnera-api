package com.f8.turnera.services;

import com.f8.turnera.models.ResourceTypeDTO;
import com.f8.turnera.models.ResourceTypeFilterDTO;

import org.springframework.data.domain.Page;

public interface IResourceTypeService {

    public Page<ResourceTypeDTO> findAllByFilter(ResourceTypeFilterDTO filter);

    public ResourceTypeDTO findById(Long id);

    public ResourceTypeDTO create(ResourceTypeDTO resourceTypeDTO);

    public ResourceTypeDTO update(ResourceTypeDTO resourceTypeDTO);

    public void deleteById(Long id);
}
