package com.f8.turnera.controllers;

import java.util.List;

import com.f8.turnera.models.ResourceDTO;
import com.f8.turnera.models.ResourceFilterDTO;
import com.f8.turnera.services.IResourceService;

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
public class ResourceController {
    private final Logger log = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private IResourceService service;

    @GetMapping("/resources/findAllByFilter")
    @PreAuthorize("hasAuthority('resources.read')")
    public ResponseEntity<List<ResourceDTO>> findAllByFilter(ResourceFilterDTO filter) {
        log.info("REST request to get Resources by filter: {}", filter);

        List<ResourceDTO> result = service.findAllByFilter(filter);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/resources/{id}")
    @PreAuthorize("hasAuthority('resources.read')")
    public ResponseEntity<ResourceDTO> getResource(@PathVariable Long id) {
        log.info("REST request to get Resource {}", id);

        ResourceDTO result = service.findById(id);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/resources")
    @PreAuthorize("hasAuthority('resources.write')")
    public ResponseEntity<ResourceDTO> createResource(@RequestBody ResourceDTO resourceDTO) {
        log.info("REST request to create Resource: {}", resourceDTO);

        ResourceDTO result = service.create(resourceDTO);

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/resources")
    @PreAuthorize("hasAuthority('resources.write')")
    public ResponseEntity<ResourceDTO> updateResource(@RequestBody ResourceDTO resourceDTO) {
        log.info("REST request to update Resource: {}", resourceDTO);

        ResourceDTO result = service.update(resourceDTO);

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("resources/{id}")
    @PreAuthorize("hasAuthority('resources.delete')")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        log.info("REST request to delete Resource {}", id);

        service.deleteById(id);

        return ResponseEntity.ok().build();
    }

}
