package com.f8.turnera.controllers;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.domain.dtos.ResourceTypeDTO;
import com.f8.turnera.domain.dtos.ResourceTypeFilterDTO;
import com.f8.turnera.domain.dtos.ResponseDTO;
import com.f8.turnera.domain.services.IResourceTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ResourceTypeController {

    @Autowired
    private IResourceTypeService service;

    @GetMapping("/resources-types/findAllByFilter")
    @PreAuthorize("hasAuthority('resourcesTypes.read')")
    public ResponseEntity<ResponseDTO> findAllByFilter(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            ResourceTypeFilterDTO request) throws Exception {
        return new ResponseEntity<>(service.findAllByFilter(token, request), HttpStatus.OK);
    }

    @GetMapping("/resources-types/{id}")
    @PreAuthorize("hasAuthority('resourcesTypes.read')")
    public ResponseEntity<ResponseDTO> getResourceType(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) throws Exception {
        return new ResponseEntity<>(service.findById(token, id), HttpStatus.OK);
    }

    @PostMapping("/resources-types")
    @PreAuthorize("hasAuthority('resourcesTypes.write')")
    public ResponseEntity<ResponseDTO> createResourceType(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody ResourceTypeDTO request) throws Exception {
        return new ResponseEntity<>(service.create(token, request), HttpStatus.OK);
    }

    @PutMapping("/resources-types")
    @PreAuthorize("hasAuthority('resourcesTypes.write')")
    public ResponseEntity<ResponseDTO> updateResourceType(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody ResourceTypeDTO request) throws Exception {
        return new ResponseEntity<>(service.update(token, request), HttpStatus.OK);
    }

    @DeleteMapping("resources-types/{id}")
    @PreAuthorize("hasAuthority('resourcesTypes.delete')")
    public ResponseEntity<ResponseDTO> deleteResourceType(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) throws Exception {
        return new ResponseEntity<>(service.deleteById(token, id), HttpStatus.OK);
    }

}
