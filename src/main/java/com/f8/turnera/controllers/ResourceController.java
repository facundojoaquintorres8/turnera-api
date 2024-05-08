package com.f8.turnera.controllers;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.domain.dtos.ResourceDTO;
import com.f8.turnera.domain.dtos.ResourceFilterDTO;
import com.f8.turnera.domain.dtos.ResponseDTO;
import com.f8.turnera.domain.services.IResourceService;

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
public class ResourceController {

    @Autowired
    private IResourceService service;

    @GetMapping("/resources/findAllByFilter")
    @PreAuthorize("hasAuthority('resources.read')")
    public ResponseEntity<ResponseDTO> findAllByFilter(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            ResourceFilterDTO request) throws Exception {
        return new ResponseEntity<>(service.findAllByFilter(token, request), HttpStatus.OK);
    }

    @GetMapping("/resources/{id}")
    @PreAuthorize("hasAuthority('resources.read')")
    public ResponseEntity<ResponseDTO> getResource(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) throws Exception {
        return new ResponseEntity<>(service.findById(token, id), HttpStatus.OK);
    }

    @PostMapping("/resources")
    @PreAuthorize("hasAuthority('resources.write')")
    public ResponseEntity<ResponseDTO> createResource(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody ResourceDTO request) throws Exception {
        return new ResponseEntity<>(service.create(token, request), HttpStatus.OK);
    }

    @PutMapping("/resources")
    @PreAuthorize("hasAuthority('resources.write')")
    public ResponseEntity<ResponseDTO> updateResource(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody ResourceDTO request) throws Exception {
        return new ResponseEntity<>(service.update(token, request), HttpStatus.OK);
    }

    @DeleteMapping("resources/{id}")
    @PreAuthorize("hasAuthority('resources.delete')")
    public ResponseEntity<ResponseDTO> deleteResource(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) throws Exception {
        return new ResponseEntity<>(service.deleteById(token, id), HttpStatus.OK);
    }

}
