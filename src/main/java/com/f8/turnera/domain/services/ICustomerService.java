package com.f8.turnera.domain.services;

import org.springframework.data.domain.Page;

import com.f8.turnera.domain.dtos.CustomerDTO;
import com.f8.turnera.domain.dtos.CustomerFilterDTO;
import com.f8.turnera.domain.dtos.OrganizationDTO;

public interface ICustomerService {

    public Page<CustomerDTO> findAllByFilter(String token, CustomerFilterDTO filter);

    public CustomerDTO findById(String token, Long id);

    public CustomerDTO create(String token, CustomerDTO customerDTO);

    public CustomerDTO createQuick(CustomerDTO customerDTO, OrganizationDTO organizationDTO);

    public CustomerDTO update(String token, CustomerDTO customerDTO);

    public void deleteById(String token, Long id);
}
