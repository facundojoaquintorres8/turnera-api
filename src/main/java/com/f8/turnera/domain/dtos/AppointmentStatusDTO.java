package com.f8.turnera.domain.dtos;

import lombok.Data;

@Data
public class AppointmentStatusDTO {

    private Long id;
    private AppointmentStatusEnum status;
    private String observations;

}