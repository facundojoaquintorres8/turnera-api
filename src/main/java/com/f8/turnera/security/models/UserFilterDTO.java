package com.f8.turnera.security.models;

import com.f8.turnera.models.DefaultFilterDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserFilterDTO extends DefaultFilterDTO {

    private String firstName;
    private String lastName;
    private String username;

}