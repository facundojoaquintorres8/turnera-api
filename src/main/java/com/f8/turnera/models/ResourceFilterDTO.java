package com.f8.turnera.models;

public class ResourceFilterDTO extends DefaultFilterDTO {

    private Long resourceTypeId;

    private String description;

    private String code;

    private String resourceTypeDescription;

    public Long getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(Long resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResourceTypeDescription() {
        return resourceTypeDescription;
    }

    public void setResourceTypeDescription(String resourceTypeDescription) {
        this.resourceTypeDescription = resourceTypeDescription;
    }
}
