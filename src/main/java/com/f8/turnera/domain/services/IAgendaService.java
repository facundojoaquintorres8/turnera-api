package com.f8.turnera.domain.services;

import org.springframework.data.domain.Page;

import com.f8.turnera.domain.dtos.AgendaDTO;
import com.f8.turnera.domain.dtos.AgendaSaveDTO;
import com.f8.turnera.domain.dtos.AppointmentFilterDTO;

public interface IAgendaService {

    public Page<AgendaDTO> findAllByFilter(String token, AppointmentFilterDTO filter);

    public AgendaDTO findById(String token, Long id);

    public Boolean create(String token, AgendaSaveDTO agendaDTO);

    public AgendaDTO update(String token, AgendaDTO agendaDTO);

    public void deleteById(String token, Long id);

    public AgendaDTO desactivate(String token, Long id);
}
