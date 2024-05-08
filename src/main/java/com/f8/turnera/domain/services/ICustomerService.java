package com.f8.turnera.domain.services;

import org.springframework.data.domain.Page;

import com.f8.turnera.domain.dtos.CustomerDTO;
import com.f8.turnera.domain.dtos.CustomerFilterDTO;
import com.f8.turnera.domain.dtos.OrganizationDTO;

public interface ICustomerService {

    Page<CustomerDTO> findAllByFilter(String token, CustomerFilterDTO filter) throws Exception;

    CustomerDTO findById(String token, Long id) throws Exception;

    CustomerDTO create(String token, CustomerDTO customerDTO) throws Exception;

    CustomerDTO createQuick(CustomerDTO customerDTO, OrganizationDTO organizationDTO) throws Exception;

    CustomerDTO update(String token, CustomerDTO customerDTO) throws Exception;

    void deleteById(String token, Long id) throws Exception;
}
