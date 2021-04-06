package com.f8.turnera.security.services;

import com.f8.turnera.security.models.UserDTO;
import com.f8.turnera.security.models.UserFilterDTO;

import org.springframework.data.domain.Page;

public interface IUserService {

    public Page<UserDTO> findAllByFilter(UserFilterDTO filter);

    public UserDTO findById(Long id);

    public UserDTO create(UserDTO userDTO);

    public UserDTO update(UserDTO userDTO);

    public void deleteById(Long id);
}
