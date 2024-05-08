package com.f8.turnera.controllers;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.domain.dtos.HolidayDTO;
import com.f8.turnera.domain.dtos.HolidayFilterDTO;
import com.f8.turnera.domain.services.IHolidayService;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HolidayController {

    @Autowired
    private IHolidayService service;

    @GetMapping("/holidays/findAllByFilter")
    @PreAuthorize("hasAuthority('holidays.read')")
    public ResponseEntity<Page<HolidayDTO>> findAllByFilter(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            HolidayFilterDTO filter) {
        Page<HolidayDTO> result = service.findAllByFilter(token, filter);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/holidays/{id}")
    @PreAuthorize("hasAuthority('holidays.read')")
    public ResponseEntity<HolidayDTO> getHoliday(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) {
        HolidayDTO result = service.findById(token, id);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/holidays")
    @PreAuthorize("hasAuthority('holidays.write')")
    public ResponseEntity<HolidayDTO> createHoliday(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody HolidayDTO holidayDTO) {
        HolidayDTO result = service.create(token, holidayDTO);

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/holidays")
    @PreAuthorize("hasAuthority('holidays.write')")
    public ResponseEntity<HolidayDTO> updateHoliday(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody HolidayDTO holidayDTO) {
        HolidayDTO result = service.update(token, holidayDTO);

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("holidays/{id}")
    @PreAuthorize("hasAuthority('holidays.delete')")
    public ResponseEntity<Void> deleteHoliday(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) {
        service.deleteById(token, id);

        return ResponseEntity.ok().build();
    }

}
