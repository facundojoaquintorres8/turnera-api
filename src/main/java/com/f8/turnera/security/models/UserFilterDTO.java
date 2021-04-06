package com.f8.turnera.security.models;

import com.f8.turnera.models.DefaultFilterDTO;

public class UserFilterDTO extends DefaultFilterDTO {

    private String firstName;

    private String lastName;
    
    private String username;

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}