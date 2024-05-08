package com.f8.turnera.security.domain.services;

import com.f8.turnera.security.domain.dtos.LoginDTO;
import com.f8.turnera.security.domain.dtos.ResponseDTO;

public interface IAuthService {

    ResponseDTO login(LoginDTO authDTO) throws Exception;

}
