package com.f8.turnera.domain.services;

import com.f8.turnera.domain.dtos.OrganizationDTO;

public interface IOrganizationService {

    OrganizationDTO findById(String token) throws Exception;

    OrganizationDTO update(String token, OrganizationDTO organizationDTO) throws Exception;
}
