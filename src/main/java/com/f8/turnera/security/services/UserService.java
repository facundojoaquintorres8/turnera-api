package com.f8.turnera.security.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.f8.turnera.config.TokenProvider;
import com.f8.turnera.email.IEmailService;
import com.f8.turnera.entities.Organization;
import com.f8.turnera.repositories.IOrganizationRepository;
import com.f8.turnera.security.entities.Profile;
import com.f8.turnera.security.entities.User;
import com.f8.turnera.security.models.ProfileDTO;
import com.f8.turnera.security.models.UserDTO;
import com.f8.turnera.security.models.UserFilterDTO;
import com.f8.turnera.security.repositories.IUserRepository;
import com.f8.turnera.util.EmailValidation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.bytebuddy.utility.RandomString;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IOrganizationRepository organizationRepository;

    @Autowired
    private EntityManager em;
    
    @Autowired
    private IEmailService emailService;

    @Override
    public List<UserDTO> findAllByFilter(UserFilterDTO filter) {
        ModelMapper modelMapper = new ModelMapper();

        List<User> users = findByCriteria(filter);
        users.sort(Comparator.comparing(User::getLastName));
        return users.stream().map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    private List<User> findByCriteria(UserFilterDTO filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);

        List<Predicate> predicates = new ArrayList<>();

        Root<User> root = cq.from(User.class);
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

        TypedQuery<User> query = em.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public UserDTO findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new RuntimeException("Usuario no encontrado - " + id);
        }

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(user.get(), UserDTO.class);
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        ModelMapper modelMapper = new ModelMapper();

        Optional<Organization> organization = organizationRepository.findById(userDTO.getOrganizationId());
        if (!organization.isPresent()) {
            throw new RuntimeException("El Usuario no tiene una Organización asociada válida.");
        }

        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("El Correo Electrónico ingresado ya está registrado. Por favor ingrese otro.");
        }

        if (!EmailValidation.validateEmail(userDTO.getUsername())) {
            throw new RuntimeException("El Correo Electrónico es inválido.");
        }

        User user = modelMapper.map(userDTO, User.class);
        user.setCreatedDate(LocalDateTime.now());
        user.setActive(false);
        user.setOrganization(organization.get());
        user.setIsAdmin(false);
        user.setActivationKey(RandomString.make(20));
        user.setPassword(RandomString.make(40));
        user.setProfiles(addPermissions(userDTO, modelMapper));

        emailService.sendAccountActivationEmail(user);

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        Optional<User> user = userRepository.findById(userDTO.getId());
        if (!user.isPresent()) {
            throw new RuntimeException("Usuario no encontrado - " + userDTO.getId());
        }

        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userDTO.getId())) {
            throw new RuntimeException("El Correo Electrónico ingresado ya está registrado. Por favor ingrese otro.");
        }

        if (!EmailValidation.validateEmail(userDTO.getUsername())) {
            throw new RuntimeException("El Correo Electrónico es inválido.");
        }

        ModelMapper modelMapper = new ModelMapper();

        try {
            user.ifPresent(u -> {
                u.setUsername(userDTO.getUsername());
                u.setActive(userDTO.getActive());
                u.setFirstName(userDTO.getFirstName());
                u.setLastName(userDTO.getLastName());
                u.setProfiles(addPermissions(userDTO, modelMapper));

                userRepository.save(u);
            });

        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
        return modelMapper.map(user.get(), UserDTO.class);
    }

    private Set<Profile> addPermissions(UserDTO userDTO, ModelMapper modelMapper) {
        Set<Profile> newProfiles = new HashSet<>();
        for (ProfileDTO profile : userDTO.getProfiles()) {
                newProfiles.add(modelMapper.map(profile, Profile.class));
        }
        return newProfiles;
    }

    @Override
    public void deleteById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new RuntimeException("Usuario no encontrado - " + id);
        }

        if (TokenProvider.getUsernameWithoutToken().equals(user.get().getUsername())) {
            throw new RuntimeException("No puede borrar su propio Usuario.");
        }

        try {
            userRepository.delete(user.get());
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
    }
}
