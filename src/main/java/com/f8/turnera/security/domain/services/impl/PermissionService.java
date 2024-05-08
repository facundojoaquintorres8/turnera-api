package com.f8.turnera.security.domain.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.f8.turnera.security.domain.dtos.PermissionDTO;
import com.f8.turnera.security.domain.entities.Permission;
import com.f8.turnera.security.domain.repositories.IPermissionRepository;
import com.f8.turnera.security.domain.services.IPermissionService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService implements IPermissionService {

    @Autowired
    private IPermissionRepository repository;

    @Override
    public List<PermissionDTO> findAll() throws Exception {
        ModelMapper modelMapper = new ModelMapper();

        List<Permission> permissions = repository.findAll();
        return permissions.stream().map(permission -> modelMapper.map(permission, PermissionDTO.class)).collect(Collectors.toList());
    }

    @Override
    public PermissionDTO findByCode(String code) throws Exception {
        Optional<Permission> permission = repository.findByCode(code);
        if (!permission.isPresent()) {
            return null;
        }

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(permission.get(), PermissionDTO.class);
    }
}
