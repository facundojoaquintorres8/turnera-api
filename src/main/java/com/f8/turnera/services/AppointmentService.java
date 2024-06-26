package com.f8.turnera.services;

import java.time.LocalDateTime;
import java.util.Optional;

import com.f8.turnera.email.IEmailService;
import com.f8.turnera.entities.Agenda;
import com.f8.turnera.entities.Appointment;
import com.f8.turnera.entities.Customer;
import com.f8.turnera.entities.Organization;
import com.f8.turnera.models.AppointmentChangeStatusDTO;
import com.f8.turnera.models.AppointmentDTO;
import com.f8.turnera.models.AppointmentSaveDTO;
import com.f8.turnera.models.AppointmentStatusEnum;
import com.f8.turnera.repositories.IAgendaRepository;
import com.f8.turnera.repositories.IAppointmentRepository;
import com.f8.turnera.repositories.IOrganizationRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService implements IAppointmentService {

    @Autowired
    private IAppointmentRepository appointmentRepository;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IOrganizationRepository organizationRepository;
    
    @Autowired
    private IAgendaRepository agendaRepository;

    @Autowired
    private IEmailService emailService;

    @Override
    public AppointmentDTO book(AppointmentSaveDTO appointmentSaveDTO) {
        Optional<Organization> organization = organizationRepository
                .findById(appointmentSaveDTO.getAgenda().getOrganizationId());
        if (!organization.isPresent()) {
            throw new RuntimeException("El Turno no tiene una Organización asociada válida.");
        }

        Optional<Agenda> agenda = agendaRepository.findById(appointmentSaveDTO.getAgenda().getId());
        if (!agenda.isPresent()) {
            throw new RuntimeException("El Turno no tiene una Disponibilidad válida.");
        }

        ModelMapper modelMapper = new ModelMapper();

        Customer customer = null;
        if (appointmentSaveDTO.getCustomer().getId() != null) {
            customer = modelMapper.map(appointmentSaveDTO.getCustomer(), Customer.class);
        } else {
            customer = modelMapper.map(
                    customerService.createQuick(appointmentSaveDTO.getCustomer(), organization.get()), Customer.class);
        }

        Appointment appointment = new Appointment(LocalDateTime.now(), organization.get(), agenda.get(), customer);
        appointment.addStatus(AppointmentStatusEnum.BOOKED, null);
        
        try {
            appointmentRepository.save(appointment);
            agenda.get().setLastAppointment(appointment);
            agendaRepository.save(agenda.get());
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }

        emailService.sendBookedAppointmentEmail(appointment);

        return modelMapper.map(appointment, AppointmentDTO.class);
    }

    @Override
    public AppointmentDTO absent(AppointmentChangeStatusDTO appointmentChangeStatusDTO) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentChangeStatusDTO.getId());
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
    public AppointmentDTO cancel(AppointmentChangeStatusDTO appointmentChangeStatusDTO) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentChangeStatusDTO.getId());
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
    public AppointmentDTO attend(AppointmentChangeStatusDTO appointmentChangeStatusDTO) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentChangeStatusDTO.getId());
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
    public AppointmentDTO finalize(AppointmentChangeStatusDTO appointmentChangeStatusDTO) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentChangeStatusDTO.getId());
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
