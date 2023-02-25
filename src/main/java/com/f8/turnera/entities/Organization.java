package com.f8.turnera.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "organizations")
@Data
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "business_name")
    private String businessName;
    
    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "cuit")
    private String cuit;

    @Column(name = "address")
    private String address;

    @Column(name = "phone1")
    private String phone1;

    @Column(name = "phone2")
    private String phone2;

    @Column(name = "default_email")
    private String defaultEmail;

}
