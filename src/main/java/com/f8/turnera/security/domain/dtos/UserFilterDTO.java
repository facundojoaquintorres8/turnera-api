package com.f8.turnera.security.domain.dtos;

import com.f8.turnera.domain.dtos.DefaultFilterDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserFilterDTO extends DefaultFilterDTO {

    private String firstName;
    private String lastName;
    private String username;

}