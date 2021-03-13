package com.f8.turnera.models;

public class ResourceFilterDTO extends DefaultFilterDTO {

    private Long resourceTypeId;

    public Long getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(Long resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }
}
