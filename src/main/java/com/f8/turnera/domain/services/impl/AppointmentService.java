package com.f8.turnera.domain.services.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import com.f8.turnera.domain.dtos.AgendaDTO;
import com.f8.turnera.domain.dtos.AppointmentChangeStatusDTO;
import com.f8.turnera.domain.dtos.AppointmentDTO;
import com.f8.turnera.domain.dtos.AppointmentSaveDTO;
import com.f8.turnera.domain.dtos.AppointmentStatusEnum;
import com.f8.turnera.domain.dtos.OrganizationDTO;
import com.f8.turnera.domain.dtos.ResponseDTO;
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
import com.f8.turnera.exception.NoContentException;
import com.f8.turnera.util.MapperHelper;
import com.f8.turnera.util.OrganizationHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseDTO book(String token, AppointmentSaveDTO appointmentSaveDTO) throws Exception {
        OrganizationDTO organizationDTO = (OrganizationDTO) organizationService.findById(token).getData();
        Organization organization = MapperHelper.modelMapper().map(organizationDTO, Organization.class);
        AgendaDTO agenda = (AgendaDTO) agendaService.findById(token, appointmentSaveDTO.getAgenda().getId()).getData();

        Customer customer = null;
        if (appointmentSaveDTO.getCustomer().getId() != null) {
            customer = MapperHelper.modelMapper().map(appointmentSaveDTO.getCustomer(), Customer.class);
        } else {
            customer = customerService.createQuick(appointmentSaveDTO.getCustomer(), organization);
        }

        Appointment appointment = new Appointment(LocalDateTime.now(), organization,
                MapperHelper.modelMapper().map(agenda, Agenda.class), customer);
        appointment.addStatus(AppointmentStatusEnum.BOOKED, null);

        appointmentRepository.save(appointment);
        agenda.setLastAppointment(MapperHelper.modelMapper().map(appointment, AppointmentDTO.class));
        agendaService.setLastAppointment(token, agenda);

        emailService.sendBookedAppointmentEmail(appointment);

        return new ResponseDTO(HttpStatus.OK.value(), MapperHelper.modelMapper().map(appointment, AppointmentDTO.class));
    }

    @Override
    public ResponseDTO absent(String token, AppointmentChangeStatusDTO appointmentChangeStatusDTO) throws Exception {
        Optional<Appointment> appointment = appointmentRepository.findByIdAndOrganizationId(appointmentChangeStatusDTO.getId(), OrganizationHelper.getOrganizationId(token));
        if (!appointment.isPresent()) {
            throw new NoContentException("Turno no encontrado - " + appointmentChangeStatusDTO.getId());
        }

        appointment.get().addStatus(AppointmentStatusEnum.ABSENT, appointmentChangeStatusDTO.getObservations());

        appointmentRepository.save(appointment.get());

        return new ResponseDTO(HttpStatus.OK.value(), MapperHelper.modelMapper().map(appointment.get(), AppointmentDTO.class));
    }

    @Override
    public ResponseDTO cancel(String token, AppointmentChangeStatusDTO appointmentChangeStatusDTO) throws Exception {
        Optional<Appointment> appointment = appointmentRepository.findByIdAndOrganizationId(appointmentChangeStatusDTO.getId(), OrganizationHelper.getOrganizationId(token));
        if (!appointment.isPresent()) {
            throw new NoContentException("Turno no encontrado - " + appointmentChangeStatusDTO.getId());
        }

        appointment.get().addStatus(AppointmentStatusEnum.CANCELLED, appointmentChangeStatusDTO.getObservations());

        emailService.sendCanceledAppointmentEmail(appointment.get());

        appointmentRepository.save(appointment.get());

        return new ResponseDTO(HttpStatus.OK.value(), MapperHelper.modelMapper().map(appointment.get(), AppointmentDTO.class));
    }

    @Override
    public ResponseDTO attend(String token, AppointmentChangeStatusDTO appointmentChangeStatusDTO) throws Exception {
        Optional<Appointment> appointment = appointmentRepository.findByIdAndOrganizationId(appointmentChangeStatusDTO.getId(), OrganizationHelper.getOrganizationId(token));
        if (!appointment.isPresent()) {
            throw new NoContentException("Turno no encontrado - " + appointmentChangeStatusDTO.getId());
        }

        appointment.get().addStatus(AppointmentStatusEnum.IN_ATTENTION, appointmentChangeStatusDTO.getObservations());

        appointmentRepository.save(appointment.get());

        return new ResponseDTO(HttpStatus.OK.value(), MapperHelper.modelMapper().map(appointment.get(), AppointmentDTO.class));
    }

    @Override
    public ResponseDTO finalize(String token, AppointmentChangeStatusDTO appointmentChangeStatusDTO) throws Exception {
        Optional<Appointment> appointment = appointmentRepository.findByIdAndOrganizationId(appointmentChangeStatusDTO.getId(), OrganizationHelper.getOrganizationId(token));
        if (!appointment.isPresent()) {
            throw new NoContentException("Turno no encontrado - " + appointmentChangeStatusDTO.getId());
        }

        appointment.get().addStatus(AppointmentStatusEnum.FINALIZED, appointmentChangeStatusDTO.getObservations());

        emailService.sendFinalizeAppointmentEmail(appointment.get());

        appointmentRepository.save(appointment.get());

        return new ResponseDTO(HttpStatus.OK.value(), MapperHelper.modelMapper().map(appointment.get(), AppointmentDTO.class));
    }
}
