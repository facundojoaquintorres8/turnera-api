package com.f8.turnera.security.domain.dtos;

import lombok.Data;

@Data
public class ActivateDTO {
    
    private String password;
    private String activationKey;

}
