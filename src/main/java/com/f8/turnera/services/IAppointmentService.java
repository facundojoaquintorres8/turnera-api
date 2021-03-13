package com.f8.turnera.services;

import com.f8.turnera.models.AppointmentDTO;
import com.f8.turnera.models.AppointmentSaveDTO;

public interface IAppointmentService {

    public AppointmentDTO book(AppointmentSaveDTO appointmentSaveDTO);

    public AppointmentDTO absent(Long id);

    public AppointmentDTO cancel(Long id);

    public AppointmentDTO attend(Long id);

    public AppointmentDTO finalize(Long id);
}
