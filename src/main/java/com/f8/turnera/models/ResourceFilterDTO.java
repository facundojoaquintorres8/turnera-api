package com.f8.turnera.models;

import lombok.Data;

@Data
public class ResourceFilterDTO extends DefaultFilterDTO {

    private Long resourceTypeId;
    private String description;
    private String code;
    private String resourceTypeDescription;
    
}
