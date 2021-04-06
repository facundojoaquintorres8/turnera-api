package com.f8.turnera.controllers;

import java.util.List;

import com.f8.turnera.models.AgendaDTO;
import com.f8.turnera.models.AgendaSaveDTO;
import com.f8.turnera.models.AppointmentFilterDTO;
import com.f8.turnera.services.IAgendaService;

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
public class AgendaController {
    private final Logger log = LoggerFactory.getLogger(AgendaController.class);

    @Autowired
    private IAgendaService agendaService;

    @GetMapping("/agendas/findAllByFilter")
    @PreAuthorize("hasAuthority('agendas.read')")
    public ResponseEntity<Page<AgendaDTO>> findAllByFilter(AppointmentFilterDTO filter) {
        log.info("REST request to get Agendas by filter: {}", filter);

        Page<AgendaDTO> result = agendaService.findAllByFilter(filter);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/agendas")
    @PreAuthorize("hasAuthority('agendas.write')")
    public ResponseEntity<List<AgendaDTO>> createAgenda(@RequestBody AgendaSaveDTO agendaDTO) {
        log.info("REST request to create Agenda: {}", agendaDTO);

        List<AgendaDTO> result = agendaService.create(agendaDTO);

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("agendas/{id}")
    @PreAuthorize("hasAuthority('agendas.delete')")
    public ResponseEntity<Void> deleteAgenda(@PathVariable Long id) {
        log.info("REST request to delete Agenda {}", id);

        agendaService.deleteById(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("agendas/{id}/desactivate")
    @PreAuthorize("hasAuthority('agendas.read')")
    public ResponseEntity<AgendaDTO> desactivateAgenda(@PathVariable Long id) {
        log.info("REST request to desactivate Agenda {}", id);

        AgendaDTO result = agendaService.desactivate(id);

        return ResponseEntity.ok().body(result);
    }

}
