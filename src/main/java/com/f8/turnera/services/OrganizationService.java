package com.f8.turnera.services;

import java.util.Optional;

import com.f8.turnera.entities.Organization;
import com.f8.turnera.models.OrganizationDTO;
import com.f8.turnera.repositories.IOrganizationRepository;
import com.f8.turnera.util.EmailValidation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService implements IOrganizationService {

    @Autowired
    private IOrganizationRepository organizationRepository;

    @Override
    public OrganizationDTO findById(Long id) {
        Optional<Organization> organization = organizationRepository.findById(id);
        if (!organization.isPresent()) {
            throw new RuntimeException("Organización no encontrada - " + id);
        }

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(organization.get(), OrganizationDTO.class);
    }

    @Override
    public OrganizationDTO update(OrganizationDTO organizationDTO) {
        Optional<Organization> organization = organizationRepository.findById(organizationDTO.getId());
        if (!organization.isPresent()) {
            throw new RuntimeException("Organización no encontrada - " + organizationDTO.getId());
        }

        if (!EmailValidation.validateEmail(organizationDTO.getDefaultEmail())) {
            throw new RuntimeException("El Correo Electrónico es inválido.");
        }

        ModelMapper modelMapper = new ModelMapper();

        try {
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

        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
        return modelMapper.map(organization.get(), OrganizationDTO.class);
    }

}