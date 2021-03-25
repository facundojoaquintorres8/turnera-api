package com.f8.turnera.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.format.DateTimeFormatter;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.f8.turnera.entities.Appointment;
import com.f8.turnera.entities.Organization;
import com.f8.turnera.security.entities.User;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService implements IEmailService {

	final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

	@Value("${baseUrl}")
	private String baseUrl;

	@Autowired
	private JavaMailSender sender;

	@Autowired
	private TemplateEngine templateEngine;

	private void sendEmail(String emailContent, String subject, Organization organization, String to) {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setFrom("Turnera <" + organization.getDefaultEmail() +  ">");
			helper.setTo(to);
			helper.setText(emailContent, true);
			helper.setSubject(subject);
			sender.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void sendOrganizationActivationEmail(User user) {
		Context context = new Context();
		context.setVariable("firstName", user.getFirstName());
		context.setVariable("link", baseUrl + "account/" + user.getActivationKey() + "/activate");
		String emailContent = templateEngine.process("organizationActivation", context);
		sendEmail(emailContent, "Turnera - Activación de Cuenta y Organización", user.getOrganization(), user.getUsername());
	}

	@Override
	public void sendAccountActivationEmail(User user) {
		Context context = new Context();
		context.setVariable("firstName", user.getFirstName());
		context.setVariable("organization", user.getOrganization().getBusinessName());
		context.setVariable("link", baseUrl + "account/" + user.getActivationKey() + "/activate");
		String emailContent = templateEngine.process("accountActivation", context);
		sendEmail(emailContent, "Turnera - Activación de Cuenta", user.getOrganization(), user.getUsername());
	}

	@Override
	public void sendPasswordResetRequestEmail(User user) {
		Context context = new Context();
		context.setVariable("firstName", user.getFirstName());
		context.setVariable("link", baseUrl + "account/" + user.getResetKey() + "/password-reset");
		String emailContent = templateEngine.process("passwordResetRequest", context);
		sendEmail(emailContent, "Turnera - Restrablecer Contraseña", user.getOrganization(), user.getUsername());
	}

	@Override
	public void sendBookedAppointmentEmail(Appointment appointment) {
		Context context = new Context();
		context.setVariable("businessName", appointment.getCustomer().getBusinessName());
		context.setVariable("resource", appointment.getAgenda().getResource().getDescription());
		context.setVariable("fromDate", appointment.getAgenda().getStartDate().format(formatter));
		context.setVariable("toDate", appointment.getAgenda().getEndDate().format(formatter));
		String emailContent = templateEngine.process("confirmedAppointment", context);
		sendEmail(emailContent, "Turnera - Turno Confirmado", appointment.getOrganization(), appointment.getCustomer().getEmail());		
	}

	@Override
	public void sendCanceledAppointmentEmail(Appointment appointment) {
		Context context = new Context();
		context.setVariable("businessName", appointment.getCustomer().getBusinessName());
		context.setVariable("resource", appointment.getAgenda().getResource().getDescription());
		context.setVariable("fromDate", appointment.getAgenda().getStartDate().format(formatter));
		context.setVariable("toDate", appointment.getAgenda().getEndDate().format(formatter));
		String emailContent = templateEngine.process("canceledAppointment", context);
		sendEmail(emailContent, "Turnera - Turno Cancelado", appointment.getOrganization(), appointment.getCustomer().getEmail());	
	}

	@Override
	public void sendFinalizeAppointmentEmail(Appointment appointment) {
		Context context = new Context();
		context.setVariable("businessName", appointment.getCustomer().getBusinessName());
		context.setVariable("resource", appointment.getAgenda().getResource().getDescription());
		context.setVariable("fromDate", appointment.getAgenda().getStartDate().format(formatter));
		context.setVariable("toDate", appointment.getAgenda().getEndDate().format(formatter));
		String emailContent = templateEngine.process("finalizeAppointment", context);
		sendEmail(emailContent, "Turnera - Turno Finalizado", appointment.getOrganization(), appointment.getCustomer().getEmail());	
	}

}