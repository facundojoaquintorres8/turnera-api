package com.f8.turnera.domain.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import com.f8.turnera.domain.dtos.HolidayDTO;
import com.f8.turnera.domain.dtos.HolidayFilterDTO;

public interface IHolidayService {

    public Page<HolidayDTO> findAllByFilter(String token, HolidayFilterDTO filter);

    public HolidayDTO findById(String token, Long id);

    public HolidayDTO create(String token, HolidayDTO holidayDTO);

    public HolidayDTO update(String token, HolidayDTO holidayDTO);

    public void deleteById(String token, Long id);

    public List<LocalDate> findAllDatesToAgenda(String token);

}
