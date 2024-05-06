package com.f8.turnera.controllers;

import com.f8.turnera.models.OrganizationDTO;
import com.f8.turnera.services.IOrganizationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrganizationController {

    @Autowired
    private IOrganizationService service;
    
    @GetMapping("/organizations/{id}")
    @PreAuthorize("hasAuthority('organizations.read')")
    public ResponseEntity<OrganizationDTO> getCustomer(@PathVariable Long id) {
        OrganizationDTO result = service.findById(id);

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/organizations")
    @PreAuthorize("hasAuthority('organizations.write')")
    public ResponseEntity<OrganizationDTO> updateOrganization(@RequestBody OrganizationDTO organizationDTO) {
        OrganizationDTO result = service.update(organizationDTO);

        return ResponseEntity.ok().body(result);
    }

}
