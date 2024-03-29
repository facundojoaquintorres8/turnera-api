package com.f8.turnera.security.controllers;

import java.util.List;

import com.f8.turnera.security.models.PermissionDTO;
import com.f8.turnera.security.services.IPermissionService;

import org.springframework.beans.factory.annotation.Autowired;
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
    public List<PermissionDTO> findAll() {
        return service.findAll();
    }
}
