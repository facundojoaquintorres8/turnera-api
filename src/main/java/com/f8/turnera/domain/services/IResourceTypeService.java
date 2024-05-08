package com.f8.turnera.domain.services;

import com.f8.turnera.domain.dtos.ResourceTypeDTO;
import com.f8.turnera.domain.dtos.ResourceTypeFilterDTO;
import com.f8.turnera.domain.dtos.ResponseDTO;

public interface IResourceTypeService {

    ResponseDTO findAllByFilter(String token, ResourceTypeFilterDTO filter) throws Exception;

    ResponseDTO findById(String token, Long id) throws Exception;

    ResponseDTO create(String token, ResourceTypeDTO resourceTypeDTO) throws Exception;

    ResponseDTO update(String token, ResourceTypeDTO resourceTypeDTO) throws Exception;

    ResponseDTO deleteById(String token, Long id) throws Exception;
}
