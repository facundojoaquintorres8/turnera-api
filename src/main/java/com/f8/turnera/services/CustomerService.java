package com.f8.turnera.services;

import java.time.LocalDateTime;
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

import com.f8.turnera.entities.Customer;
import com.f8.turnera.entities.Organization;
import com.f8.turnera.models.CustomerDTO;
import com.f8.turnera.models.CustomerFilterDTO;
import com.f8.turnera.repositories.ICustomerRepository;
import com.f8.turnera.repositories.IOrganizationRepository;
import com.f8.turnera.util.EmailValidation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    public List<CustomerDTO> findAllByFilter(CustomerFilterDTO filter) {
        ModelMapper modelMapper = new ModelMapper();

        List<Customer> customers = findByCriteria(filter);
        customers.sort(Comparator.comparing(Customer::getBusinessName));
        return customers.stream().map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
    }

    private List<Customer> findByCriteria(CustomerFilterDTO filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);

        List<Predicate> predicates = new ArrayList<>();

        Root<Customer> root = cq.from(Customer.class);
        if (filter.getOrganizationId() != null) {
            Predicate predicate = cb.equal(root.join("organization", JoinType.LEFT),
                    filter.getOrganizationId());
            predicates.add(predicate);
        }
        if (filter.getActive() != null) {
            Predicate predicate = cb.equal(root.get("active"), filter.getActive());
            predicates.add(predicate);
        }

        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Customer> query = em.createQuery(cq);
        return query.getResultList();
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
            throw new RuntimeException("No se puede borrar el Cliente porque tiene Turnos asociados.");
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
    }
}
