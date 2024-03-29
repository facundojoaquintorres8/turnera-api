package com.f8.turnera.models;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class AppointmentFilterDTO extends DefaultFilterDTO {

    private Long resourceTypeId;

    private Long resourceId;

    private String resourceDescription;

    private Long customerId;

    private String customerBusinessName;

    private AppointmentStatusEnum status;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate from;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate to;

    private Boolean ignorePaginated;

    public Long getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(Long resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceDescription() {
        return resourceDescription;
    }

    public void setResourceDescription(String resourceDescription) {
        this.resourceDescription = resourceDescription;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerBusinessName() {
        return customerBusinessName;
    }

    public void setCustomerBusinessName(String customerBusinessName) {
        this.customerBusinessName = customerBusinessName;
    }

    public AppointmentStatusEnum getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatusEnum status) {
        this.status = status;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }
    
    public Boolean getIgnorePaginated() {
        return ignorePaginated;
    }

    public void setIgnorePaginated(Boolean ignorePaginated) {
        this.ignorePaginated = ignorePaginated;
    }
}
