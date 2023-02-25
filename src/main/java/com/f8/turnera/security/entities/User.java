package com.f8.turnera.security.entities;

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

import com.f8.turnera.entities.Organization;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(exclude = "profiles")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id")
    private Organization organization;
    
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
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

    @Override
    public String toString() {
        return "User()..."; // TODO
    }

}
