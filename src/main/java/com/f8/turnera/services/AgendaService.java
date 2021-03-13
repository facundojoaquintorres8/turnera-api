package com.f8.turnera.services;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.f8.turnera.entities.Agenda;
import com.f8.turnera.entities.Organization;
import com.f8.turnera.entities.Resource;
import com.f8.turnera.models.AgendaDTO;
import com.f8.turnera.models.AgendaSaveDTO;
import com.f8.turnera.models.AppointmentFilterDTO;
import com.f8.turnera.models.AppointmentStatusEnum;
import com.f8.turnera.repositories.IAgendaRepository;
import com.f8.turnera.repositories.IOrganizationRepository;
import com.f8.turnera.repositories.IResourceRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgendaService implements IAgendaService {

    @Autowired
    private IAgendaRepository agendaRepository;

    @Autowired
    private IOrganizationRepository organizationRepository;

    @Autowired
    private IResourceRepository resourceRepository;

    @Autowired
    private EntityManager em;

    @Override
    public List<AgendaDTO> findAllByFilter(AppointmentFilterDTO filter) {
        ModelMapper modelMapper = new ModelMapper();

        List<Agenda> agendas = findByCriteria(filter);
        agendas.sort(Comparator.comparing(Agenda::getStartDate));
        return agendas.stream().map(agenda -> modelMapper.map(agenda, AgendaDTO.class)).collect(Collectors.toList());
    }

    private List<Agenda> findByCriteria(AppointmentFilterDTO filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Agenda> cq = cb.createQuery(Agenda.class);

        List<Predicate> predicates = new ArrayList<>();

        Root<Agenda> root = cq.from(Agenda.class);
        if (filter.getResourceId() != null) {
            Predicate predicate = cb.equal(root.join("resource", JoinType.LEFT), filter.getResourceId());
            predicates.add(predicate);
        }
        if (filter.getOrganizationId() != null) {
            Predicate predicate = cb.equal(root.join("organization", JoinType.LEFT), filter.getOrganizationId());
            predicates.add(predicate);
        }
        if (filter.getResourceTypeId() != null) {
            Predicate predicate = cb.equal(root.join("resource", JoinType.LEFT).get("resourceType"),
                    filter.getResourceTypeId());
            predicates.add(predicate);
        }
        if (filter.getCustomerId() != null) {
            Predicate predicate = cb.equal(root.join("lastAppointment", JoinType.LEFT).get("customer"),
                    filter.getCustomerId());
            predicates.add(predicate);
        }
        if (filter.getStatus() != null) {
            if (filter.getStatus() == AppointmentStatusEnum.FREE) {
                Predicate predicate1 = root.join("lastAppointment", JoinType.LEFT).isNull();
                Predicate predicate2 = cb.equal(root.join("lastAppointment", JoinType.LEFT).get("currentStatus"),
                        AppointmentStatusEnum.CANCELLED);
                predicates.add(cb.or(predicate1, predicate2));

            } else {
                Predicate predicate = cb.equal(root.join("lastAppointment", JoinType.LEFT).get("currentStatus"), filter.getStatus());
                predicates.add(predicate);
            }
        }
        if (filter.getFrom() != null) {
            LocalDateTime startDate = LocalDateTime.of(filter.getFrom(), LocalTime.MIN);
            Predicate predicate = cb.greaterThanOrEqualTo(root.get("startDate"), startDate);
            predicates.add(predicate);
        }

        if (filter.getTo() != null) {
            LocalDateTime endDate = LocalDateTime.of(filter.getTo().plusDays(1), LocalTime.MIN);
            Predicate predicate = cb.lessThan(root.get("endDate"), endDate);
            predicates.add(predicate);
        }

        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Agenda> query = em.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public AgendaDTO findById(Long id) {
        Optional<Agenda> agenda = agendaRepository.findById(id);
        if (!agenda.isPresent()) {
            throw new RuntimeException("Agenda no encontrada - " + id);
        }

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(agenda.get(), AgendaDTO.class);
    }

    @Override
    public List<AgendaDTO> create(AgendaSaveDTO agendaSaveDTO) {
        ModelMapper modelMapper = new ModelMapper();

        Optional<Organization> organization = organizationRepository.findById(agendaSaveDTO.getOrganizationId());
        if (!organization.isPresent()) {
            throw new RuntimeException("La Agenda no tiene una Organización asociada válida.");
        }

        Optional<Resource> resource = resourceRepository.findById(agendaSaveDTO.getResource().getId());
        if (!resource.isPresent()) {
            throw new RuntimeException("Recurso no encontrado - " + agendaSaveDTO.getResource().getId());
        }

        List<Agenda> agendas = new ArrayList<>();
        LocalDateTime createdDate = LocalDateTime.now();

        List<LocalTime> hours = new ArrayList<>();
        while (agendaSaveDTO.getStartHour().isBefore(agendaSaveDTO.getEndHour())) {
            hours.add(agendaSaveDTO.getStartHour());
            LocalTime newTime = agendaSaveDTO.getStartHour().plusMinutes(agendaSaveDTO.getDuration());
            if (newTime.equals(LocalTime.MIDNIGHT)) {
                newTime = LocalTime.MAX;
            }
            agendaSaveDTO.setStartHour(newTime);
        }

        while (!agendaSaveDTO.getStartDate().isAfter(agendaSaveDTO.getEndDate())) {
            switch (agendaSaveDTO.getStartDate().getDayOfWeek()) {
                case SUNDAY:
                    if (agendaSaveDTO.getSunday()) {
                        createAgenda(agendaSaveDTO, hours, agendas, resource.get(), organization.get(), createdDate);
                    }
                    break;
                case MONDAY:
                    if (agendaSaveDTO.getMonday()) {
                        createAgenda(agendaSaveDTO, hours, agendas, resource.get(), organization.get(), createdDate);
                    }
                    break;
                case TUESDAY:
                    if (agendaSaveDTO.getTuesday()) {
                        createAgenda(agendaSaveDTO, hours, agendas, resource.get(), organization.get(), createdDate);
                    }
                    break;
                case WEDNESDAY:
                    if (agendaSaveDTO.getWednesday()) {
                        createAgenda(agendaSaveDTO, hours, agendas, resource.get(), organization.get(), createdDate);
                    }
                    break;
                case THURSDAY:
                    if (agendaSaveDTO.getThursday()) {
                        createAgenda(agendaSaveDTO, hours, agendas, resource.get(), organization.get(), createdDate);
                    }
                    break;
                case FRIDAY:
                    if (agendaSaveDTO.getFriday()) {
                        createAgenda(agendaSaveDTO, hours, agendas, resource.get(), organization.get(), createdDate);
                    }
                    break;
                case SATURDAY:
                    if (agendaSaveDTO.getSaturday()) {
                        createAgenda(agendaSaveDTO, hours, agendas, resource.get(), organization.get(), createdDate);
                    }
                    break;
            }
            agendaSaveDTO.setStartDate(agendaSaveDTO.getStartDate().plusDays(1));
        }

        try {
            agendaRepository.saveAll(agendas);
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
        return agendas.stream().map(a -> modelMapper.map(a, AgendaDTO.class)).collect(Collectors.toList());
    }

    private void createAgenda(AgendaSaveDTO agendaSaveDTO, List<LocalTime> hours, List<Agenda> agendaToSave,
            Resource resource, Organization organization, LocalDateTime createdDate) {
        for (LocalTime hour : hours) {
            LocalDateTime start = LocalDateTime.of(agendaSaveDTO.getStartDate(), hour);
            LocalDateTime end = start.plusMinutes(agendaSaveDTO.getDuration());
            agendaToSave.add(new Agenda(true, createdDate, organization, resource, start, end));
        }
    }

    @Override
    public void deleteById(Long id) {
        Optional<Agenda> agenda = agendaRepository.findById(id);
        if (!agenda.isPresent()) {
            throw new RuntimeException("Agenda no encontrada - " + id);
        }

        try {
            agendaRepository.delete(agenda.get());
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
    }
}