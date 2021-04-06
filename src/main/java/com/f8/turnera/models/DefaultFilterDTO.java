package com.f8.turnera.models;

import java.util.List;

public class DefaultFilterDTO {

    private int page;

    private List<String> sort;

    private Long organizationId;

    private Boolean active;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

}
