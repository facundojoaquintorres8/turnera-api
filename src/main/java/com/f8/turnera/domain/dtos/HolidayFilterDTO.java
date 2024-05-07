package com.f8.turnera.domain.dtos;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HolidayFilterDTO extends DefaultFilterDTO {

    private LocalDate date;
    private String description;
    private Boolean useInAgenda;

}
