package com.f8.turnera.security.services;

import com.f8.turnera.security.models.ActivateDTO;
import com.f8.turnera.security.models.PasswordChangeDTO;
import com.f8.turnera.security.models.PasswordResetDTO;
import com.f8.turnera.security.models.PasswordResetRequestDTO;
import com.f8.turnera.security.models.RegisterDTO;
import com.f8.turnera.security.models.UserDTO;

public interface IAccountService {

	UserDTO register(RegisterDTO registerDTO);

	UserDTO activate(ActivateDTO activateDTO);

	UserDTO passwordResetRequest(PasswordResetRequestDTO passwordResetRequestDTO);

	UserDTO passwordReset(PasswordResetDTO passwordResetDTO);

	UserDTO passwordChange(PasswordChangeDTO passwordChangeDTO);    
}
