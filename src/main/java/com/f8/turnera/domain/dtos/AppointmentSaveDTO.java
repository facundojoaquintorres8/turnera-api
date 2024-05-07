package com.f8.turnera.domain.dtos;

import lombok.Data;

@Data
public class AppointmentSaveDTO {

    private AgendaDTO agenda;
    private CustomerDTO customer;

}
