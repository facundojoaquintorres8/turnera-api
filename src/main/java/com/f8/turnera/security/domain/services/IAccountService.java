package com.f8.turnera.security.domain.services;

import com.f8.turnera.security.domain.dtos.ActivateDTO;
import com.f8.turnera.security.domain.dtos.PasswordChangeDTO;
import com.f8.turnera.security.domain.dtos.PasswordResetDTO;
import com.f8.turnera.security.domain.dtos.PasswordResetRequestDTO;
import com.f8.turnera.security.domain.dtos.RegisterDTO;
import com.f8.turnera.security.domain.dtos.ResponseDTO;

public interface IAccountService {

	ResponseDTO register(RegisterDTO registerDTO) throws Exception;

	ResponseDTO activate(ActivateDTO activateDTO) throws Exception;

	ResponseDTO passwordResetRequest(PasswordResetRequestDTO passwordResetRequestDTO) throws Exception;

	ResponseDTO passwordReset(PasswordResetDTO passwordResetDTO) throws Exception;

	ResponseDTO passwordChange(String token, PasswordChangeDTO passwordChangeDTO) throws Exception;    
}
