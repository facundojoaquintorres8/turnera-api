package com.f8.turnera.domain.dtos;

import java.util.List;

import lombok.Data;

@Data
public class DefaultFilterDTO {

    private int page;
    private List<String> sort;
    private Long organizationId;
    private Boolean active;
    private Boolean ignorePaginated;

}
