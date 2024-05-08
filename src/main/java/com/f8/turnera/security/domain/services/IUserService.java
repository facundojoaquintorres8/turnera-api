package com.f8.turnera.security.domain.services;

import com.f8.turnera.security.domain.dtos.ResponseDTO;
import com.f8.turnera.security.domain.dtos.UserDTO;
import com.f8.turnera.security.domain.dtos.UserFilterDTO;

public interface IUserService {

    ResponseDTO findAllByFilter(String token, UserFilterDTO filter) throws Exception;

    ResponseDTO findById(String token, Long id) throws Exception;

    ResponseDTO create(String token, UserDTO userDTO) throws Exception;

    ResponseDTO update(String token, UserDTO userDTO) throws Exception;

    ResponseDTO deleteById(String token, Long id) throws Exception;
}
