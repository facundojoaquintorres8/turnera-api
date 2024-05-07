package com.f8.turnera.domain.dtos;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class AgendaDTO {

    private Long id;
    private Long organizationId;
    private Boolean active;
    private ResourceDTO resource;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private AppointmentDTO lastAppointment;

}
