package com.f8.turnera.domain.dtos;

import java.time.LocalDate;

import lombok.Data;

@Data
public class HolidayDTO {

    private Long id;
    private Boolean active;
    private LocalDate date;
    private String description;
    private Boolean useInAgenda;

}
