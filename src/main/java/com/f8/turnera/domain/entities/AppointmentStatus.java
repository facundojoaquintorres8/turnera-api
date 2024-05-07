package com.f8.turnera.domain.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.f8.turnera.domain.dtos.AppointmentStatusEnum;

import lombok.Data;

@Entity
@Table(name = "appointments_status")
@Data
public class AppointmentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")    
    @NotNull
    private Long id;

    @Column(name = "created_date")
    @NotNull
    private LocalDateTime createdDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "appointment_id")
    @NotNull
    private Appointment appointment;

    @Column(name = "status")
    @NotNull
    private AppointmentStatusEnum status;

    @Column(name = "observations")
    private String observations;

    public AppointmentStatus(LocalDateTime createdDate, Appointment appointment, AppointmentStatusEnum status,
            String observations) {
        this.createdDate = createdDate;
        this.appointment = appointment;
        this.status = status;
        this.observations = observations;
    }

    public AppointmentStatus() {
    }
}
