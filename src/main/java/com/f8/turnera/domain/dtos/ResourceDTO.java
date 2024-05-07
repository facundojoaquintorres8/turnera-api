package com.f8.turnera.domain.dtos;

import lombok.Data;

@Data
public class ResourceDTO {

    private Long id;
    private Long organizationId;
    private Boolean active;
    private String description;
    private String code;
    private ResourceTypeDTO resourceType;

}
