package com.f8.turnera.domain.services.impl;

import java.util.Optional;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.config.TokenUtil;
import com.f8.turnera.domain.dtos.OrganizationDTO;
import com.f8.turnera.domain.entities.Organization;
import com.f8.turnera.domain.repositories.IOrganizationRepository;
import com.f8.turnera.domain.services.IOrganizationService;
import com.f8.turnera.exception.NoContentException;
import com.f8.turnera.util.EmailValidation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService implements IOrganizationService {

    @Autowired
    private IOrganizationRepository organizationRepository;

    @Override
    public OrganizationDTO findById(String token) throws Exception {
        Long organizationId = Long.parseLong(TokenUtil.getClaimByToken(token, SecurityConstants.ORGANIZATION_KEY).toString());
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (!organization.isPresent()) {
            throw new NoContentException("Organización no encontrada - " + organizationId);
        }

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(organization.get(), OrganizationDTO.class);
    }

    @Override
    public OrganizationDTO update(String token, OrganizationDTO organizationDTO) throws Exception {
        Long organizationId = Long.parseLong(TokenUtil.getClaimByToken(token, SecurityConstants.ORGANIZATION_KEY).toString());
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (!organization.isPresent()) {
            throw new NoContentException("Organización no encontrada - " + organizationId);
        }

        EmailValidation.validateEmail(organizationDTO.getDefaultEmail());

        ModelMapper modelMapper = new ModelMapper();

        organization.ifPresent(o -> {
            o.setBusinessName(organizationDTO.getBusinessName());
            o.setBrandName(organizationDTO.getBrandName());
            o.setCuit(organizationDTO.getCuit());
            o.setAddress(organizationDTO.getAddress());
            o.setPhone1(organizationDTO.getPhone1());
            o.setPhone2(organizationDTO.getPhone2());
            o.setDefaultEmail(organizationDTO.getDefaultEmail());

            organizationRepository.save(o);
        });
        
        return modelMapper.map(organization.get(), OrganizationDTO.class);
    }

}