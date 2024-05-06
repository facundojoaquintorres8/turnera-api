package com.f8.turnera.controllers;

import com.f8.turnera.models.CustomerDTO;
import com.f8.turnera.models.CustomerFilterDTO;
import com.f8.turnera.services.ICustomerService;

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
public class CustomerController {

    @Autowired
    private ICustomerService service;

    @GetMapping("/customers/findAllByFilter")
    @PreAuthorize("hasAuthority('customers.read')")
    public ResponseEntity<Page<CustomerDTO>> findAllByFilter(CustomerFilterDTO filter) {
        Page<CustomerDTO> result = service.findAllByFilter(filter);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/customers/{id}")
    @PreAuthorize("hasAuthority('customers.read')")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
        CustomerDTO result = service.findById(id);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/customers")
    @PreAuthorize("hasAuthority('customers.write')")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO result = service.create(customerDTO);

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/customers")
    @PreAuthorize("hasAuthority('customers.write')")
    public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO result = service.update(customerDTO);

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("customers/{id}")
    @PreAuthorize("hasAuthority('customers.delete')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        service.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
