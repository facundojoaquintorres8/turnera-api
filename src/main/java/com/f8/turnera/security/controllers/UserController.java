package com.f8.turnera.security.controllers;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.security.domain.dtos.ResponseDTO;
import com.f8.turnera.security.domain.dtos.UserDTO;
import com.f8.turnera.security.domain.dtos.UserFilterDTO;
import com.f8.turnera.security.domain.services.IUserService;

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
public class UserController {

    @Autowired
    private IUserService service;

    @GetMapping("/users/findAllByFilter")
    @PreAuthorize("hasAuthority('users.read')")
    public ResponseEntity<ResponseDTO> findAllByFilter(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            UserFilterDTO request) throws Exception {
        return new ResponseEntity<>(service.findAllByFilter(token, request), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('users.read')")
    public ResponseEntity<ResponseDTO> getUser(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) throws Exception {
        return new ResponseEntity<>(service.findById(token, id), HttpStatus.OK);
    }

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('users.write')")
    public ResponseEntity<ResponseDTO> createUser(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody UserDTO request) throws Exception {
        return new ResponseEntity<>(service.create(token, request), HttpStatus.OK);
    }

    @PutMapping("/users")
    @PreAuthorize("hasAuthority('users.write')")
    public ResponseEntity<ResponseDTO> updateUser(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody UserDTO request) throws Exception {
        return new ResponseEntity<>(service.update(token, request), HttpStatus.OK);
    }

    @DeleteMapping("users/{id}")
    @PreAuthorize("hasAuthority('users.delete')")
    public ResponseEntity<ResponseDTO> deleteUser(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) throws Exception {
        return new ResponseEntity<>(service.deleteById(token, id), HttpStatus.OK);
    }

}
