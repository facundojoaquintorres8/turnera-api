package com.f8.turnera.models;

import java.time.LocalDate;

import lombok.Data;

@Data
public class HolidayDTO {

    private Long id;
    private Long organizationId;
    private Boolean active;
    private LocalDate date;
    private String description;
    private Boolean useInAgenda;

}
