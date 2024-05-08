package com.f8.turnera.security.domain.services;

import org.springframework.data.domain.Page;

import com.f8.turnera.security.domain.dtos.UserDTO;
import com.f8.turnera.security.domain.dtos.UserFilterDTO;

public interface IUserService {

    public Page<UserDTO> findAllByFilter(String token, UserFilterDTO filter);

    public UserDTO findById(String token, Long id);

    public UserDTO create(String token, UserDTO userDTO);

    public UserDTO update(String token, UserDTO userDTO);

    public void deleteById(String token, Long id);
}
