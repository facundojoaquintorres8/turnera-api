package com.f8.turnera.models;

public class AppointmentChangeStatusDTO {

    private Long id;

    private String observations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}
