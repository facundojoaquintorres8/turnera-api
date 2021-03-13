package com.f8.turnera.models;

public class AppointmentSaveDTO {

    private AgendaDTO agenda;

    private CustomerDTO customer;

    public AgendaDTO getAgenda() {
        return agenda;
    }

    public void setAgenda(AgendaDTO agenda) {
        this.agenda = agenda;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }
}
