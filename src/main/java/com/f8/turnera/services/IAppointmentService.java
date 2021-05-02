package com.f8.turnera.services;

import com.f8.turnera.models.AppointmentChangeStatusDTO;
import com.f8.turnera.models.AppointmentDTO;
import com.f8.turnera.models.AppointmentSaveDTO;

public interface IAppointmentService {

    public AppointmentDTO book(AppointmentSaveDTO appointmentSaveDTO);

    public AppointmentDTO absent(AppointmentChangeStatusDTO appointmentChangeStatusDTO);

    public AppointmentDTO cancel(AppointmentChangeStatusDTO appointmentChangeStatusDTO);

    public AppointmentDTO attend(AppointmentChangeStatusDTO appointmentChangeStatusDTO);

    public AppointmentDTO finalize(AppointmentChangeStatusDTO appointmentChangeStatusDTO);
}
