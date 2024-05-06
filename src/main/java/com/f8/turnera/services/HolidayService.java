package com.f8.turnera.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

import com.f8.turnera.entities.Organization;
import com.f8.turnera.entities.Holiday;
import com.f8.turnera.models.HolidayDTO;
import com.f8.turnera.models.HolidayFilterDTO;
import com.f8.turnera.models.OrganizationDTO;
import com.f8.turnera.repositories.IHolidayRepository;
import com.f8.turnera.util.Constants;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HolidayService implements IHolidayService {

    @Autowired
    private IHolidayRepository holidayRepository;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private EntityManager em;

    @Override
    public Page<HolidayDTO> findAllByFilter(HolidayFilterDTO filter) {
        ModelMapper modelMapper = new ModelMapper();

        Page<Holiday> holidays = findByCriteria(filter);
        return holidays.map(holiday -> modelMapper.map(holiday, HolidayDTO.class));
    }

    private Page<Holiday> findByCriteria(HolidayFilterDTO filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Holiday> cq = cb.createQuery(Holiday.class);

        List<Predicate> predicates = new ArrayList<>();

        Root<Holiday> root = cq.from(Holiday.class);
        if (filter.getDate() != null) {
            Predicate predicate = cb.equal(root.get("date"), filter.getDate());
            predicates.add(predicate);
        }
        if (filter.getDescription() != null) {
            Predicate predicate = cb.like(cb.lower(root.get("description")),
                    "%" + filter.getDescription().toLowerCase() + "%");
            predicates.add(predicate);
        }
        if (filter.getUseInAgenda() != null) {
            Predicate predicate = cb.equal(root.get("useInAgenda"), filter.getUseInAgenda());
            predicates.add(predicate);
        }
        if (filter.getOrganizationId() != null) {
            Predicate predicate = cb.equal(root.join("organization", JoinType.LEFT), filter.getOrganizationId());
            predicates.add(predicate);
        }
        if (filter.getActive() != null) {
            Predicate predicate = cb.equal(root.get("active"), filter.getActive());
            predicates.add(predicate);
        }

        cq.where(predicates.toArray(new Predicate[0]));

        List<Holiday> result = em.createQuery(cq).getResultList();
        if (filter.getSort() != null) {
            if (filter.getSort().get(0).equals("ASC")) {
                switch (filter.getSort().get(1)) {
                    case "date":
                        result.sort(Comparator.comparing(Holiday::getDate));
                        break;
                    case "description":
                        result.sort(Comparator.comparing(Holiday::getDescription, String::compareToIgnoreCase));
                        break;
                    case "useInAgenda":
                        result.sort(Comparator.comparing(Holiday::getUseInAgenda));
                        break;
                    default:
                        break;
                }
            } else if (filter.getSort().get(0).equals("DESC")) {
                switch (filter.getSort().get(1)) {
                    case "date":
                        result.sort(Comparator.comparing(Holiday::getDate).reversed());
                        break;
                    case "description":
                        result.sort(
                                Comparator.comparing(Holiday::getDescription, String::compareToIgnoreCase).reversed());
                        break;
                    case "useInAgenda":
                        result.sort(Comparator.comparing(Holiday::getUseInAgenda).reversed());
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
        return new PageImpl<Holiday>(result.subList(fromIndex, toIndex), pageable, count);
    }

    @Override
    public HolidayDTO findById(Long id) {
        Optional<Holiday> holiday = holidayRepository.findById(id);
        if (!holiday.isPresent()) {
            throw new RuntimeException("Feriado no encontrado - " + id);
        }

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(holiday.get(), HolidayDTO.class);
    }

    @Override
    public HolidayDTO create(HolidayDTO holidayDTO) {
        ModelMapper modelMapper = new ModelMapper();

        OrganizationDTO organization = organizationService.findById(holidayDTO.getOrganizationId());

        Optional<Holiday> existingHoliday = holidayRepository.findByDateAndOrganizationId(holidayDTO.getDate(), holidayDTO.getOrganizationId());
        if (existingHoliday.isPresent()) {
            throw new RuntimeException("El feriado ingresado ya está registrado.");
        }

        Holiday holiday = modelMapper.map(holidayDTO, Holiday.class);
        holiday.setCreatedDate(LocalDateTime.now());
        holiday.setActive(true);
        holiday.setOrganization(modelMapper.map(organization, Organization.class));

        try {
            holidayRepository.save(holiday);
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
        return modelMapper.map(holiday, HolidayDTO.class);
    }

    @Override
    public HolidayDTO update(HolidayDTO holidayDTO) {
        Optional<Holiday> holiday = holidayRepository.findById(holidayDTO.getId());
        if (!holiday.isPresent()) {
            throw new RuntimeException("Feriado no encontrado - " + holidayDTO.getId());
        }

        Optional<Holiday> existingHoliday = holidayRepository.findByDateAndOrganizationId(holidayDTO.getDate(), holidayDTO.getOrganizationId());
        if (existingHoliday.isPresent() && !existingHoliday.get().getId().equals(holidayDTO.getId())) {
            throw new RuntimeException("El feriado ingresado ya está registrado.");
        }

        ModelMapper modelMapper = new ModelMapper();

        try {
            holiday.ifPresent(r -> {
                r.setActive(holidayDTO.getActive());
                r.setDate(holidayDTO.getDate());
                r.setDescription(holidayDTO.getDescription());
                r.setUseInAgenda(holidayDTO.getUseInAgenda());

                holidayRepository.save(r);
            });

        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
        return modelMapper.map(holiday.get(), HolidayDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Holiday> holiday = holidayRepository.findById(id);
        if (!holiday.isPresent()) {
            throw new RuntimeException("Feriado no encontrado - " + id);
        }

        try {
            holidayRepository.delete(holiday.get());
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
    }

    @Override
    public List<LocalDate> findAllDatesToAgenda() {
        HolidayFilterDTO filter = new HolidayFilterDTO();
        filter.setActive(true);
        filter.setUseInAgenda(true);
        filter.setIgnorePaginated(true);

        Page<Holiday> holidays = findByCriteria(filter);

        if (!holidays.isEmpty()) {
            return holidays.stream().map(Holiday::getDate).collect(Collectors.toList());
        }
        return new ArrayList<LocalDate>();
    }
}
