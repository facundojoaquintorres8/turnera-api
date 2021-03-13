package com.f8.turnera.security.services;

import com.f8.turnera.security.models.LoginDTO;
import com.f8.turnera.security.models.SessionUserDTO;

public interface IAuthService {
    SessionUserDTO login(LoginDTO authDTO);
}
