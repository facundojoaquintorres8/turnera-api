package com.f8.turnera.controllers;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.domain.dtos.OrganizationDTO;
import com.f8.turnera.domain.services.IOrganizationService;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<OrganizationDTO> getOrganization(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token) throws Exception {
        OrganizationDTO result = service.findById(token);

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/organizations")
    @PreAuthorize("hasAuthority('organizations.write')")
    public ResponseEntity<OrganizationDTO> updateOrganization(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody OrganizationDTO organizationDTO) throws Exception {
        OrganizationDTO result = service.update(token, organizationDTO);

        return ResponseEntity.ok().body(result);
    }

}
