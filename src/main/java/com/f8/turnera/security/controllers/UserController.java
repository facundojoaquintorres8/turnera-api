package com.f8.turnera.security.controllers;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.security.domain.dtos.UserDTO;
import com.f8.turnera.security.domain.dtos.UserFilterDTO;
import com.f8.turnera.security.domain.services.IUserService;

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
public class UserController {

    @Autowired
    private IUserService service;

    @GetMapping("/users/findAllByFilter")
    @PreAuthorize("hasAuthority('users.read')")
    public ResponseEntity<Page<UserDTO>> findAllByFilter(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            UserFilterDTO filter) throws Exception {
        Page<UserDTO> result = service.findAllByFilter(token, filter);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('users.read')")
    public ResponseEntity<UserDTO> getUser(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) throws Exception {
        UserDTO result = service.findById(token, id);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('users.write')")
    public ResponseEntity<UserDTO> createUser(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody UserDTO userDTO) throws Exception {
        UserDTO result = service.create(token, userDTO);

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/users")
    @PreAuthorize("hasAuthority('users.write')")
    public ResponseEntity<UserDTO> updateUser(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody UserDTO userDTO) throws Exception {
        UserDTO result = service.update(token, userDTO);

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("users/{id}")
    @PreAuthorize("hasAuthority('users.delete')")
    public ResponseEntity<Void> deleteUser(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) throws Exception {
        service.deleteById(token, id);

        return ResponseEntity.ok().build();
    }

}
