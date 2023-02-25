package com.f8.turnera.models;

import lombok.Data;

@Data
public class AppointmentStatusDTO {

    private Long id;
    private AppointmentStatusEnum status;
    private String observations;

}