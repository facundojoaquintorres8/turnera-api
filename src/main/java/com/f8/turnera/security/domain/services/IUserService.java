package com.f8.turnera.security.domain.services;

import org.springframework.data.domain.Page;

import com.f8.turnera.security.domain.dtos.UserDTO;
import com.f8.turnera.security.domain.dtos.UserFilterDTO;

public interface IUserService {

    Page<UserDTO> findAllByFilter(String token, UserFilterDTO filter) throws Exception;

    UserDTO findById(String token, Long id) throws Exception;

    UserDTO create(String token, UserDTO userDTO) throws Exception;

    UserDTO update(String token, UserDTO userDTO) throws Exception;

    void deleteById(String token, Long id) throws Exception;
}
