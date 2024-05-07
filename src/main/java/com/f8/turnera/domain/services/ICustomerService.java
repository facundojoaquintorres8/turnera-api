package com.f8.turnera.domain.services;

import org.springframework.data.domain.Page;

import com.f8.turnera.domain.dtos.CustomerDTO;
import com.f8.turnera.domain.dtos.CustomerFilterDTO;
import com.f8.turnera.domain.dtos.OrganizationDTO;

public interface ICustomerService {

    public Page<CustomerDTO> findAllByFilter(CustomerFilterDTO filter);

    public CustomerDTO findById(Long id);

    public CustomerDTO create(CustomerDTO customerDTO);

    public CustomerDTO createQuick(CustomerDTO customerDTO, OrganizationDTO organizationDTO);

    public CustomerDTO update(CustomerDTO customerDTO);

    public void deleteById(Long id);
}
