package com.f8.turnera.domain.services;

import org.springframework.data.domain.Page;

import com.f8.turnera.domain.dtos.ResourceDTO;
import com.f8.turnera.domain.dtos.ResourceFilterDTO;

public interface IResourceService {

    public Page<ResourceDTO> findAllByFilter(String token, ResourceFilterDTO filter);

    public ResourceDTO findById(String token, Long id);

    public ResourceDTO create(String token, ResourceDTO resourceDTO);

    public ResourceDTO update(String token, ResourceDTO resourceDTO);

    public void deleteById(String token, Long id);
}
