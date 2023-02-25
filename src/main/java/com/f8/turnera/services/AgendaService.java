package com.f8.turnera.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
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
import com.f8.turnera.models.RepeatTypeEnum;
import com.f8.turnera.repositories.IAgendaRepository;
import com.f8.turnera.repositories.IOrganizationRepository;
import com.f8.turnera.repositories.IResourceRepository;
import com.f8.turnera.util.Constants;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<AgendaDTO> findAllByFilter(AppointmentFilterDTO filter) {
        ModelMapper modelMapper = new ModelMapper();

        Page<Agenda> agendas = findByCriteria(filter);
        return agendas.map(agenda -> modelMapper.map(agenda, AgendaDTO.class));
    }

    private Page<Agenda> findByCriteria(AppointmentFilterDTO filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Agenda> cq = cb.createQuery(Agenda.class);

        List<Predicate> predicates = new ArrayList<>();

        Root<Agenda> root = cq.from(Agenda.class);
        if (filter.getResourceId() != null) {
            Predicate predicate = cb.equal(root.join("resource", JoinType.LEFT), filter.getResourceId());
            predicates.add(predicate);
        }
        if (filter.getResourceDescription() != null) {
            Predicate predicate = cb.like(cb.lower(root.join("resource", JoinType.LEFT).get("description")),
                    "%" + filter.getResourceDescription().toLowerCase() + "%");
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
        if (filter.getCustomerBusinessName() != null) {
            Predicate predicate = cb.like(
                    cb.lower(root.join("lastAppointment", JoinType.LEFT).get("customer").get("businessName")),
                    "%" + filter.getCustomerBusinessName().toLowerCase() + "%");
            predicates.add(predicate);
        }
        if (filter.getStatus() != null) {
            if (filter.getStatus() == AppointmentStatusEnum.FREE) {
                Predicate predicate1 = root.join("lastAppointment", JoinType.LEFT).isNull();
                Predicate predicate2 = cb.equal(root.join("lastAppointment", JoinType.LEFT)
                        .join("lastAppointmentStatus", JoinType.LEFT).get("status"), AppointmentStatusEnum.CANCELLED);
                predicates.add(cb.or(predicate1, predicate2));

            } else {
                Predicate predicate = cb.equal(root.join("lastAppointment", JoinType.LEFT)
                        .join("lastAppointmentStatus", JoinType.LEFT).get("status"), filter.getStatus());
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
            Predicate predicate = cb.lessThanOrEqualTo(root.get("endDate"), endDate);
            predicates.add(predicate);
        }
        if (filter.getActive() != null) {
            Predicate predicate = cb.equal(root.get("active"), filter.getActive());
            predicates.add(predicate);
        }

        cq.where(predicates.toArray(new Predicate[0]));

        List<Agenda> result = em.createQuery(cq).getResultList();
        if (filter.getSort() != null) {
            if (filter.getSort().get(0).equals("ASC")) {
                switch (filter.getSort().get(1)) {
                    case "startDate":
                        result.sort(Comparator.comparing(Agenda::getStartDate));
                        break;
                    case "endDate":
                        result.sort(Comparator.comparing(Agenda::getEndDate));
                        break;
                    case "resourceDescription":
                        result.sort(
                                Comparator.comparing(x -> x.getResource().getDescription(),
                                        String::compareToIgnoreCase));
                        break;
                    default:
                        break;
                }
            } else if (filter.getSort().get(0).equals("DESC")) {
                switch (filter.getSort().get(1)) {
                    case "startDate":
                        result.sort(Comparator.comparing(Agenda::getStartDate).reversed());
                        break;
                    case "endDate":
                        result.sort(Comparator.comparing(Agenda::getEndDate).reversed());
                        break;
                    case "resourceDescription":
                        result.sort(Comparator.comparing(x -> x.getResource().getDescription(),
                                Comparator.nullsFirst(String::compareToIgnoreCase).reversed()));
                        break;
                    default:
                        break;
                }
            }
        }
        int count = result.size();
        int fromIndex = Constants.ITEMS_PER_PAGE * (filter.getPage());
        int toIndex = fromIndex + Constants.ITEMS_PER_PAGE > count ? count : fromIndex + Constants.ITEMS_PER_PAGE;
        if (filter.getIgnorePaginated() != null && filter.getIgnorePaginated()) {
            fromIndex = 0;
            toIndex = count;
        }
        Pageable pageable = PageRequest.of(filter.getPage(), Constants.ITEMS_PER_PAGE);
        return new PageImpl<Agenda>(result.subList(fromIndex, toIndex), pageable, count);
    }

    @Override
    public AgendaDTO findById(Long id) {
        Optional<Agenda> agenda = agendaRepository.findById(id);
        if (!agenda.isPresent()) {
            throw new RuntimeException("Disponibilidad no encontrada - " + id);
        }

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(agenda.get(), AgendaDTO.class);
    }

    @Override
    public List<AgendaDTO> create(AgendaSaveDTO agendaSaveDTO) {
        ModelMapper modelMapper = new ModelMapper();

        // validations
        Optional<Organization> organization = organizationRepository.findById(agendaSaveDTO.getOrganizationId());
        if (!organization.isPresent()) {
            throw new RuntimeException("La disponibilidad no tiene una Organización asociada válida.");
        }
        Optional<Resource> resource = resourceRepository.findById(agendaSaveDTO.getResource().getId());
        if (!resource.isPresent()) {
            throw new RuntimeException("Recurso no encontrado - " + agendaSaveDTO.getResource().getId());
        }
        if (agendaSaveDTO.getStartDate().isAfter(agendaSaveDTO.getEndDate())) {
            throw new RuntimeException("La fecha de inicio deber ser menor o igual a la de fin.");
        }
        if (agendaSaveDTO.getStartDate().isEqual(agendaSaveDTO.getEndDate()) &&
                !agendaSaveDTO.getStartHour().isBefore(agendaSaveDTO.getEndHour())
                && !agendaSaveDTO.getEndHour().equals(LocalTime.MIDNIGHT)) {
            throw new RuntimeException("La hora de inicio deber ser menor a la de fin.");
        }
        Boolean isSegmented = agendaSaveDTO.getSegmented() != null && agendaSaveDTO.getSegmented();
        if (isSegmented && agendaSaveDTO.getDuration() != null
                && agendaSaveDTO.getDuration() < 5) {
            throw new RuntimeException("La duración debe ser mayor o igual a 5 minutos.");
        }
        if (agendaSaveDTO.getRepeat() != null && agendaSaveDTO.getRepeat()) {
            if (agendaSaveDTO.getRepeatType() == null) {
                throw new RuntimeException("Cada es requerido.");
            } else if (agendaSaveDTO.getFinalize() == null) {
                throw new RuntimeException("Finaliza es requerido.");
            } else if (agendaSaveDTO.getRepeatType().equals(RepeatTypeEnum.WEEKLY)
                    && (agendaSaveDTO.getSunday() == null || !agendaSaveDTO.getSunday())
                    && (agendaSaveDTO.getMonday() == null || !agendaSaveDTO.getMonday())
                    && (agendaSaveDTO.getTuesday() == null || !agendaSaveDTO.getTuesday())
                    && (agendaSaveDTO.getWednesday() == null || !agendaSaveDTO.getWednesday())
                    && (agendaSaveDTO.getThursday() == null || !agendaSaveDTO.getThursday())
                    && (agendaSaveDTO.getFriday() == null || !agendaSaveDTO.getFriday())
                    && (agendaSaveDTO.getSaturday() == null || !agendaSaveDTO.getSaturday())) {
                throw new RuntimeException("Debe seleccionar al menos un día de la semana.");
            } else if (agendaSaveDTO.getRepeatType() == RepeatTypeEnum.DAILY) {
                LocalDateTime start = LocalDateTime.of(agendaSaveDTO.getStartDate(), agendaSaveDTO.getStartHour());
                LocalDateTime end = LocalDateTime.of(agendaSaveDTO.getEndDate(), agendaSaveDTO.getEndHour());
                if (ChronoUnit.MINUTES.between(start, end) > 1440) {
                    throw new RuntimeException("La disponibilidad diaria no puede superar las 24 hs.");                    
                }
            }
        }

        // to validate after generating agendas
        AppointmentFilterDTO filter = new AppointmentFilterDTO();
        filter.setResourceId(agendaSaveDTO.getResource().getId());
        filter.setFrom(agendaSaveDTO.getStartDate());
        filter.setTo(agendaSaveDTO.getEndDate());
        filter.setActive(true);
        filter.setIgnorePaginated(true);

        List<Agenda> agendas = new ArrayList<>();
        LocalDateTime createdDate = LocalDateTime.now();

        if (agendaSaveDTO.getEndHour().equals(LocalTime.MIDNIGHT)) {
            agendaSaveDTO.setEndHour(LocalTime.MAX);
        }
        List<LocalTime> hours = new ArrayList<>();
        // while (agendaSaveDTO.getStartHour().isBefore(agendaSaveDTO.getEndHour())) {
        // hours.add(agendaSaveDTO.getStartHour());
        // LocalTime newTime =
        // agendaSaveDTO.getStartHour().plusMinutes(agendaSaveDTO.getDuration());
        // if (newTime.isBefore(agendaSaveDTO.getStartHour()) ||
        // newTime.equals(LocalTime.MIDNIGHT)) {
        // newTime = LocalTime.MAX;
        // }
        // agendaSaveDTO.setStartHour(newTime);
        // }

        if (agendaSaveDTO.getRepeat() != null && agendaSaveDTO.getRepeat()) {
            switch (agendaSaveDTO.getRepeatType()) {
                case DAILY:
                    if (isSegmented) {
                        LocalDateTime start = LocalDateTime.of(agendaSaveDTO.getStartDate(),
                                agendaSaveDTO.getStartHour());
                        LocalDateTime end = LocalDateTime.of(agendaSaveDTO.getEndDate(), agendaSaveDTO.getEndHour());
                        LocalDateTime finalize = LocalDateTime.of(agendaSaveDTO.getFinalize(), LocalTime.MIN);
                        Long daysCount = 0L;
                        while (!end.isAfter(finalize)) {
                            while (!start.isAfter(end)) {
                                // if the last agenda exceeds the end date
                                if (start.plusMinutes(agendaSaveDTO.getDuration()).isAfter(end)) {
                                    break;
                                }
                                agendas.add(new Agenda(true, createdDate, organization.get(), resource.get(), start, end));
                                start = start.plusMinutes(agendaSaveDTO.getDuration());
                            }
                            daysCount++;
                            start = LocalDateTime.of(agendaSaveDTO.getStartDate(), agendaSaveDTO.getStartHour()).plusDays(daysCount);
                            end = end.plusDays(1);
                        }
                    } else {
                        LocalDateTime start = LocalDateTime.of(agendaSaveDTO.getStartDate(),
                                agendaSaveDTO.getStartHour());
                        LocalDateTime end = LocalDateTime.of(agendaSaveDTO.getEndDate(), agendaSaveDTO.getEndHour());
                        LocalDateTime finalize = LocalDateTime.of(agendaSaveDTO.getFinalize(), LocalTime.MIN);
                        while (!end.isAfter(finalize)) {
                            agendas.add(new Agenda(true, createdDate, organization.get(), resource.get(), start, end));
                            start = start.plusDays(1);
                            end = end.plusDays(1);
                        }
                    }
                    break;
                case WEEKLY:
                    if (isSegmented) {
                    } else {
                    }
                    break;
                case MONTHLY:
                    if (isSegmented) {
                    } else {
                        LocalDateTime start = LocalDateTime.of(agendaSaveDTO.getStartDate(),
                                agendaSaveDTO.getStartHour());
                        LocalDateTime end = LocalDateTime.of(agendaSaveDTO.getEndDate(), agendaSaveDTO.getEndHour());
                        LocalDateTime finalize = LocalDateTime.of(agendaSaveDTO.getFinalize(), LocalTime.MIN);
                        Long monthsCount = 0L;
                        while (!end.isAfter(finalize)) {
                            agendas.add(new Agenda(true, createdDate, organization.get(), resource.get(), start, end));
                            monthsCount++;
                            start = LocalDateTime.of(agendaSaveDTO.getStartDate(), agendaSaveDTO.getStartHour()).plusMonths(monthsCount);
                            end = LocalDateTime.of(agendaSaveDTO.getEndDate(), agendaSaveDTO.getEndHour()).plusMonths(monthsCount);
                        }
                    }
                    break;
                default:
                    break;
            }
        } else {
            LocalDateTime start = LocalDateTime.of(agendaSaveDTO.getStartDate(), agendaSaveDTO.getStartHour());
            LocalDateTime end = LocalDateTime.of(agendaSaveDTO.getEndDate(), agendaSaveDTO.getEndHour());
            if (isSegmented) {
                while (!start.isAfter(end)) {
                    // if the last agenda exceeds the end date
                    if (start.plusMinutes(agendaSaveDTO.getDuration()).isAfter(end)) {
                        break;
                    }
                    agendas.add(new Agenda(true, createdDate, organization.get(), resource.get(), start, end));
                    start = start.plusMinutes(agendaSaveDTO.getDuration());
                }
            } else {
                agendas.add(new Agenda(true, createdDate, organization.get(), resource.get(), start, end));
            }
        }

        // validation after generating agendas
        Page<Agenda> existingAgendas = findByCriteria(filter);
        if (!existingAgendas.isEmpty()) {
            Boolean flag = false;
            for (Agenda newAgenda : agendas) {
                List<Agenda> overlappingAgenda = existingAgendas.stream()
                        .filter(oldAgenda -> ((newAgenda.getStartDate().isAfter(oldAgenda.getStartDate())
                                || newAgenda.getStartDate().equals(oldAgenda.getStartDate()))
                                && newAgenda.getStartDate().isBefore(oldAgenda.getEndDate()))
                                || ((newAgenda.getEndDate().isBefore(oldAgenda.getEndDate())
                                        || newAgenda.getEndDate().equals(oldAgenda.getEndDate()))
                                        && newAgenda.getEndDate().isAfter(oldAgenda.getStartDate()))
                                || ((newAgenda.getStartDate().isBefore(oldAgenda.getStartDate())
                                        || newAgenda.getStartDate().equals(oldAgenda.getStartDate()))
                                        && (newAgenda.getEndDate().isAfter(oldAgenda.getEndDate())
                                                || newAgenda.getEndDate().equals(oldAgenda.getEndDate()))))
                        .collect(Collectors.toList());
                if (!overlappingAgenda.isEmpty()) {
                    flag = true;
                }
            }

            if (flag) {
                throw new RuntimeException("Hay Disponibilidades existentes para el Recurso en el período dado.");
            }
        }

        try {
            agendaRepository.saveAll(agendas);
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
        return agendas.stream().map(a -> modelMapper.map(a, AgendaDTO.class)).collect(Collectors.toList());
    }

    private void createAgendasNoRepeatAndSegmented(AgendaSaveDTO agendaSaveDTO, List<LocalTime> hours,
            List<Agenda> agendas,
            Resource resource, Organization organization, LocalDateTime createdDate) {
        while (!agendaSaveDTO.getStartDate().isAfter(agendaSaveDTO.getEndDate())) {
            switch (agendaSaveDTO.getStartDate().getDayOfWeek()) {
                case SUNDAY:
                    if (agendaSaveDTO.getSunday() != null && agendaSaveDTO.getSunday()) {
                        createAgenda(agendaSaveDTO, hours, agendas, resource, organization, createdDate);
                    }
                    break;
                case MONDAY:
                    if (agendaSaveDTO.getMonday() != null && agendaSaveDTO.getMonday()) {
                        createAgenda(agendaSaveDTO, hours, agendas, resource, organization, createdDate);
                    }
                    break;
                case TUESDAY:
                    if (agendaSaveDTO.getTuesday() != null && agendaSaveDTO.getTuesday()) {
                        createAgenda(agendaSaveDTO, hours, agendas, resource, organization, createdDate);
                    }
                    break;
                case WEDNESDAY:
                    if (agendaSaveDTO.getWednesday() != null && agendaSaveDTO.getWednesday()) {
                        createAgenda(agendaSaveDTO, hours, agendas, resource, organization, createdDate);
                    }
                    break;
                case THURSDAY:
                    if (agendaSaveDTO.getThursday() != null && agendaSaveDTO.getThursday()) {
                        createAgenda(agendaSaveDTO, hours, agendas, resource, organization, createdDate);
                    }
                    break;
                case FRIDAY:
                    if (agendaSaveDTO.getFriday() != null && agendaSaveDTO.getFriday()) {
                        createAgenda(agendaSaveDTO, hours, agendas, resource, organization, createdDate);
                    }
                    break;
                case SATURDAY:
                    if (agendaSaveDTO.getSaturday() != null && agendaSaveDTO.getSaturday()) {
                        createAgenda(agendaSaveDTO, hours, agendas, resource, organization, createdDate);
                    }
                    break;
            }
            agendaSaveDTO.setStartDate(agendaSaveDTO.getStartDate().plusDays(1));
        }
    }

    private void createAgenda(AgendaSaveDTO agendaSaveDTO, List<LocalTime> hours, List<Agenda> agendaToSave,
            Resource resource, Organization organization, LocalDateTime createdDate) {
        for (LocalTime hour : hours) {
            LocalDateTime start = LocalDateTime.of(agendaSaveDTO.getStartDate(), hour);
            LocalDateTime end = start.plusMinutes(agendaSaveDTO.getDuration());
            if (!end.toLocalTime().isAfter(agendaSaveDTO.getEndHour())) {
                agendaToSave.add(new Agenda(true, createdDate, organization, resource, start, end));
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        Optional<Agenda> agenda = agendaRepository.findById(id);
        if (!agenda.isPresent()) {
            throw new RuntimeException("Disponibilidad no encontrada - " + id);
        }

        try {
            agendaRepository.delete(agenda.get());
        } catch (DataIntegrityViolationException dive) {
            throw new RuntimeException("No se puede eliminar la Disponibilidad porque tiene Turnos asociados.");
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
    }

    @Override
    public AgendaDTO desactivate(Long id) {
        Optional<Agenda> agenda = agendaRepository.findById(id);
        if (!agenda.isPresent()) {
            throw new RuntimeException("Disponibilidad no encontrada - " + id);
        }

        ModelMapper modelMapper = new ModelMapper();

        try {
            agenda.ifPresent(a -> {
                a.setActive(false);
                agendaRepository.save(a);
            });

        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
        return modelMapper.map(agenda.get(), AgendaDTO.class);
    }
}
