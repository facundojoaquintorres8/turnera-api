package com.f8.turnera.security.models;

import lombok.Data;

@Data
public class PasswordChangeDTO {

    private String username;
    private String currentPassword;
    private String password;

}
