package com.f8.turnera.domain.services;


import com.f8.turnera.domain.dtos.CustomerDTO;
import com.f8.turnera.domain.dtos.CustomerFilterDTO;
import com.f8.turnera.domain.dtos.OrganizationDTO;
import com.f8.turnera.domain.dtos.ResponseDTO;

public interface ICustomerService {

    ResponseDTO findAllByFilter(String token, CustomerFilterDTO filter) throws Exception;

    ResponseDTO findById(String token, Long id) throws Exception;

    ResponseDTO create(String token, CustomerDTO customerDTO) throws Exception;

    ResponseDTO createQuick(CustomerDTO customerDTO, OrganizationDTO organizationDTO) throws Exception;

    ResponseDTO update(String token, CustomerDTO customerDTO) throws Exception;

    ResponseDTO deleteById(String token, Long id) throws Exception;
}
