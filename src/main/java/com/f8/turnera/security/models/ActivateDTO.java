package com.f8.turnera.security.models;

import lombok.Data;

@Data
public class ActivateDTO {
    
    private String password;
    private String activationKey;

}
