package com.f8.turnera.security.domain.entities;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.f8.turnera.domain.entities.Organization;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

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
    
    @Column(name = "first_name")
    @NotBlank
    private String firstName;

    @Column(name = "last_name")
    @NotBlank
    private String lastName;

    @Column(name = "username", unique = true)
    @NotBlank
    private String username;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "activation_key")
    @JsonIgnore
    private String activationKey;
    
    @Column(name = "reset_key")
    @JsonIgnore
    private String resetKey;
    
    @Column(name = "reset_date")
    @JsonIgnore
    private LocalDateTime resetDate;
    
    @Column(name = "admin")
    private Boolean admin;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name="users_profiles",
        joinColumns=@JoinColumn(name="user_id"),
        inverseJoinColumns=@JoinColumn(name="profile_id")
    )
    private Set<Profile> profiles;

}
