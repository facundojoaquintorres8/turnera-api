package com.f8.turnera.security.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
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
import com.f8.turnera.util.Constants;
import com.f8.turnera.util.EmailValidation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<UserDTO> findAllByFilter(UserFilterDTO filter) {
        ModelMapper modelMapper = new ModelMapper();

        Page<User> users = findByCriteria(filter);
        return users.map(user -> modelMapper.map(user, UserDTO.class));
    }

    private Page<User> findByCriteria(UserFilterDTO filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);

        List<Predicate> predicates = new ArrayList<>();

        Root<User> root = cq.from(User.class);
        if (filter.getFirstName() != null) {
            Predicate predicate = cb.like(cb.lower(root.get("firstName")),
                    "%" + filter.getFirstName().toLowerCase() + "%");
            predicates.add(predicate);
        }
        if (filter.getLastName() != null) {
            Predicate predicate = cb.like(cb.lower(root.get("lastName")),
                    "%" + filter.getLastName().toLowerCase() + "%");
            predicates.add(predicate);
        }
        if (filter.getUsername() != null) {
            Predicate predicate = cb.like(cb.lower(root.get("username")),
                    "%" + filter.getUsername().toLowerCase() + "%");
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

        List<User> result = em.createQuery(cq).getResultList();
        if (filter.getSort() != null) {
            if (filter.getSort().get(0).equals("ASC")) {
                switch (filter.getSort().get(1)) {
                case "firstName":
                    result.sort(Comparator.comparing(User::getFirstName, String::compareToIgnoreCase));
                    break;
                case "lastName":
                    result.sort(Comparator.comparing(User::getLastName, String::compareToIgnoreCase));
                    break;
                case "username":
                    result.sort(Comparator.comparing(User::getUsername, String::compareToIgnoreCase));
                    break;
                default:
                    break;
                }
            } else if (filter.getSort().get(0).equals("DESC")) {
                switch (filter.getSort().get(1)) {
                case "firstName":
                    result.sort(Comparator.comparing(User::getFirstName, String::compareToIgnoreCase).reversed());
                    break;
                case "lastName":
                    result.sort(Comparator.comparing(User::getLastName, String::compareToIgnoreCase).reversed());
                    break;
                case "username":
                    result.sort(Comparator.comparing(User::getUsername, String::compareToIgnoreCase).reversed());
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
        return new PageImpl<User>(result.subList(fromIndex, toIndex), pageable, count);
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
            throw new RuntimeException("No puede eliminar su propio Usuario.");
        }

        try {
            userRepository.delete(user.get());
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
    }
}
