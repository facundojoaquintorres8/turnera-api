package com.f8.turnera.controllers;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.domain.dtos.CustomerDTO;
import com.f8.turnera.domain.dtos.CustomerFilterDTO;
import com.f8.turnera.domain.dtos.ResponseDTO;
import com.f8.turnera.domain.services.ICustomerService;

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
public class CustomerController {

    @Autowired
    private ICustomerService service;

    @GetMapping("/customers/findAllByFilter")
    @PreAuthorize("hasAuthority('customers.read')")
    public ResponseEntity<ResponseDTO> findAllByFilter(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            CustomerFilterDTO request) throws Exception {
        return new ResponseEntity<>(service.findAllByFilter(token, request), HttpStatus.OK);
    }

    @GetMapping("/customers/{id}")
    @PreAuthorize("hasAuthority('customers.read')")
    public ResponseEntity<ResponseDTO> getCustomer(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) throws Exception {
        return new ResponseEntity<>(service.findById(token, id), HttpStatus.OK);
    }

    @PostMapping("/customers")
    @PreAuthorize("hasAuthority('customers.write')")
    public ResponseEntity<ResponseDTO> createCustomer(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody CustomerDTO request) throws Exception {
        return new ResponseEntity<>(service.create(token, request), HttpStatus.OK);
    }

    @PutMapping("/customers")
    @PreAuthorize("hasAuthority('customers.write')")
    public ResponseEntity<ResponseDTO> updateCustomer(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody CustomerDTO request) throws Exception {
        return new ResponseEntity<>(service.update(token, request), HttpStatus.OK);
    }

    @DeleteMapping("customers/{id}")
    @PreAuthorize("hasAuthority('customers.delete')")
    public ResponseEntity<ResponseDTO> deleteCustomer(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) throws Exception {
        return new ResponseEntity<>(service.deleteById(token, id), HttpStatus.OK);
    }
}
