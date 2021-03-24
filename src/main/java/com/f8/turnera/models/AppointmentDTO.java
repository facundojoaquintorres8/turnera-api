package com.f8.turnera.models;

public class AppointmentDTO {

    private Long id;

    private String customerBusinessName;

    private AppointmentStatusDTO lastAppointmentStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerBusinessName() {
        return customerBusinessName;
    }

    public void setCustomerBusinessName(String customerBusinessName) {
        this.customerBusinessName = customerBusinessName;
    }

    public AppointmentStatusDTO getLastAppointmentStatus() {
        return lastAppointmentStatus;
    }

    public void setLastAppointmentStatus(AppointmentStatusDTO lastAppointmentStatus) {
        this.lastAppointmentStatus = lastAppointmentStatus;
    }
}
