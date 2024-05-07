package com.f8.turnera.domain.services;

import org.springframework.data.domain.Page;

import com.f8.turnera.domain.dtos.ResourceDTO;
import com.f8.turnera.domain.dtos.ResourceFilterDTO;

public interface IResourceService {

    public Page<ResourceDTO> findAllByFilter(ResourceFilterDTO filter);

    public ResourceDTO findById(Long id);

    public ResourceDTO create(ResourceDTO resourceDTO);

    public ResourceDTO update(ResourceDTO resourceDTO);

    public void deleteById(Long id);
}
