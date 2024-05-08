package com.f8.turnera.controllers;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.domain.dtos.AgendaDTO;
import com.f8.turnera.domain.dtos.AgendaSaveDTO;
import com.f8.turnera.domain.dtos.AppointmentFilterDTO;
import com.f8.turnera.domain.services.IAgendaService;

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
public class AgendaController {

    @Autowired
    private IAgendaService agendaService;

    @GetMapping("/agendas/findAllByFilter")
    @PreAuthorize("hasAuthority('agendas.read')")
    public ResponseEntity<Page<AgendaDTO>> findAllByFilter(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            AppointmentFilterDTO filter) {
        Page<AgendaDTO> result = agendaService.findAllByFilter(token, filter);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/agendas")
    @PreAuthorize("hasAuthority('agendas.write')")
    public ResponseEntity<Boolean> createAgenda(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @RequestBody AgendaSaveDTO agendaDTO) {
        Boolean result = agendaService.create(token, agendaDTO);

        if (!result) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("agendas/{id}")
    @PreAuthorize("hasAuthority('agendas.delete')")
    public ResponseEntity<Void> deleteAgenda(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) {
        agendaService.deleteById(token, id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("agendas/{id}/desactivate")
    @PreAuthorize("hasAuthority('agendas.read')")
    public ResponseEntity<AgendaDTO> desactivateAgenda(
            @RequestHeader(name = SecurityConstants.HEADER_TOKEN) String token,
            @PathVariable Long id) {
        AgendaDTO result = agendaService.desactivate(token, id);

        return ResponseEntity.ok().body(result);
    }

}
