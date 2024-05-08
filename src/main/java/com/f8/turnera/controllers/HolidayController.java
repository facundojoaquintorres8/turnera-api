package com.f8.turnera.controllers;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.domain.dtos.HolidayDTO;
import com.f8.turnera.domain.dtos.HolidayFilterDTO;
import com.f8.turnera.domain.dtos.ResponseDTO;
import com.f8.turnera.domain.services.IHolidayService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ResponseDTO> findAllByFilter(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            HolidayFilterDTO request) throws Exception {
        return new ResponseEntity<>(service.findAllByFilter(token, request), HttpStatus.OK);
    }

    @GetMapping("/holidays/{id}")
    @PreAuthorize("hasAuthority('holidays.read')")
    public ResponseEntity<ResponseDTO> getHoliday(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) throws Exception {
        return new ResponseEntity<>(service.findById(token, id), HttpStatus.OK);
    }

    @PostMapping("/holidays")
    @PreAuthorize("hasAuthority('holidays.write')")
    public ResponseEntity<ResponseDTO> createHoliday(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody HolidayDTO request) throws Exception {
        return new ResponseEntity<>(service.create(token, request), HttpStatus.OK);
    }

    @PutMapping("/holidays")
    @PreAuthorize("hasAuthority('holidays.write')")
    public ResponseEntity<ResponseDTO> updateHoliday(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody HolidayDTO request) throws Exception {
        return new ResponseEntity<>(service.update(token, request), HttpStatus.OK);
    }

    @DeleteMapping("holidays/{id}")
    @PreAuthorize("hasAuthority('holidays.delete')")
    public ResponseEntity<ResponseDTO> deleteHoliday(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) throws Exception {
        return new ResponseEntity<>(service.deleteById(token, id), HttpStatus.OK);
    }

}
