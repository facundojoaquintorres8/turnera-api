package com.f8.turnera.security.models;

import com.f8.turnera.models.DefaultFilterDTO;

public class ProfileFilterDTO extends DefaultFilterDTO {

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}