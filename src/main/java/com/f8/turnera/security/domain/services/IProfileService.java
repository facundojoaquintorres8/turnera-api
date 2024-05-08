package com.f8.turnera.security.domain.services;

import com.f8.turnera.security.domain.dtos.ProfileDTO;
import com.f8.turnera.security.domain.dtos.ProfileFilterDTO;
import com.f8.turnera.security.domain.dtos.ResponseDTO;

public interface IProfileService {

    ResponseDTO findAllByFilter(String token, ProfileFilterDTO filter) throws Exception;

    ResponseDTO findById(String token, Long id) throws Exception;

    ResponseDTO create(String token, ProfileDTO profileDTO) throws Exception;

    ResponseDTO update(String token, ProfileDTO profileDTO) throws Exception;

    ResponseDTO deleteById(String token, Long id) throws Exception;
}
