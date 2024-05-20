package com.f8.turnera.domain.services;


import com.f8.turnera.domain.dtos.CustomerDTO;
import com.f8.turnera.domain.dtos.CustomerFilterDTO;
import com.f8.turnera.domain.dtos.ResponseDTO;
import com.f8.turnera.domain.entities.Customer;
import com.f8.turnera.domain.entities.Organization;

public interface ICustomerService {

    ResponseDTO findAllByFilter(String token, CustomerFilterDTO filter) throws Exception;

    ResponseDTO findById(String token, Long id) throws Exception;

    ResponseDTO create(String token, CustomerDTO customerDTO) throws Exception;

    Customer createQuick(CustomerDTO customerDTO, Organization organization) throws Exception;

    ResponseDTO update(String token, CustomerDTO customerDTO) throws Exception;

    ResponseDTO deleteById(String token, Long id) throws Exception;
}
