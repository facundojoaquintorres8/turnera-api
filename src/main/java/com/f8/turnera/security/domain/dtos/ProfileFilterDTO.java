package com.f8.turnera.security.domain.dtos;

import com.f8.turnera.domain.dtos.DefaultFilterDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProfileFilterDTO extends DefaultFilterDTO {

    private String description;

}