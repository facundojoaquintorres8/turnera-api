package com.f8.turnera.domain.services;

import com.f8.turnera.domain.entities.Appointment;
import com.f8.turnera.security.domain.entities.User;

public interface IEmailService {
	// TODO: transaccionar en caso de que falle el envío de email
	void sendOrganizationActivationEmail(User user) throws Exception;

	void sendAccountActivationEmail(User user) throws Exception;

	void sendPasswordResetRequestEmail(User user) throws Exception;

	void sendBookedAppointmentEmail(Appointment appointment) throws Exception;

	void sendCanceledAppointmentEmail(Appointment appointment) throws Exception;

	void sendFinalizeAppointmentEmail(Appointment appointment) throws Exception;
}
