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
import javax.validation.constraints.NotNull;

import com.f8.turnera.entities.Organization;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "profiles")
@Data
@EqualsAndHashCode(exclude = "users")
public class Profile {

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

    @Column(name = "description")
    @NotNull
    private String description;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "profiles_permissions", joinColumns = @JoinColumn(name = "profile_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "users_profiles", joinColumns = @JoinColumn(name = "profile_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;

    public Profile(Boolean active, LocalDateTime createdDate, Organization organization, String description,
            Set<Permission> permissions) {
        this.active = active;
        this.createdDate = createdDate;
        this.organization = organization;
        this.description = description;
        this.permissions = permissions;
    }

    public Profile() {
    }

    @Override
    public String toString() {
        return "Profile()..."; // TODO
    }
}
