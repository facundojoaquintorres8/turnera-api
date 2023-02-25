package com.f8.turnera.security.models;

import com.f8.turnera.models.DefaultFilterDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProfileFilterDTO extends DefaultFilterDTO {

    private String description;

}