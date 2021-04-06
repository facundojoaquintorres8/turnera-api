package com.f8.turnera.services;

import com.f8.turnera.models.ResourceDTO;
import com.f8.turnera.models.ResourceFilterDTO;

import org.springframework.data.domain.Page;

public interface IResourceService {

    public Page<ResourceDTO> findAllByFilter(ResourceFilterDTO filter);

    public ResourceDTO findById(Long id);

    public ResourceDTO create(ResourceDTO resourceDTO);

    public ResourceDTO update(ResourceDTO resourceDTO);

    public void deleteById(Long id);
}
