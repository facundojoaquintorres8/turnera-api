package com.f8.turnera.security.domain.services;

import com.f8.turnera.security.domain.dtos.ActivateDTO;
import com.f8.turnera.security.domain.dtos.PasswordChangeDTO;
import com.f8.turnera.security.domain.dtos.PasswordResetDTO;
import com.f8.turnera.security.domain.dtos.PasswordResetRequestDTO;
import com.f8.turnera.security.domain.dtos.RegisterDTO;
import com.f8.turnera.security.domain.dtos.UserDTO;

public interface IAccountService {

	UserDTO register(RegisterDTO registerDTO);

	UserDTO activate(ActivateDTO activateDTO);

	UserDTO passwordResetRequest(PasswordResetRequestDTO passwordResetRequestDTO);

	UserDTO passwordReset(PasswordResetDTO passwordResetDTO);

	UserDTO passwordChange(PasswordChangeDTO passwordChangeDTO);    
}
