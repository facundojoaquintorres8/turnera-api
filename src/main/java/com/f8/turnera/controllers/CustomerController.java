package com.f8.turnera.controllers;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.domain.dtos.CustomerDTO;
import com.f8.turnera.domain.dtos.CustomerFilterDTO;
import com.f8.turnera.domain.services.ICustomerService;

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
public class CustomerController {

    @Autowired
    private ICustomerService service;

    @GetMapping("/customers/findAllByFilter")
    @PreAuthorize("hasAuthority('customers.read')")
    public ResponseEntity<Page<CustomerDTO>> findAllByFilter(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            CustomerFilterDTO filter) throws Exception {
        Page<CustomerDTO> result = service.findAllByFilter(token, filter);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/customers/{id}")
    @PreAuthorize("hasAuthority('customers.read')")
    public ResponseEntity<CustomerDTO> getCustomer(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) throws Exception {
        CustomerDTO result = service.findById(token, id);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/customers")
    @PreAuthorize("hasAuthority('customers.write')")
    public ResponseEntity<CustomerDTO> createCustomer(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody CustomerDTO customerDTO) throws Exception {
        CustomerDTO result = service.create(token, customerDTO);

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/customers")
    @PreAuthorize("hasAuthority('customers.write')")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody CustomerDTO customerDTO) throws Exception {
        CustomerDTO result = service.update(token, customerDTO);

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("customers/{id}")
    @PreAuthorize("hasAuthority('customers.delete')")
    public ResponseEntity<Void> deleteCustomer(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) throws Exception {
        service.deleteById(token, id);

        return ResponseEntity.ok().build();
    }
}
