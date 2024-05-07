package com.f8.turnera.domain.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import com.f8.turnera.domain.dtos.HolidayDTO;
import com.f8.turnera.domain.dtos.HolidayFilterDTO;

public interface IHolidayService {

    public Page<HolidayDTO> findAllByFilter(HolidayFilterDTO filter);

    public HolidayDTO findById(Long id);

    public HolidayDTO create(HolidayDTO holidayDTO);

    public HolidayDTO update(HolidayDTO holidayDTO);

    public void deleteById(Long id);

    public List<LocalDate> findAllDatesToAgenda();

}
