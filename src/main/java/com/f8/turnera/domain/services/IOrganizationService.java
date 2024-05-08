package com.f8.turnera.domain.services;

import com.f8.turnera.domain.dtos.OrganizationDTO;
import com.f8.turnera.domain.dtos.ResponseDTO;

public interface IOrganizationService {

    ResponseDTO findById(String token) throws Exception;

    ResponseDTO update(String token, OrganizationDTO organizationDTO) throws Exception;
}
