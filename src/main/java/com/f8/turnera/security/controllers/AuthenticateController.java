package com.f8.turnera.security.controllers;

import com.f8.turnera.security.domain.dtos.LoginDTO;
import com.f8.turnera.security.domain.dtos.ResponseDTO;
import com.f8.turnera.security.domain.services.IAuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticateController {

  @Autowired
  private IAuthService service;

  @PostMapping("/authenticate")
  public ResponseEntity<ResponseDTO> authenticate(@RequestBody LoginDTO request) throws Exception {
    return new ResponseEntity<>(service.login(request), HttpStatus.OK);
  }
}
