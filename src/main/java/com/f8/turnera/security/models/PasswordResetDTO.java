package com.f8.turnera.security.models;

import lombok.Data;

@Data
public class PasswordResetDTO {
    
    private String password;
    
    private String resetKey;

}
