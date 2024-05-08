package com.f8.turnera.security.domain.services;

import org.springframework.data.domain.Page;

import com.f8.turnera.security.domain.dtos.ProfileDTO;
import com.f8.turnera.security.domain.dtos.ProfileFilterDTO;

public interface IProfileService {

    Page<ProfileDTO> findAllByFilter(String token, ProfileFilterDTO filter) throws Exception;

    ProfileDTO findById(String token, Long id) throws Exception;

    ProfileDTO create(String token, ProfileDTO profileDTO) throws Exception;

    ProfileDTO update(String token, ProfileDTO profileDTO) throws Exception;

    void deleteById(String token, Long id) throws Exception;
}
