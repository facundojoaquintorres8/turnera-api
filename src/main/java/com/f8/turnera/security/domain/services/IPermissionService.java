package com.f8.turnera.security.domain.services;

import java.util.List;

import com.f8.turnera.security.domain.dtos.PermissionDTO;

public interface IPermissionService {

    public List<PermissionDTO> findAll();

    public PermissionDTO findByCode(String code);
}
