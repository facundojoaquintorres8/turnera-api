package com.f8.turnera.domain.services;

import org.springframework.data.domain.Page;

import com.f8.turnera.domain.dtos.AgendaDTO;
import com.f8.turnera.domain.dtos.AgendaSaveDTO;
import com.f8.turnera.domain.dtos.AppointmentFilterDTO;

public interface IAgendaService {

    public Page<AgendaDTO> findAllByFilter(AppointmentFilterDTO filter);

    public AgendaDTO findById(Long id);

    public Boolean create(AgendaSaveDTO agendaDTO);

    public AgendaDTO update(AgendaDTO agendaDTO);

    public void deleteById(Long id);

    public AgendaDTO desactivate(Long id);
}
