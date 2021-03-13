package com.f8.turnera.models;

public class AppointmentDTO {

    private Long id;

    private String customerBusinessName;

    private AppointmentStatusEnum currentStatus;

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

    public AppointmentStatusEnum getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(AppointmentStatusEnum currentStatus) {
        this.currentStatus = currentStatus;
    }
}
