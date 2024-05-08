package com.f8.turnera.controllers;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.domain.dtos.ResourceDTO;
import com.f8.turnera.domain.dtos.ResourceFilterDTO;
import com.f8.turnera.domain.services.IResourceService;

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
public class ResourceController {

    @Autowired
    private IResourceService service;

    @GetMapping("/resources/findAllByFilter")
    @PreAuthorize("hasAuthority('resources.read')")
    public ResponseEntity<Page<ResourceDTO>> findAllByFilter(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            ResourceFilterDTO filter) throws Exception {
        Page<ResourceDTO> result = service.findAllByFilter(token, filter);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/resources/{id}")
    @PreAuthorize("hasAuthority('resources.read')")
    public ResponseEntity<ResourceDTO> getResource(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) throws Exception {
        ResourceDTO result = service.findById(token, id);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/resources")
    @PreAuthorize("hasAuthority('resources.write')")
    public ResponseEntity<ResourceDTO> createResource(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody ResourceDTO resourceDTO) throws Exception {
        ResourceDTO result = service.create(token, resourceDTO);

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/resources")
    @PreAuthorize("hasAuthority('resources.write')")
    public ResponseEntity<ResourceDTO> updateResource(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody ResourceDTO resourceDTO) throws Exception {
        ResourceDTO result = service.update(token, resourceDTO);

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("resources/{id}")
    @PreAuthorize("hasAuthority('resources.delete')")
    public ResponseEntity<Void> deleteResource(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) throws Exception {
        service.deleteById(token, id);

        return ResponseEntity.ok().build();
    }

}
