package com.f8.turnera.security.services;

import java.util.List;

import com.f8.turnera.security.models.ProfileDTO;
import com.f8.turnera.security.models.ProfileFilterDTO;

public interface IProfileService {

    public List<ProfileDTO> findAllByFilter(ProfileFilterDTO filter);

    public ProfileDTO findById(Long id);

    public ProfileDTO create(ProfileDTO profileDTO);

    public ProfileDTO update(ProfileDTO profileDTO);

    public void deleteById(Long id);
}
