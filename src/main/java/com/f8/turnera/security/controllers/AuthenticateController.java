package com.f8.turnera.security.controllers;

import com.f8.turnera.security.domain.dtos.LoginDTO;
import com.f8.turnera.security.domain.dtos.SessionUserDTO;
import com.f8.turnera.security.domain.services.IAuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticateController {

  @Autowired
  private IAuthService authService;

  @PostMapping("/authenticate")
  public ResponseEntity<SessionUserDTO> authenticate(@RequestBody LoginDTO login) {
    SessionUserDTO result = authService.login(login);
    return ResponseEntity.ok().body(result);
  }
}
