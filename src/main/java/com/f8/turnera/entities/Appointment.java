package com.f8.turnera.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.f8.turnera.models.AppointmentStatusEnum;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agenda_id")
    private Agenda agenda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appointment")
    private Set<AppointmentStatus> status;

    private AppointmentStatusEnum currentStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<AppointmentStatus> getStatus() {
        return status;
    }

    public void setStatus(Set<AppointmentStatus> status) {
        this.status = status;
    }

    public void addStatus(AppointmentStatusEnum appointmentStatus) {
        status.add(new AppointmentStatus(LocalDateTime.now(), this, appointmentStatus));
        setCurrentStatus(appointmentStatus);
    }

    public AppointmentStatusEnum getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(AppointmentStatusEnum currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Appointment(LocalDateTime createdDate, Organization organization, Agenda agenda, Customer customer) {
        this.createdDate = createdDate;
        this.organization = organization;
        this.agenda = agenda;
        this.customer = customer;
        this.status = new HashSet<>();
    }

    public Appointment() {
        this.status = new HashSet<>();
    }
}
