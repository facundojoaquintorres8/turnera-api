package com.f8.turnera.services;

import java.util.List;

import com.f8.turnera.models.ResourceTypeDTO;
import com.f8.turnera.models.ResourceTypeFilterDTO;

public interface IResourceTypeService {

    public List<ResourceTypeDTO> findAllByFilter(ResourceTypeFilterDTO filter);

    public ResourceTypeDTO findById(Long id);

    public ResourceTypeDTO create(ResourceTypeDTO resourceTypeDTO);

    public ResourceTypeDTO update(ResourceTypeDTO resourceTypeDTO);

    public void deleteById(Long id);
}
