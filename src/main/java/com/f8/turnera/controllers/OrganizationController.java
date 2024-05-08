package com.f8.turnera.controllers;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.domain.dtos.OrganizationDTO;
import com.f8.turnera.domain.dtos.ResponseDTO;
import com.f8.turnera.domain.services.IOrganizationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrganizationController {

    @Autowired
    private IOrganizationService service;

    @GetMapping("/organizations")
    @PreAuthorize("hasAuthority('organizations.read')")
    public ResponseEntity<ResponseDTO> getOrganization(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token) throws Exception {
        return new ResponseEntity<>(service.findById(token), HttpStatus.OK);
    }

    @PutMapping("/organizations")
    @PreAuthorize("hasAuthority('organizations.write')")
    public ResponseEntity<ResponseDTO> updateOrganization(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody OrganizationDTO request) throws Exception {
        return new ResponseEntity<>(service.update(token, request), HttpStatus.OK);
    }

}
