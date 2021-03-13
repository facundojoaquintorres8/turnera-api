package com.f8.turnera.services;

import java.util.List;

import com.f8.turnera.models.AgendaDTO;
import com.f8.turnera.models.AgendaSaveDTO;
import com.f8.turnera.models.AppointmentFilterDTO;

public interface IAgendaService {

    public List<AgendaDTO> findAllByFilter(AppointmentFilterDTO filter);

    public AgendaDTO findById(Long id);

    public List<AgendaDTO> create(AgendaSaveDTO agendaDTO);

    public void deleteById(Long id);
}
