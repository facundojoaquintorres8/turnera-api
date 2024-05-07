package com.f8.turnera.security.domain.services;

import org.springframework.data.domain.Page;

import com.f8.turnera.security.domain.dtos.UserDTO;
import com.f8.turnera.security.domain.dtos.UserFilterDTO;

public interface IUserService {

    public Page<UserDTO> findAllByFilter(UserFilterDTO filter);

    public UserDTO findById(Long id);

    public UserDTO create(UserDTO userDTO);

    public UserDTO update(UserDTO userDTO);

    public void deleteById(Long id);
}
