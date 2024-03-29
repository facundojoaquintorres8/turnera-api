package com.f8.turnera.security.services;

import java.util.List;
import java.util.stream.Collectors;

import com.f8.turnera.security.entities.Permission;
import com.f8.turnera.security.models.PermissionDTO;
import com.f8.turnera.security.repositories.IPermissionRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService implements IPermissionService {

    @Autowired
    private IPermissionRepository repository;

    @Override
    public List<PermissionDTO> findAll() {
        ModelMapper modelMapper = new ModelMapper();

        List<Permission> permissions = repository.findAll();
        return permissions.stream().map(permission -> modelMapper.map(permission, PermissionDTO.class)).collect(Collectors.toList());
    }
}
