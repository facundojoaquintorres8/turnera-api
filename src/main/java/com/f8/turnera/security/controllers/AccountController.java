package com.f8.turnera.security.controllers;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.security.domain.dtos.ActivateDTO;
import com.f8.turnera.security.domain.dtos.PasswordChangeDTO;
import com.f8.turnera.security.domain.dtos.PasswordResetDTO;
import com.f8.turnera.security.domain.dtos.PasswordResetRequestDTO;
import com.f8.turnera.security.domain.dtos.RegisterDTO;
import com.f8.turnera.security.domain.dtos.UserDTO;
import com.f8.turnera.security.domain.services.IAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private IAccountService service;

    @PostMapping("/account/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterDTO registerDTO) throws Exception {
        UserDTO result = service.register(registerDTO);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/account/activate")
    public ResponseEntity<UserDTO> activate(@RequestBody ActivateDTO activateDTO) throws Exception {
        UserDTO result = service.activate(activateDTO);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/account/password-reset/request")
    public ResponseEntity<UserDTO> passwordResetRequest(@RequestBody PasswordResetRequestDTO passwordResetRequestDTO) throws Exception {
        UserDTO result = service.passwordResetRequest(passwordResetRequestDTO);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/account/password-reset")
    public ResponseEntity<UserDTO> passwordReset(@RequestBody PasswordResetDTO passwordResetDTO) throws Exception {
        UserDTO result = service.passwordReset(passwordResetDTO);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/account/password-change")
    public ResponseEntity<UserDTO> passwordChange(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody PasswordChangeDTO passwordChangeDTO) throws Exception {
        UserDTO result = service.passwordChange(token, passwordChangeDTO);
        return ResponseEntity.ok().body(result);
    }
}
