package com.f8.turnera.domain.services;

import org.springframework.data.domain.Page;

import com.f8.turnera.domain.dtos.AgendaDTO;
import com.f8.turnera.domain.dtos.AgendaSaveDTO;
import com.f8.turnera.domain.dtos.AppointmentFilterDTO;

public interface IAgendaService {

    Page<AgendaDTO> findAllByFilter(String token, AppointmentFilterDTO filter) throws Exception;

    AgendaDTO findById(String token, Long id) throws Exception;

    Boolean create(String token, AgendaSaveDTO agendaDTO) throws Exception;

    AgendaDTO update(String token, AgendaDTO agendaDTO) throws Exception;

    void deleteById(String token, Long id) throws Exception;

    AgendaDTO desactivate(String token, Long id) throws Exception;
}
