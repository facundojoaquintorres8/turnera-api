package com.f8.turnera.domain.services.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.config.TokenUtil;
import com.f8.turnera.domain.dtos.AgendaDTO;
import com.f8.turnera.domain.dtos.AppointmentChangeStatusDTO;
import com.f8.turnera.domain.dtos.AppointmentDTO;
import com.f8.turnera.domain.dtos.AppointmentSaveDTO;
import com.f8.turnera.domain.dtos.AppointmentStatusEnum;
import com.f8.turnera.domain.dtos.OrganizationDTO;
import com.f8.turnera.domain.entities.Agenda;
import com.f8.turnera.domain.entities.Appointment;
import com.f8.turnera.domain.entities.Customer;
import com.f8.turnera.domain.entities.Organization;
import com.f8.turnera.domain.repositories.IAppointmentRepository;
import com.f8.turnera.domain.services.IAgendaService;
import com.f8.turnera.domain.services.IAppointmentService;
import com.f8.turnera.domain.services.ICustomerService;
import com.f8.turnera.domain.services.IEmailService;
import com.f8.turnera.domain.services.IOrganizationService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService implements IAppointmentService {

    @Autowired
    private IAppointmentRepository appointmentRepository;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private IAgendaService agendaService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IEmailService emailService;

    @Override
    public AppointmentDTO book(String token, AppointmentSaveDTO appointmentSaveDTO) {
        OrganizationDTO organization = organizationService.findById(token);
        AgendaDTO agenda = agendaService.findById(token, appointmentSaveDTO.getAgenda().getId());

        ModelMapper modelMapper = new ModelMapper();

        Customer customer = null;
        if (appointmentSaveDTO.getCustomer().getId() != null) {
            customer = modelMapper.map(appointmentSaveDTO.getCustomer(), Customer.class);
        } else {
            customer = modelMapper.map(
                    customerService.createQuick(appointmentSaveDTO.getCustomer(), organization), Customer.class);
        }

        Appointment appointment = new Appointment(LocalDateTime.now(),
                modelMapper.map(organization, Organization.class),
                modelMapper.map(agenda, Agenda.class), customer);
        appointment.addStatus(AppointmentStatusEnum.BOOKED, null);

        try {
            appointmentRepository.save(appointment);
            AppointmentDTO appointmentDTO = modelMapper.map(appointment, AppointmentDTO.class);
            agenda.setLastAppointment(appointmentDTO);
            agendaService.update(token, agenda);
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }

        emailService.sendBookedAppointmentEmail(appointment);

        return modelMapper.map(appointment, AppointmentDTO.class);
    }

    @Override
    public AppointmentDTO absent(String token, AppointmentChangeStatusDTO appointmentChangeStatusDTO) {
        Long orgId = Long.parseLong(TokenUtil.getClaimByToken(token, SecurityConstants.ORGANIZATION_KEY).toString());
        Optional<Appointment> appointment = appointmentRepository.findByIdAndOrganizationId(appointmentChangeStatusDTO.getId(), orgId);
        if (!appointment.isPresent()) {
            throw new RuntimeException("Turno no encontrado - " + appointmentChangeStatusDTO.getId());
        }

        appointment.get().addStatus(AppointmentStatusEnum.ABSENT, appointmentChangeStatusDTO.getObservations());

        try {
            appointmentRepository.save(appointment.get());
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(appointment.get(), AppointmentDTO.class);
    }

    @Override
    public AppointmentDTO cancel(String token, AppointmentChangeStatusDTO appointmentChangeStatusDTO) {
        Long orgId = Long.parseLong(TokenUtil.getClaimByToken(token, SecurityConstants.ORGANIZATION_KEY).toString());
        Optional<Appointment> appointment = appointmentRepository.findByIdAndOrganizationId(appointmentChangeStatusDTO.getId(), orgId);
        if (!appointment.isPresent()) {
            throw new RuntimeException("Turno no encontrado - " + appointmentChangeStatusDTO.getId());
        }

        appointment.get().addStatus(AppointmentStatusEnum.CANCELLED, appointmentChangeStatusDTO.getObservations());

        emailService.sendCanceledAppointmentEmail(appointment.get());

        try {
            appointmentRepository.save(appointment.get());
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(appointment.get(), AppointmentDTO.class);
    }

    @Override
    public AppointmentDTO attend(String token, AppointmentChangeStatusDTO appointmentChangeStatusDTO) {
        Long orgId = Long.parseLong(TokenUtil.getClaimByToken(token, SecurityConstants.ORGANIZATION_KEY).toString());
        Optional<Appointment> appointment = appointmentRepository.findByIdAndOrganizationId(appointmentChangeStatusDTO.getId(), orgId);
        if (!appointment.isPresent()) {
            throw new RuntimeException("Turno no encontrado - " + appointmentChangeStatusDTO.getId());
        }

        appointment.get().addStatus(AppointmentStatusEnum.IN_ATTENTION, appointmentChangeStatusDTO.getObservations());

        try {
            appointmentRepository.save(appointment.get());
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(appointment.get(), AppointmentDTO.class);
    }

    @Override
    public AppointmentDTO finalize(String token, AppointmentChangeStatusDTO appointmentChangeStatusDTO) {
        Long orgId = Long.parseLong(TokenUtil.getClaimByToken(token, SecurityConstants.ORGANIZATION_KEY).toString());
        Optional<Appointment> appointment = appointmentRepository.findByIdAndOrganizationId(appointmentChangeStatusDTO.getId(), orgId);
        if (!appointment.isPresent()) {
            throw new RuntimeException("Turno no encontrado - " + appointmentChangeStatusDTO.getId());
        }

        appointment.get().addStatus(AppointmentStatusEnum.FINALIZED, appointmentChangeStatusDTO.getObservations());

        emailService.sendFinalizeAppointmentEmail(appointment.get());

        try {
            appointmentRepository.save(appointment.get());
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(appointment.get(), AppointmentDTO.class);
    }
}
