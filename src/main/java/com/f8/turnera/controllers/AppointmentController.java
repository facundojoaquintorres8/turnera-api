package com.f8.turnera.controllers;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.domain.dtos.AppointmentChangeStatusDTO;
import com.f8.turnera.domain.dtos.AppointmentDTO;
import com.f8.turnera.domain.dtos.AppointmentSaveDTO;
import com.f8.turnera.domain.services.IAppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    private IAppointmentService appointmentService;

    @PostMapping("/appointments")
    @PreAuthorize("hasAuthority('appointments.book')")
    public ResponseEntity<AppointmentDTO> book(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody AppointmentSaveDTO appointmentSaveDTO) {
        AppointmentDTO result = appointmentService.book(token, appointmentSaveDTO);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("appointments/absent")
    @PreAuthorize("hasAuthority('appointments.absent')")
    public ResponseEntity<AppointmentDTO> absent(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody AppointmentChangeStatusDTO appointmentChangeStatusDTO) {
        AppointmentDTO result = appointmentService.absent(token, appointmentChangeStatusDTO);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("appointments/cancel")
    @PreAuthorize("hasAuthority('appointments.cancel')")
    public ResponseEntity<AppointmentDTO> cancel(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody AppointmentChangeStatusDTO appointmentChangeStatusDTO) {
        AppointmentDTO result = appointmentService.cancel(token, appointmentChangeStatusDTO);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("appointments/attend")
    @PreAuthorize("hasAuthority('appointments.attend')")
    public ResponseEntity<AppointmentDTO> attend(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody AppointmentChangeStatusDTO appointmentChangeStatusDTO) {
        AppointmentDTO result = appointmentService.attend(token, appointmentChangeStatusDTO);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("appointments/finalize")
    @PreAuthorize("hasAuthority('appointments.finalize')")
    public ResponseEntity<AppointmentDTO> finalize(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody AppointmentChangeStatusDTO appointmentChangeStatusDTO) {
        AppointmentDTO result = appointmentService.finalize(token, appointmentChangeStatusDTO);

        return ResponseEntity.ok().body(result);
    }

}
