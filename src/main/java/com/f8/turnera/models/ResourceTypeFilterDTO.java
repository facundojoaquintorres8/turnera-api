package com.f8.turnera.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceTypeFilterDTO extends DefaultFilterDTO {

    private String description;

}
