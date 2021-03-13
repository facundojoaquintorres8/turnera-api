package com.f8.turnera.security.controllers;

import java.util.List;

import com.f8.turnera.security.models.ProfileDTO;
import com.f8.turnera.security.models.ProfileFilterDTO;
import com.f8.turnera.security.services.IProfileService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProfileController {
    private final Logger log = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private IProfileService service;

    @GetMapping("/profiles/findAllByFilter")
    @PreAuthorize("hasAuthority('profiles.read')")
    public ResponseEntity<List<ProfileDTO>> findAllByFilter(ProfileFilterDTO filter) {
        log.info("REST request to get Profiles by filter: {}", filter);

        List<ProfileDTO> result = service.findAllByFilter(filter);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/profiles/{id}")
    @PreAuthorize("hasAuthority('profiles.read')")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Long id) {
        log.info("REST request to get Profile {}", id);

        ProfileDTO result = service.findById(id);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/profiles")
    @PreAuthorize("hasAuthority('profiles.write')")
    public ResponseEntity<ProfileDTO> createProfile(@RequestBody ProfileDTO profileDTO) {
        log.info("REST request to create Profile: {}", profileDTO);

        ProfileDTO result = service.create(profileDTO);

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/profiles")
    @PreAuthorize("hasAuthority('profiles.write')")
    public ResponseEntity<ProfileDTO> updateProfile(@RequestBody ProfileDTO profileDTO) {
        log.info("REST request to update Profile: {}", profileDTO);

        ProfileDTO result = service.update(profileDTO);

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("profiles/{id}")
    @PreAuthorize("hasAuthority('profiles.delete')")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        log.info("REST request to delete Profile {}", id);

        service.deleteById(id);

        return ResponseEntity.ok().build();
    }

}
