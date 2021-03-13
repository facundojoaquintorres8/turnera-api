package com.f8.turnera.security.controllers;

import com.f8.turnera.security.models.ActivateDTO;
import com.f8.turnera.security.models.PasswordChangeDTO;
import com.f8.turnera.security.models.PasswordResetDTO;
import com.f8.turnera.security.models.PasswordResetRequestDTO;
import com.f8.turnera.security.models.RegisterDTO;
import com.f8.turnera.security.models.UserDTO;
import com.f8.turnera.security.services.IAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private IAccountService service;

    @PostMapping("/account/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterDTO registerDTO) {
        UserDTO result = service.register(registerDTO);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/account/activate")
    public ResponseEntity<UserDTO> activate(@RequestBody ActivateDTO activateDTO) {
        UserDTO result = service.activate(activateDTO);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/account/password-reset/request")
    public ResponseEntity<UserDTO> passwordResetRequest(@RequestBody PasswordResetRequestDTO passwordResetRequestDTO) {
        UserDTO result = service.passwordResetRequest(passwordResetRequestDTO);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/account/password-reset")
    public ResponseEntity<UserDTO> passwordReset(@RequestBody PasswordResetDTO passwordResetDTO) {
        UserDTO result = service.passwordReset(passwordResetDTO);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/account/password-change")
    public ResponseEntity<UserDTO> passwordChange(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        UserDTO result = service.passwordChange(passwordChangeDTO);
        return ResponseEntity.ok().body(result);
    }
}
