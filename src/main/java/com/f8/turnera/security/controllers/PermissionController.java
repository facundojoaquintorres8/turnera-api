package com.f8.turnera.security.controllers;

import com.f8.turnera.security.domain.dtos.ResponseDTO;
import com.f8.turnera.security.domain.services.IPermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PermissionController {

    @Autowired
    private IPermissionService service;

    @GetMapping("/permissions")
    @PreAuthorize("hasAuthority('profiles.write')")
    public ResponseEntity<ResponseDTO> findAll() throws Exception {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }
}
