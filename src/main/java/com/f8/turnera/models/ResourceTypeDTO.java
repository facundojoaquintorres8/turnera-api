package com.f8.turnera.models;

import lombok.Data;

@Data
public class ResourceTypeDTO {

    private Long id;
    private Boolean active;
    private Long organizationId;
    private String description;

}
