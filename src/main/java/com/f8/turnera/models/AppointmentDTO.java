package com.f8.turnera.models;

import lombok.Data;

@Data
public class AppointmentDTO {

    private Long id;
    private String customerBusinessName;
    private AppointmentStatusDTO lastAppointmentStatus;

}
