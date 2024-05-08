package com.f8.turnera.domain.services;

import com.f8.turnera.domain.dtos.AppointmentChangeStatusDTO;
import com.f8.turnera.domain.dtos.AppointmentDTO;
import com.f8.turnera.domain.dtos.AppointmentSaveDTO;

public interface IAppointmentService {

    public AppointmentDTO book(String token, AppointmentSaveDTO appointmentSaveDTO);

    public AppointmentDTO absent(String token, AppointmentChangeStatusDTO appointmentChangeStatusDTO);

    public AppointmentDTO cancel(String token, AppointmentChangeStatusDTO appointmentChangeStatusDTO);

    public AppointmentDTO attend(String token, AppointmentChangeStatusDTO appointmentChangeStatusDTO);

    public AppointmentDTO finalize(String token, AppointmentChangeStatusDTO appointmentChangeStatusDTO);
}
