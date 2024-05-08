package com.f8.turnera.security.controllers;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.security.domain.dtos.ProfileDTO;
import com.f8.turnera.security.domain.dtos.ProfileFilterDTO;
import com.f8.turnera.security.domain.services.IProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<ProfileDTO>> findAllByFilter(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            ProfileFilterDTO filter) throws Exception {
        Page<ProfileDTO> result = service.findAllByFilter(token, filter);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/profiles/{id}")
    @PreAuthorize("hasAuthority('profiles.read')")
    public ResponseEntity<ProfileDTO> getProfile(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) throws Exception {
        ProfileDTO result = service.findById(token, id);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/profiles")
    @PreAuthorize("hasAuthority('profiles.write')")
    public ResponseEntity<ProfileDTO> createProfile(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody ProfileDTO profileDTO) throws Exception {
        ProfileDTO result = service.create(token, profileDTO);

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/profiles")
    @PreAuthorize("hasAuthority('profiles.write')")
    public ResponseEntity<ProfileDTO> updateProfile(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody ProfileDTO profileDTO) throws Exception {
        ProfileDTO result = service.update(token, profileDTO);

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("profiles/{id}")
    @PreAuthorize("hasAuthority('profiles.delete')")
    public ResponseEntity<Void> deleteProfile(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) throws Exception {
        service.deleteById(token, id);

        return ResponseEntity.ok().build();
    }

}
