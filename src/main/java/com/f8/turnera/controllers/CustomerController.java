package com.f8.turnera.controllers;

import java.util.List;

import com.f8.turnera.models.CustomerDTO;
import com.f8.turnera.models.CustomerFilterDTO;
import com.f8.turnera.services.ICustomerService;

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
public class CustomerController {
    private final Logger log = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private ICustomerService service;

    @GetMapping("/customers/findAllByFilter")
    @PreAuthorize("hasAuthority('customers.read')")
    public ResponseEntity<List<CustomerDTO>> findAllByFilter(CustomerFilterDTO filter) {
        log.info("REST request to get Customers by filter: {}", filter);

        List<CustomerDTO> result = service.findAllByFilter(filter);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/customers/{id}")
    @PreAuthorize("hasAuthority('customers.read')")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
        log.info("REST request to get Customer {}", id);

        CustomerDTO result = service.findById(id);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/customers")
    @PreAuthorize("hasAuthority('customers.write')")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        log.info("REST request to create Customer: {}", customerDTO);

        CustomerDTO result = service.create(customerDTO);

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/customers")
    @PreAuthorize("hasAuthority('customers.write')")
    public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerDTO customerDTO) {
        log.info("REST request to update Customer: {}", customerDTO);

        CustomerDTO result = service.update(customerDTO);

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("customers/{id}")
    @PreAuthorize("hasAuthority('customers.delete')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        log.info("REST request to delete Customer {}", id);

        service.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
