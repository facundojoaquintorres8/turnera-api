package com.f8.turnera.services;

import com.f8.turnera.models.AgendaDTO;
import com.f8.turnera.models.AgendaSaveDTO;
import com.f8.turnera.models.AppointmentFilterDTO;

import org.springframework.data.domain.Page;

public interface IAgendaService {

    public Page<AgendaDTO> findAllByFilter(AppointmentFilterDTO filter);

    public AgendaDTO findById(Long id);

    public Boolean create(AgendaSaveDTO agendaDTO);

    public void deleteById(Long id);

    public AgendaDTO desactivate(Long id);
}
