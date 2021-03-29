package com.f8.turnera.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.f8.turnera.entities.Customer;
import com.f8.turnera.entities.Organization;
import com.f8.turnera.models.CustomerDTO;
import com.f8.turnera.models.CustomerFilterDTO;
import com.f8.turnera.repositories.ICustomerRepository;
import com.f8.turnera.repositories.IOrganizationRepository;
import com.f8.turnera.util.Constants;
import com.f8.turnera.util.EmailValidation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private ICustomerRepository customerRepository;

    @Autowired
    private IOrganizationRepository organizationRepository;

    @Autowired
    private EntityManager em;

    @Override
    public Page<CustomerDTO> findAllByFilter(CustomerFilterDTO filter) {
        ModelMapper modelMapper = new ModelMapper();

        Page<Customer> customers = findByCriteria(filter);

        return customers.map(customer -> modelMapper.map(customer, CustomerDTO.class));
    }

    private Page<Customer> findByCriteria(CustomerFilterDTO filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);

        List<Predicate> predicates = new ArrayList<>();

        Root<Customer> root = cq.from(Customer.class);
        if (filter.getBusinessName() != null) {
            Predicate predicate = cb.like(cb.lower(root.get("businessName")),
                    "%" + filter.getBusinessName().toLowerCase() + "%");
            predicates.add(predicate);
        }
        if (filter.getBrandName() != null) {
            Predicate predicate = cb.like(cb.lower(root.get("brandName")),
                    "%" + filter.getBrandName().toLowerCase() + "%");
            predicates.add(predicate);
        }
        if (filter.getEmail() != null) {
            Predicate predicate = cb.like(cb.lower(root.get("email")), "%" + filter.getEmail().toLowerCase() + "%");
            predicates.add(predicate);
        }
        if (filter.getPhone1() != null) {
            Predicate predicate = cb.like(cb.lower(root.get("phone1")), "%" + filter.getPhone1().toLowerCase() + "%");
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

        List<Customer> result = em.createQuery(cq).getResultList();
        if (filter.getSort() != null) {
            if (filter.getSort().get(0).equals("ASC")) {
                switch (filter.getSort().get(1)) {
                case "businessName":
                    result.sort(Comparator.comparing(Customer::getBusinessName, String::compareToIgnoreCase));
                    break;
                case "brandName":
                    result.sort(Comparator.comparing(Customer::getBrandName,
                            Comparator.nullsFirst(String::compareToIgnoreCase)));
                    break;
                case "email":
                    result.sort(Comparator.comparing(Customer::getEmail, String::compareToIgnoreCase));
                    break;
                case "phone1":
                    result.sort(Comparator.comparing(Customer::getPhone1, String::compareToIgnoreCase));
                    break;
                default:
                    break;
                }
            } else {
                switch (filter.getSort().get(1)) {
                case "businessName":
                    result.sort(
                            Comparator.comparing(Customer::getBusinessName, String::compareToIgnoreCase).reversed());
                    break;
                case "brandName":
                    result.sort(Comparator
                            .comparing(Customer::getBrandName, Comparator.nullsFirst(String::compareToIgnoreCase))
                            .reversed());
                    break;
                case "email":
                    result.sort(Comparator.comparing(Customer::getEmail, String::compareToIgnoreCase).reversed());
                    break;
                case "phone1":
                    result.sort(Comparator.comparing(Customer::getPhone1, String::compareToIgnoreCase).reversed());
                    break;
                default:
                    break;
                }
            }
        }
        int count = result.size();
        int fromIndex = Constants.ITEMS_PER_PAGE * (filter.getPage());
        int toIndex = fromIndex + Constants.ITEMS_PER_PAGE > count ? count : fromIndex + Constants.ITEMS_PER_PAGE;
        Pageable pageable = PageRequest.of(filter.getPage(), Constants.ITEMS_PER_PAGE);
        return new PageImpl<Customer>(result.subList(fromIndex, toIndex), pageable, count);
    }

    @Override
    public CustomerDTO findById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (!customer.isPresent()) {
            throw new RuntimeException("Cliente no encontrado - " + id);
        }

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(customer.get(), CustomerDTO.class);
    }

    @Override
    public CustomerDTO create(CustomerDTO customerDTO) {
        ModelMapper modelMapper = new ModelMapper();

        Optional<Organization> organization = organizationRepository.findById(customerDTO.getOrganizationId());
        if (!organization.isPresent()) {
            throw new RuntimeException("El Cliente no tiene una Organización asociada válida.");
        }

        if (!EmailValidation.validateEmail(customerDTO.getEmail())) {
            throw new RuntimeException("El Correo Electrónico es inválido.");
        }

        Customer customer = modelMapper.map(customerDTO, Customer.class);
        customer.setCreatedDate(LocalDateTime.now());
        customer.setActive(true);
        customer.setOrganization(organization.get());

        try {
            customerRepository.save(customer);
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
        return modelMapper.map(customer, CustomerDTO.class);
    }

    @Override
    public CustomerDTO createQuick(CustomerDTO customerDTO, Organization organization) {
        if (!EmailValidation.validateEmail(customerDTO.getEmail())) {
            throw new RuntimeException("El Correo Electrónico es inválido.");
        }

        ModelMapper modelMapper = new ModelMapper();

        Customer customer = modelMapper.map(customerDTO, Customer.class);
        customer.setCreatedDate(LocalDateTime.now());
        customer.setActive(true);
        customer.setOrganization(organization);

        try {
            customerRepository.save(customer);
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
        return modelMapper.map(customer, CustomerDTO.class);
    }

    @Override
    public CustomerDTO update(CustomerDTO customerDTO) {
        Optional<Customer> customer = customerRepository.findById(customerDTO.getId());
        if (!customer.isPresent()) {
            throw new RuntimeException("Cliente no encontrado - " + customerDTO.getId());
        }

        if (!EmailValidation.validateEmail(customerDTO.getEmail())) {
            throw new RuntimeException("El Correo Electrónico es inválido.");
        }

        ModelMapper modelMapper = new ModelMapper();

        try {
            customer.ifPresent(c -> {
                c.setActive(customerDTO.getActive());
                c.setBusinessName(customerDTO.getBusinessName());
                c.setBrandName(customerDTO.getBrandName());
                c.setCuit(customerDTO.getCuit());
                c.setAddress(customerDTO.getAddress());
                c.setPhone1(customerDTO.getPhone1());
                c.setPhone2(customerDTO.getPhone2());
                c.setEmail(customerDTO.getEmail());

                customerRepository.save(c);
            });

        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
        return modelMapper.map(customer.get(), CustomerDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (!customer.isPresent()) {
            throw new RuntimeException("Cliente no encontrado - " + id);
        }

        try {
            customerRepository.delete(customer.get());
        } catch (DataIntegrityViolationException dive) {
            throw new RuntimeException("No se puede eliminar el Cliente porque tiene Turnos asociados.");
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
    }
}
