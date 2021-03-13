package com.f8.turnera.repositories;

import java.util.List;

import com.f8.turnera.entities.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByOrganizationId(Long onganizationId);
}