package com.f8.turnera.models;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AgendaDTO {

    private Long id;
    private Long organizationId;
    private Boolean active;
    private ResourceDTO resource;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private AppointmentDTO lastAppointment;

}
