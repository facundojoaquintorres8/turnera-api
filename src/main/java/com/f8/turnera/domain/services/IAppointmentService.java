package com.f8.turnera.domain.services;

import com.f8.turnera.domain.dtos.AppointmentChangeStatusDTO;
import com.f8.turnera.domain.dtos.AppointmentDTO;
import com.f8.turnera.domain.dtos.AppointmentSaveDTO;

public interface IAppointmentService {

    public AppointmentDTO book(AppointmentSaveDTO appointmentSaveDTO);

    public AppointmentDTO absent(AppointmentChangeStatusDTO appointmentChangeStatusDTO);

    public AppointmentDTO cancel(AppointmentChangeStatusDTO appointmentChangeStatusDTO);

    public AppointmentDTO attend(AppointmentChangeStatusDTO appointmentChangeStatusDTO);

    public AppointmentDTO finalize(AppointmentChangeStatusDTO appointmentChangeStatusDTO);
}
