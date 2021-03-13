package com.f8.turnera.models;

import java.time.LocalDateTime;

public class AgendaDTO {

    private Long id;

    private Long organizationId;

    private Boolean active;

    private ResourceDTO resource;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private AppointmentDTO lastAppointment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ResourceDTO getResource() {
        return resource;
    }

    public void setResource(ResourceDTO resource) {
        this.resource = resource;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public AppointmentDTO getLastAppointment() {
        return lastAppointment;
    }

    public void setLastAppointment(AppointmentDTO lastAppointment) {
        this.lastAppointment = lastAppointment;
    }
}
