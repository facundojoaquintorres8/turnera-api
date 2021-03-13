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

@Entity
@Table(name = "profiles")
public class Profile {

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

    @Column(name = "description")
    private String description;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name="profiles_permissions",
        joinColumns=@JoinColumn(name="profile_id"),
        inverseJoinColumns=@JoinColumn(name="permission_id")
    )                    
    private Set<Permission> permissions;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name="users_profiles",
        joinColumns=@JoinColumn(name="profile_id"),
        inverseJoinColumns=@JoinColumn(name="user_id")
    )                    
    private Set<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

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
}
