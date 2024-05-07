package com.f8.turnera.security.domain.dtos;

import java.util.List;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private Long organizationId;
    private Boolean active;
    private String firstName;
    private String lastName;
    private String username;
    private List<ProfileDTO> profiles;

}
