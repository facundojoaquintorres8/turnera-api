package com.f8.turnera.services;

import java.util.List;

import com.f8.turnera.models.ResourceDTO;
import com.f8.turnera.models.ResourceFilterDTO;

public interface IResourceService {

    public List<ResourceDTO> findAllByFilter(ResourceFilterDTO filter);

    public ResourceDTO findById(Long id);

    public ResourceDTO create(ResourceDTO resourceDTO);

    public ResourceDTO update(ResourceDTO resourceDTO);

    public void deleteById(Long id);
}
