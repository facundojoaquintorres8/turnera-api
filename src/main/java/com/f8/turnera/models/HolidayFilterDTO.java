package com.f8.turnera.models;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HolidayFilterDTO extends DefaultFilterDTO {

    private LocalDate date;
    private String description;
    private Boolean useInAgenda;

}
