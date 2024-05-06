package com.f8.turnera.security.services;

import java.util.List;

import com.f8.turnera.security.models.PermissionDTO;

public interface IPermissionService {

    public List<PermissionDTO> findAll();

    public PermissionDTO findByCode(String code);
}
