package com.f8.turnera.security.models;

import lombok.Data;

@Data
public class SessionUserDTO {

    private UserDTO user;
    private String token;

}
