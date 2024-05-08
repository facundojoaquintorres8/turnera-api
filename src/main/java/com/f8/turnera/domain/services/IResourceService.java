package com.f8.turnera.domain.services;

import com.f8.turnera.domain.dtos.ResourceDTO;
import com.f8.turnera.domain.dtos.ResourceFilterDTO;
import com.f8.turnera.domain.dtos.ResponseDTO;

public interface IResourceService {

    ResponseDTO findAllByFilter(String token, ResourceFilterDTO filter) throws Exception;

    ResponseDTO findById(String token, Long id) throws Exception;

    ResponseDTO create(String token, ResourceDTO resourceDTO) throws Exception;

    ResponseDTO update(String token, ResourceDTO resourceDTO) throws Exception;

    ResponseDTO deleteById(String token, Long id) throws Exception;
}
