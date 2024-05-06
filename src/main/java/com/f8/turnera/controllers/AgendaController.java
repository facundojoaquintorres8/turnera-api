package com.f8.turnera.controllers;

import com.f8.turnera.models.AgendaDTO;
import com.f8.turnera.models.AgendaSaveDTO;
import com.f8.turnera.models.AppointmentFilterDTO;
import com.f8.turnera.services.IAgendaService;

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
public class AgendaController {

    @Autowired
    private IAgendaService agendaService;

    @GetMapping("/agendas/findAllByFilter")
    @PreAuthorize("hasAuthority('agendas.read')")
    public ResponseEntity<Page<AgendaDTO>> findAllByFilter(AppointmentFilterDTO filter) {
        Page<AgendaDTO> result = agendaService.findAllByFilter(filter);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/agendas")
    @PreAuthorize("hasAuthority('agendas.write')")
    public ResponseEntity<Boolean> createAgenda(@RequestBody AgendaSaveDTO agendaDTO) {
        Boolean result = agendaService.create(agendaDTO);

        if (!result) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("agendas/{id}")
    @PreAuthorize("hasAuthority('agendas.delete')")
    public ResponseEntity<Void> deleteAgenda(@PathVariable Long id) {
        agendaService.deleteById(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("agendas/{id}/desactivate")
    @PreAuthorize("hasAuthority('agendas.read')")
    public ResponseEntity<AgendaDTO> desactivateAgenda(@PathVariable Long id) {
        AgendaDTO result = agendaService.desactivate(id);

        return ResponseEntity.ok().body(result);
    }

}
