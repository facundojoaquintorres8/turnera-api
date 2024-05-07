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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table(name = "customers")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @NotNull
    private Long id;

    @Column(name = "active")
    @NotNull
    private Boolean active;

    @Column(name = "created_date")
    @NotNull
    private LocalDateTime createdDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id")
    @NotNull
    private Organization organization;
    
    @Column(name = "business_name")
    @NotBlank
    private String businessName;
    
    @Column(name = "brand_name")
    private String brandName;
    
    @Column(name = "cuit")
    private String cuit;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "phone1")
    @NotBlank
    private String phone1;
    
    @Column(name = "phone2")
    private String phone2;

    @Column(name = "email")
    @NotBlank
    private String email;

}
