package com.f8.turnera.services;

import com.f8.turnera.entities.Organization;
import com.f8.turnera.models.CustomerDTO;
import com.f8.turnera.models.CustomerFilterDTO;

import org.springframework.data.domain.Page;

public interface ICustomerService {

    public Page<CustomerDTO> findAllByFilter(CustomerFilterDTO filter);

    public CustomerDTO findById(Long id);

    public CustomerDTO create(CustomerDTO customerDTO);

    public CustomerDTO createQuick(CustomerDTO customerDTO, Organization organization);

    public CustomerDTO update(CustomerDTO customerDTO);

    public void deleteById(Long id);
}
