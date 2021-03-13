package com.f8.turnera.security.controllers;

import java.util.List;

import com.f8.turnera.security.models.UserDTO;
import com.f8.turnera.security.models.UserFilterDTO;
import com.f8.turnera.security.services.IUserService;

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
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService service;

    @GetMapping("/users/findAllByFilter")
    @PreAuthorize("hasAuthority('users.read')")
    public ResponseEntity<List<UserDTO>> findAllByFilter(UserFilterDTO filter) {
        log.info("REST request to get Users by filter: {}", filter);

        List<UserDTO> result = service.findAllByFilter(filter);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('users.read')")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        log.info("REST request to get User {}", id);

        UserDTO result = service.findById(id);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('users.write')")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        log.info("REST request to create User: {}", userDTO);

        UserDTO result = service.create(userDTO);

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/users")
    @PreAuthorize("hasAuthority('users.write')")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        log.info("REST request to update User: {}", userDTO);

        UserDTO result = service.update(userDTO);

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("users/{id}")
    @PreAuthorize("hasAuthority('users.delete')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("REST request to delete User {}", id);

        service.deleteById(id);

        return ResponseEntity.ok().build();
    }

}
