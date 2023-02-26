package com.f8.turnera.services;

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
            throw new RuntimeException("La Disponibilidad no tiene una Organización asociada válida.");
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

        LocalDateTime tempStart = LocalDateTime.of(agendaSaveDTO.getStartDate(), agendaSaveDTO.getStartHour());
        LocalDateTime segmentSetEndDate = LocalDateTime.of(agendaSaveDTO.getEndDate(), agendaSaveDTO.getEndHour());
        if (agendaSaveDTO.getRepeat() != null && agendaSaveDTO.getRepeat()) {
            LocalDateTime finalize = LocalDateTime.of(agendaSaveDTO.getFinalize(), LocalTime.MIN);
            switch (agendaSaveDTO.getRepeatType()) {
                case DAILY:
                    if (isSegmented) {
                        Long daysCount = 0L;
                        while (!segmentSetEndDate.isAfter(finalize)) {
                            agendas.addAll(createAgendasSegmented(resource.get(), organization.get(), createdDate,
                                tempStart, segmentSetEndDate, agendaSaveDTO.getDuration()));
                            daysCount++;
                            tempStart = LocalDateTime.of(agendaSaveDTO.getStartDate(), agendaSaveDTO.getStartHour()).plusDays(daysCount);
                            segmentSetEndDate = LocalDateTime.of(agendaSaveDTO.getEndDate(), agendaSaveDTO.getEndHour()).plusDays(daysCount);
                        }
                    } else {
                        LocalDateTime tempEnd = segmentSetEndDate;
                        while (!tempEnd.isAfter(finalize)) {
                            agendas.add(new Agenda(createdDate, organization.get(), resource.get(), tempStart, tempEnd));
                            tempStart = tempStart.plusDays(1);
                            tempEnd = tempEnd.plusDays(1);
                        }
                    }
                    break;
                case WEEKLY:
                    if (isSegmented) {
                        createAgendasSegmentedWeekly(agendaSaveDTO, agendas, resource.get(), organization.get(), createdDate);
                    } else {
                        createAgendasWeekly(agendaSaveDTO, agendas, resource.get(), organization.get(), createdDate);
                    }
                    break;
                case MONTHLY:
                    Long monthsCount = 0L;
                    if (isSegmented) {
                        while (!segmentSetEndDate.isAfter(finalize)) {
                            agendas.addAll(createAgendasSegmented(resource.get(), organization.get(), createdDate,
                                tempStart, segmentSetEndDate, agendaSaveDTO.getDuration()));
                            monthsCount++;
                            tempStart = LocalDateTime.of(agendaSaveDTO.getStartDate(), agendaSaveDTO.getStartHour()).plusMonths(monthsCount);
                            segmentSetEndDate = LocalDateTime.of(agendaSaveDTO.getEndDate(), agendaSaveDTO.getEndHour()).plusMonths(monthsCount);
                        }
                    } else {
                        LocalDateTime tempEnd = segmentSetEndDate;
                        while (!tempEnd.isAfter(finalize)) {
                            agendas.add(new Agenda(createdDate, organization.get(), resource.get(), tempStart, tempEnd));
                            monthsCount++;
                            tempStart = LocalDateTime.of(agendaSaveDTO.getStartDate(), agendaSaveDTO.getStartHour()).plusMonths(monthsCount);
                            tempEnd = segmentSetEndDate.plusMonths(monthsCount);
                        }
                    }
                    break;
                default:
                    break;
            }
        } else {
            if (isSegmented) {
                agendas = createAgendasSegmented(resource.get(), organization.get(), createdDate, 
                    tempStart, segmentSetEndDate, agendaSaveDTO.getDuration());
            } else {
                agendas.add(new Agenda(createdDate, organization.get(), resource.get(), tempStart, segmentSetEndDate));
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

    private List<Agenda> createAgendasWeekly(AgendaSaveDTO agendaSaveDTO,
            List<Agenda> agendas, Resource resource, Organization organization, LocalDateTime createdDate) {
        while (!agendaSaveDTO.getEndDate().isAfter(agendaSaveDTO.getFinalize())) {
            LocalDateTime start = LocalDateTime.of(agendaSaveDTO.getStartDate(), agendaSaveDTO.getStartHour());
            LocalDateTime end = LocalDateTime.of(agendaSaveDTO.getEndDate(), agendaSaveDTO.getEndHour());
            switch (agendaSaveDTO.getStartDate().getDayOfWeek()) {
                case SUNDAY:
                    if (agendaSaveDTO.getSunday() != null && agendaSaveDTO.getSunday()) {
                        agendas.add(new Agenda(createdDate, organization, resource, start, end));
                    }
                    break;
                case MONDAY:
                    if (agendaSaveDTO.getMonday() != null && agendaSaveDTO.getMonday()) {
                        agendas.add(new Agenda(createdDate, organization, resource, start, end));
                    }
                    break;
                case TUESDAY:
                    if (agendaSaveDTO.getTuesday() != null && agendaSaveDTO.getTuesday()) {
                        agendas.add(new Agenda(createdDate, organization, resource, start, end));
                    }
                    break;
                case WEDNESDAY:
                    if (agendaSaveDTO.getWednesday() != null && agendaSaveDTO.getWednesday()) {
                        agendas.add(new Agenda(createdDate, organization, resource, start, end));
                    }
                    break;
                case THURSDAY:
                    if (agendaSaveDTO.getThursday() != null && agendaSaveDTO.getThursday()) {
                        agendas.add(new Agenda(createdDate, organization, resource, start, end));
                    }
                    break;
                case FRIDAY:
                    if (agendaSaveDTO.getFriday() != null && agendaSaveDTO.getFriday()) {
                        agendas.add(new Agenda(createdDate, organization, resource, start, end));
                    }
                    break;
                case SATURDAY:
                    if (agendaSaveDTO.getSaturday() != null && agendaSaveDTO.getSaturday()) {
                        agendas.add(new Agenda(createdDate, organization, resource, start, end));
                    }
                    break;
            }
            agendaSaveDTO.setStartDate(agendaSaveDTO.getStartDate().plusDays(1));
            agendaSaveDTO.setEndDate(agendaSaveDTO.getEndDate().plusDays(1));
        }
        return agendas;
    }

    private List<Agenda> createAgendasSegmentedWeekly(AgendaSaveDTO agendaSaveDTO,
            List<Agenda> agendas, Resource resource, Organization organization, LocalDateTime createdDate) {
        LocalDateTime segmentSetEndDate = LocalDateTime.of(agendaSaveDTO.getEndDate(), agendaSaveDTO.getEndHour());
        LocalDateTime tempStart = LocalDateTime.of(agendaSaveDTO.getStartDate(), agendaSaveDTO.getStartHour());
        LocalDateTime finalize = LocalDateTime.of(agendaSaveDTO.getFinalize(), LocalTime.MIN);
        Long daysCount = 0L;
        while (!segmentSetEndDate.isAfter(finalize)) {
            switch (tempStart.getDayOfWeek()) {
                case SUNDAY:
                    if (agendaSaveDTO.getSunday() != null && agendaSaveDTO.getSunday()) {
                        agendas.addAll(createAgendasSegmented(resource, organization, createdDate, tempStart,
                                segmentSetEndDate, agendaSaveDTO.getDuration()));
                    }
                    break;
                case MONDAY:
                    if (agendaSaveDTO.getMonday() != null && agendaSaveDTO.getMonday()) {
                        agendas.addAll(createAgendasSegmented(resource, organization, createdDate, tempStart,
                                segmentSetEndDate, agendaSaveDTO.getDuration()));
                    }
                    break;
                case TUESDAY:
                    if (agendaSaveDTO.getTuesday() != null && agendaSaveDTO.getTuesday()) {
                        agendas.addAll(createAgendasSegmented(resource, organization, createdDate, tempStart,
                                segmentSetEndDate, agendaSaveDTO.getDuration()));
                    }
                    break;
                case WEDNESDAY:
                    if (agendaSaveDTO.getWednesday() != null && agendaSaveDTO.getWednesday()) {
                        agendas.addAll(createAgendasSegmented(resource, organization, createdDate, tempStart,
                                segmentSetEndDate, agendaSaveDTO.getDuration()));
                    }
                    break;
                case THURSDAY:
                    if (agendaSaveDTO.getThursday() != null && agendaSaveDTO.getThursday()) {
                        agendas.addAll(createAgendasSegmented(resource, organization, createdDate, tempStart,
                                segmentSetEndDate, agendaSaveDTO.getDuration()));
                    }
                    break;
                case FRIDAY:
                    if (agendaSaveDTO.getFriday() != null && agendaSaveDTO.getFriday()) {
                        agendas.addAll(createAgendasSegmented(resource, organization, createdDate, tempStart,
                                segmentSetEndDate, agendaSaveDTO.getDuration()));
                    }
                    break;
                case SATURDAY:
                    if (agendaSaveDTO.getSaturday() != null && agendaSaveDTO.getSaturday()) {
                        agendas.addAll(createAgendasSegmented(resource, organization, createdDate, tempStart,
                                segmentSetEndDate, agendaSaveDTO.getDuration()));
                    }
                    break;
            }
            daysCount++;
            tempStart = LocalDateTime.of(agendaSaveDTO.getStartDate(), agendaSaveDTO.getStartHour())
                    .plusDays(daysCount);
            segmentSetEndDate = LocalDateTime.of(agendaSaveDTO.getEndDate(), agendaSaveDTO.getEndHour())
                    .plusDays(daysCount);
        }
        return agendas;
    }

    private List<Agenda> createAgendasSegmented(Resource resource, Organization organization, LocalDateTime createdDate,
            LocalDateTime tempStart, LocalDateTime segmentSetEndDate, Long duration) {
        List<Agenda> agendas = new ArrayList<>();
        LocalDateTime tempEnd = tempStart.plusMinutes(duration);
        while (!tempEnd.isAfter(segmentSetEndDate)) {
            agendas.add(new Agenda(createdDate, organization, resource, tempStart, tempEnd));
            tempStart = tempEnd;
            tempEnd = tempEnd.plusMinutes(duration);
        }
        return agendas;
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
