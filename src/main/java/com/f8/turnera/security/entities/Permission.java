package com.f8.turnera.security.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table(name = "permissions")
@Data
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @NotNull
    private Long id;

    @Column(name = "description")
    @NotBlank
    private String description;

    @Column(name = "code", unique = true)
    @NotBlank
    private String code;

}
