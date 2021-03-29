package com.f8.turnera.security.services;

import com.f8.turnera.security.models.ProfileDTO;
import com.f8.turnera.security.models.ProfileFilterDTO;

import org.springframework.data.domain.Page;

public interface IProfileService {

    public Page<ProfileDTO> findAllByFilter(ProfileFilterDTO filter);

    public ProfileDTO findById(Long id);

    public ProfileDTO create(ProfileDTO profileDTO);

    public ProfileDTO update(ProfileDTO profileDTO);

    public void deleteById(Long id);
}
