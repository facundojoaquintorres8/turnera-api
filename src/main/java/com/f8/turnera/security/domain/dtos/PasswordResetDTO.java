package com.f8.turnera.security.domain.dtos;

import lombok.Data;

@Data
public class PasswordResetDTO {
    
    private String password;
    private String resetKey;

}
