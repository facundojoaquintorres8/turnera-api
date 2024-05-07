package com.f8.turnera.security.domain.services;

import org.springframework.data.domain.Page;

import com.f8.turnera.security.domain.dtos.ProfileDTO;
import com.f8.turnera.security.domain.dtos.ProfileFilterDTO;

public interface IProfileService {

    public Page<ProfileDTO> findAllByFilter(ProfileFilterDTO filter);

    public ProfileDTO findById(Long id);

    public ProfileDTO create(ProfileDTO profileDTO);

    public ProfileDTO update(ProfileDTO profileDTO);

    public void deleteById(Long id);
}
