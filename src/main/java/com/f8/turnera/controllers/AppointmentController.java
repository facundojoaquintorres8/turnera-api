package com.f8.turnera.controllers;

import com.f8.turnera.models.AppointmentChangeStatusDTO;
import com.f8.turnera.models.AppointmentDTO;
import com.f8.turnera.models.AppointmentSaveDTO;
import com.f8.turnera.services.IAppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    private IAppointmentService appointmentService;

    @PostMapping("/appointments")
    @PreAuthorize("hasAuthority('appointments.book')")
    public ResponseEntity<AppointmentDTO> book(@RequestBody AppointmentSaveDTO appointmentSaveDTO) {
        AppointmentDTO result = appointmentService.book(appointmentSaveDTO);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("appointments/absent")
    @PreAuthorize("hasAuthority('appointments.absent')")
    public ResponseEntity<AppointmentDTO> absent(@RequestBody AppointmentChangeStatusDTO appointmentChangeStatusDTO) {
        AppointmentDTO result = appointmentService.absent(appointmentChangeStatusDTO);

        return ResponseEntity.ok().body(result);
    }
    
    @PostMapping("appointments/cancel")
    @PreAuthorize("hasAuthority('appointments.cancel')")
    public ResponseEntity<AppointmentDTO> cancel(@RequestBody AppointmentChangeStatusDTO appointmentChangeStatusDTO) {
        AppointmentDTO result = appointmentService.cancel(appointmentChangeStatusDTO);

        return ResponseEntity.ok().body(result);
    }
    
    @PostMapping("appointments/attend")
    @PreAuthorize("hasAuthority('appointments.attend')")
    public ResponseEntity<AppointmentDTO> attend(@RequestBody AppointmentChangeStatusDTO appointmentChangeStatusDTO) {
        AppointmentDTO result = appointmentService.attend(appointmentChangeStatusDTO);

        return ResponseEntity.ok().body(result);
    }
    
    @PostMapping("appointments/finalize")
    @PreAuthorize("hasAuthority('appointments.finalize')")
    public ResponseEntity<AppointmentDTO> finalize(@RequestBody AppointmentChangeStatusDTO appointmentChangeStatusDTO) {
        AppointmentDTO result = appointmentService.finalize(appointmentChangeStatusDTO);

        return ResponseEntity.ok().body(result);
    }

}
