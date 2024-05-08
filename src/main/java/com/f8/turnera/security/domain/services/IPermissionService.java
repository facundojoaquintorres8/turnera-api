package com.f8.turnera.security.domain.services;

import com.f8.turnera.security.domain.dtos.PermissionDTO;
import com.f8.turnera.security.domain.dtos.ResponseDTO;

public interface IPermissionService {

    ResponseDTO findAll() throws Exception;

    PermissionDTO findByCode(String code) throws Exception;
}
