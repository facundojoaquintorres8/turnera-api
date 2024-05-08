package com.f8.turnera.domain.services;

import com.f8.turnera.domain.dtos.OrganizationDTO;

public interface IOrganizationService {

    public OrganizationDTO findById(String token);

    public OrganizationDTO update(String token, OrganizationDTO organizationDTO);
}
