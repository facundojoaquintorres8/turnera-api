package com.f8.turnera.entities;

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

import com.f8.turnera.models.AppointmentStatusEnum;

import lombok.Data;

@Entity
@Table(name = "appointments_status")
@Data
public class AppointmentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(name = "status")
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
