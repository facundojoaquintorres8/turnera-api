package com.f8.turnera.domain.services;

import com.f8.turnera.domain.dtos.OrganizationDTO;

public interface IOrganizationService {

    public OrganizationDTO findById(Long id);

    public OrganizationDTO update(OrganizationDTO organizationDTO);
}
