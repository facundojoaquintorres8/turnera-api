package com.f8.turnera.domain.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import com.f8.turnera.domain.dtos.HolidayDTO;
import com.f8.turnera.domain.dtos.HolidayFilterDTO;

public interface IHolidayService {

    Page<HolidayDTO> findAllByFilter(String token, HolidayFilterDTO filter) throws Exception;

    HolidayDTO findById(String token, Long id) throws Exception;

    HolidayDTO create(String token, HolidayDTO holidayDTO) throws Exception;

    HolidayDTO update(String token, HolidayDTO holidayDTO) throws Exception;

    void deleteById(String token, Long id) throws Exception;

    List<LocalDate> findAllDatesToAgenda(String token) throws Exception;

}
