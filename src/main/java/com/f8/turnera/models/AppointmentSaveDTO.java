package com.f8.turnera.models;

import lombok.Data;

@Data
public class AppointmentSaveDTO {

    private AgendaDTO agenda;
    private CustomerDTO customer;

}
