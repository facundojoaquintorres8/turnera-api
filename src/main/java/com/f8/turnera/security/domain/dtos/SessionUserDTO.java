package com.f8.turnera.security.domain.dtos;

import lombok.Data;

@Data
public class SessionUserDTO {

    private UserDTO user;
    private String token;

}
