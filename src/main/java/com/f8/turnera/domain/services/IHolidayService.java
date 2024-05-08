package com.f8.turnera.domain.services;

import java.time.LocalDate;
import java.util.List;

import com.f8.turnera.domain.dtos.HolidayDTO;
import com.f8.turnera.domain.dtos.HolidayFilterDTO;
import com.f8.turnera.domain.dtos.ResponseDTO;

public interface IHolidayService {

    ResponseDTO findAllByFilter(String token, HolidayFilterDTO filter) throws Exception;

    ResponseDTO findById(String token, Long id) throws Exception;

    ResponseDTO create(String token, HolidayDTO holidayDTO) throws Exception;

    ResponseDTO update(String token, HolidayDTO holidayDTO) throws Exception;

    ResponseDTO deleteById(String token, Long id) throws Exception;

    List<LocalDate> findAllDatesToAgenda(String token) throws Exception;

}
