package com.f8.turnera.domain.services;

import com.f8.turnera.domain.entities.Appointment;
import com.f8.turnera.security.domain.entities.User;

public interface IEmailService {
	public void sendOrganizationActivationEmail(User user);
	public void sendAccountActivationEmail(User user);
	public void sendPasswordResetRequestEmail(User user);

	public void sendBookedAppointmentEmail(Appointment appointment);
	public void sendCanceledAppointmentEmail(Appointment appointment);
	public void sendFinalizeAppointmentEmail(Appointment appointment);
}
