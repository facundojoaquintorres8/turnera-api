package com.f8.turnera.security.domain.services;

import java.util.List;

import com.f8.turnera.security.domain.dtos.PermissionDTO;

public interface IPermissionService {

    List<PermissionDTO> findAll() throws Exception;

    PermissionDTO findByCode(String code) throws Exception;
}
