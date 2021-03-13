package com.f8.turnera.services;

import com.f8.turnera.models.OrganizationDTO;

public interface IOrganizationService {

    public OrganizationDTO findById(Long id);

    public OrganizationDTO update(OrganizationDTO organizationDTO);
}
