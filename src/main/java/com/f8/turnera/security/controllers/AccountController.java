package com.f8.turnera.security.controllers;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.security.domain.dtos.ActivateDTO;
import com.f8.turnera.security.domain.dtos.PasswordChangeDTO;
import com.f8.turnera.security.domain.dtos.PasswordResetDTO;
import com.f8.turnera.security.domain.dtos.PasswordResetRequestDTO;
import com.f8.turnera.security.domain.dtos.RegisterDTO;
import com.f8.turnera.security.domain.dtos.ResponseDTO;
import com.f8.turnera.security.domain.services.IAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ResponseDTO> register(@RequestBody RegisterDTO request) throws Exception {
        return new ResponseEntity<>(service.register(request), HttpStatus.OK);
    }

    @PostMapping("/account/activate")
    public ResponseEntity<ResponseDTO> activate(@RequestBody ActivateDTO request) throws Exception {
        return new ResponseEntity<>(service.activate(request), HttpStatus.OK);
    }

    @PostMapping("/account/password-reset/request")
    public ResponseEntity<ResponseDTO> passwordResetRequest(@RequestBody PasswordResetRequestDTO request)
            throws Exception {
        return new ResponseEntity<>(service.passwordResetRequest(request), HttpStatus.OK);
    }

    @PostMapping("/account/password-reset")
    public ResponseEntity<ResponseDTO> passwordReset(@RequestBody PasswordResetDTO request) throws Exception {
        return new ResponseEntity<>(service.passwordReset(request), HttpStatus.OK);
    }

    @PostMapping("/account/password-change")
    public ResponseEntity<ResponseDTO> passwordChange(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody PasswordChangeDTO request) throws Exception {
        return new ResponseEntity<>(service.passwordChange(token, request), HttpStatus.OK);
    }
}
