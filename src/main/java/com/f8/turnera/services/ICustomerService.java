package com.f8.turnera.services;

import java.util.List;

import com.f8.turnera.entities.Organization;
import com.f8.turnera.models.CustomerDTO;
import com.f8.turnera.models.CustomerFilterDTO;

public interface ICustomerService {

    public List<CustomerDTO> findAllByFilter(CustomerFilterDTO filter);

    public CustomerDTO findById(Long id);

    public CustomerDTO create(CustomerDTO customerDTO);

    public CustomerDTO createQuick(CustomerDTO customerDTO, Organization organization);

    public CustomerDTO update(CustomerDTO customerDTO);

    public void deleteById(Long id);
}
