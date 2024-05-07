package com.f8.turnera.security.domain.services;

import com.f8.turnera.security.domain.dtos.LoginDTO;
import com.f8.turnera.security.domain.dtos.SessionUserDTO;

public interface IAuthService {
    SessionUserDTO login(LoginDTO authDTO);
}
