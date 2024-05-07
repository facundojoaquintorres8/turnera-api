package com.f8.turnera.security.domain.dtos;

import java.util.List;

import lombok.Data;

@Data
public class ProfileDTO {

    private Long id;
    private Long organizationId;
    private Boolean active;
    private String description;
    private List<PermissionDTO> permissions;

}
