package com.f8.turnera.controllers;

import com.f8.turnera.models.HolidayDTO;
import com.f8.turnera.models.HolidayFilterDTO;
import com.f8.turnera.services.IHolidayService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HolidayController {
    private final Logger log = LoggerFactory.getLogger(HolidayController.class);

    @Autowired
    private IHolidayService service;

    @GetMapping("/holidays/findAllByFilter")
    @PreAuthorize("hasAuthority('holidays.read')")
    public ResponseEntity<Page<HolidayDTO>> findAllByFilter(HolidayFilterDTO filter) {
        log.info("REST request to get Holidays by filter: {}", filter);

        Page<HolidayDTO> result = service.findAllByFilter(filter);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/holidays/{id}")
    @PreAuthorize("hasAuthority('holidays.read')")
    public ResponseEntity<HolidayDTO> getHoliday(@PathVariable Long id) {
        log.info("REST request to get Holiday {}", id);

        HolidayDTO result = service.findById(id);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/holidays")
    @PreAuthorize("hasAuthority('holidays.write')")
    public ResponseEntity<HolidayDTO> createHoliday(@RequestBody HolidayDTO holidayDTO) {
        log.info("REST request to create Holiday: {}", holidayDTO);

        HolidayDTO result = service.create(holidayDTO);

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/holidays")
    @PreAuthorize("hasAuthority('holidays.write')")
    public ResponseEntity<HolidayDTO> updateHoliday(@RequestBody HolidayDTO holidayDTO) {
        log.info("REST request to update Holiday: {}", holidayDTO);

        HolidayDTO result = service.update(holidayDTO);

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("holidays/{id}")
    @PreAuthorize("hasAuthority('holidays.delete')")
    public ResponseEntity<Void> deleteHoliday(@PathVariable Long id) {
        log.info("REST request to delete Holiday {}", id);

        service.deleteById(id);

        return ResponseEntity.ok().build();
    }

}
