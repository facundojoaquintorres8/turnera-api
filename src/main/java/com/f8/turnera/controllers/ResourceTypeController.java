package com.f8.turnera.controllers;

import com.f8.turnera.domain.dtos.ResourceTypeDTO;
import com.f8.turnera.domain.dtos.ResourceTypeFilterDTO;
import com.f8.turnera.domain.services.IResourceTypeService;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ResourceTypeController {

    @Autowired
    private IResourceTypeService service;

    @GetMapping("/resources-types/findAllByFilter")
    @PreAuthorize("hasAuthority('resourcesTypes.read')")
    public ResponseEntity<Page<ResourceTypeDTO>> findAllByFilter(ResourceTypeFilterDTO filter) {
        Page<ResourceTypeDTO> result = service.findAllByFilter(filter);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/resources-types/{id}")
    @PreAuthorize("hasAuthority('resourcesTypes.read')")
    public ResponseEntity<ResourceTypeDTO> getResourceType(@PathVariable Long id) {
        ResourceTypeDTO result = service.findById(id);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/resources-types")
    @PreAuthorize("hasAuthority('resourcesTypes.write')")
    public ResponseEntity<ResourceTypeDTO> createResourceType(@RequestBody ResourceTypeDTO resourceTypeDTO) {
        ResourceTypeDTO result = service.create(resourceTypeDTO);

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/resources-types")
    @PreAuthorize("hasAuthority('resourcesTypes.write')")
    public ResponseEntity<ResourceTypeDTO> updateResourceType(@RequestBody ResourceTypeDTO resourceTypeDTO) {
        ResourceTypeDTO result = service.update(resourceTypeDTO);

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("resources-types/{id}")
    @PreAuthorize("hasAuthority('resourcesTypes.delete')")
    public ResponseEntity<Void> deleteResourceType(@PathVariable Long id) {
        service.deleteById(id);

        return ResponseEntity.ok().build();
    }

}
