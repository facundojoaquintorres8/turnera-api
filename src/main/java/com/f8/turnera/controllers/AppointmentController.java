package com.f8.turnera.controllers;

import com.f8.turnera.models.AppointmentCancelDTO;
import com.f8.turnera.models.AppointmentDTO;
import com.f8.turnera.models.AppointmentSaveDTO;
import com.f8.turnera.services.IAppointmentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AppointmentController {
    private final Logger log = LoggerFactory.getLogger(AppointmentController.class);

    @Autowired
    private IAppointmentService appointmentService;

    @PostMapping("/appointments")
    @PreAuthorize("hasAuthority('appointments.book')")
    public ResponseEntity<AppointmentDTO> book(@RequestBody AppointmentSaveDTO appointmentSaveDTO) {
        log.info("REST request to book Appointment: {}", appointmentSaveDTO);

        AppointmentDTO result = appointmentService.book(appointmentSaveDTO);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("appointments/{id}/absent")
    @PreAuthorize("hasAuthority('appointments.absent')")
    public ResponseEntity<AppointmentDTO> absent(@PathVariable Long id) {
        log.info("REST request to absent Appointment: {}", id);

        AppointmentDTO result = appointmentService.absent(id);

        return ResponseEntity.ok().body(result);
    }
    
    @PostMapping("appointments/cancel")
    @PreAuthorize("hasAuthority('appointments.cancel')")
    public ResponseEntity<AppointmentDTO> cancel(@RequestBody AppointmentCancelDTO appointmentCancelDTO) {
        log.info("REST request to cancel Appointment: {}", appointmentCancelDTO);

        AppointmentDTO result = appointmentService.cancel(appointmentCancelDTO);

        return ResponseEntity.ok().body(result);
    }
    
    @GetMapping("appointments/{id}/attend")
    @PreAuthorize("hasAuthority('appointments.attend')")
    public ResponseEntity<AppointmentDTO> attend(@PathVariable Long id) {
        log.info("REST request to attend Appointment: {}", id);

        AppointmentDTO result = appointmentService.attend(id);

        return ResponseEntity.ok().body(result);
    }
    
    @GetMapping("appointments/{id}/finalize")
    @PreAuthorize("hasAuthority('appointments.finalize')")
    public ResponseEntity<AppointmentDTO> finalize(@PathVariable Long id) {
        log.info("REST request to finalize Appointment: {}", id);

        AppointmentDTO result = appointmentService.finalize(id);

        return ResponseEntity.ok().body(result);
    }

}
