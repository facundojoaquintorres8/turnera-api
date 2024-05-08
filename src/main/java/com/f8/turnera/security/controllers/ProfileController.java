package com.f8.turnera.security.controllers;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.security.domain.dtos.ProfileDTO;
import com.f8.turnera.security.domain.dtos.ProfileFilterDTO;
import com.f8.turnera.security.domain.dtos.ResponseDTO;
import com.f8.turnera.security.domain.services.IProfileService;

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
public class ProfileController {

    @Autowired
    private IProfileService service;

    @GetMapping("/profiles/findAllByFilter")
    @PreAuthorize("hasAuthority('profiles.read')")
    public ResponseEntity<ResponseDTO> findAllByFilter(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            ProfileFilterDTO request) throws Exception {
        return new ResponseEntity<>(service.findAllByFilter(token, request), HttpStatus.OK);
    }

    @GetMapping("/profiles/{id}")
    @PreAuthorize("hasAuthority('profiles.read')")
    public ResponseEntity<ResponseDTO> getProfile(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) throws Exception {
        return new ResponseEntity<>(service.findById(token, id), HttpStatus.OK);
    }

    @PostMapping("/profiles")
    @PreAuthorize("hasAuthority('profiles.write')")
    public ResponseEntity<ResponseDTO> createProfile(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody ProfileDTO request) throws Exception {
        return new ResponseEntity<>(service.create(token, request), HttpStatus.OK);
    }

    @PutMapping("/profiles")
    @PreAuthorize("hasAuthority('profiles.write')")
    public ResponseEntity<ResponseDTO> updateProfile(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody ProfileDTO request) throws Exception {
        return new ResponseEntity<>(service.update(token, request), HttpStatus.OK);
    }

    @DeleteMapping("profiles/{id}")
    @PreAuthorize("hasAuthority('profiles.delete')")
    public ResponseEntity<ResponseDTO> deleteProfile(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) throws Exception {
        return new ResponseEntity<>(service.deleteById(token, id), HttpStatus.OK);
    }

}
