package com.f8.turnera.domain.services;

import com.f8.turnera.domain.dtos.AppointmentChangeStatusDTO;
import com.f8.turnera.domain.dtos.AppointmentDTO;
import com.f8.turnera.domain.dtos.AppointmentSaveDTO;

public interface IAppointmentService {

    AppointmentDTO book(String token, AppointmentSaveDTO appointmentSaveDTO) throws Exception;

    AppointmentDTO absent(String token, AppointmentChangeStatusDTO appointmentChangeStatusDTO) throws Exception;

    AppointmentDTO cancel(String token, AppointmentChangeStatusDTO appointmentChangeStatusDTO) throws Exception;

    AppointmentDTO attend(String token, AppointmentChangeStatusDTO appointmentChangeStatusDTO) throws Exception;

    AppointmentDTO finalize(String token, AppointmentChangeStatusDTO appointmentChangeStatusDTO) throws Exception;
}
