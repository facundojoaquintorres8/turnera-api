package com.f8.turnera.security.domain.services;

import org.springframework.data.domain.Page;

import com.f8.turnera.security.domain.dtos.ProfileDTO;
import com.f8.turnera.security.domain.dtos.ProfileFilterDTO;

public interface IProfileService {

    public Page<ProfileDTO> findAllByFilter(String token, ProfileFilterDTO filter);

    public ProfileDTO findById(String token, Long id);

    public ProfileDTO create(String token, ProfileDTO profileDTO);

    public ProfileDTO update(String token, ProfileDTO profileDTO);

    public void deleteById(String token, Long id);
}
