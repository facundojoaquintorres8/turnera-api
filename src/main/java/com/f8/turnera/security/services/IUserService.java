package com.f8.turnera.security.services;

import java.util.List;

import com.f8.turnera.security.models.UserDTO;
import com.f8.turnera.security.models.UserFilterDTO;

public interface IUserService {

    public List<UserDTO> findAllByFilter(UserFilterDTO filter);

    public UserDTO findById(Long id);

    public UserDTO create(UserDTO userDTO);

    public UserDTO update(UserDTO userDTO);

    public void deleteById(Long id);
}
