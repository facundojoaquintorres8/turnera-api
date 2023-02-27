package com.f8.turnera.services;

import com.f8.turnera.models.HolidayDTO;
import com.f8.turnera.models.HolidayFilterDTO;

import org.springframework.data.domain.Page;

public interface IHolidayService {

    public Page<HolidayDTO> findAllByFilter(HolidayFilterDTO filter);

    public HolidayDTO findById(Long id);

    public HolidayDTO create(HolidayDTO holidayDTO);

    public HolidayDTO update(HolidayDTO holidayDTO);

    public void deleteById(Long id);
}
