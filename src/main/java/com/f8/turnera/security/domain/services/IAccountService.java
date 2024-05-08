package com.f8.turnera.security.domain.services;

import com.f8.turnera.security.domain.dtos.ActivateDTO;
import com.f8.turnera.security.domain.dtos.PasswordChangeDTO;
import com.f8.turnera.security.domain.dtos.PasswordResetDTO;
import com.f8.turnera.security.domain.dtos.PasswordResetRequestDTO;
import com.f8.turnera.security.domain.dtos.RegisterDTO;
import com.f8.turnera.security.domain.dtos.UserDTO;

public interface IAccountService {

	UserDTO register(RegisterDTO registerDTO) throws Exception;

	UserDTO activate(ActivateDTO activateDTO) throws Exception;

	UserDTO passwordResetRequest(PasswordResetRequestDTO passwordResetRequestDTO) throws Exception;

	UserDTO passwordReset(PasswordResetDTO passwordResetDTO) throws Exception;

	UserDTO passwordChange(String token, PasswordChangeDTO passwordChangeDTO) throws Exception;    
}
