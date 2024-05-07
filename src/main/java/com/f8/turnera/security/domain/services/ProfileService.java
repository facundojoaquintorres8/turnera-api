package com.f8.turnera.security.domain.services;

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

import com.f8.turnera.domain.dtos.OrganizationDTO;
import com.f8.turnera.domain.entities.Organization;
import com.f8.turnera.domain.services.IOrganizationService;
import com.f8.turnera.security.domain.dtos.PermissionDTO;
import com.f8.turnera.security.domain.dtos.ProfileDTO;
import com.f8.turnera.security.domain.dtos.ProfileFilterDTO;
import com.f8.turnera.security.domain.entities.Permission;
import com.f8.turnera.security.domain.entities.Profile;
import com.f8.turnera.security.domain.entities.User;
import com.f8.turnera.security.domain.repositories.IProfileRepository;
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
public class ProfileService implements IProfileService {

    @Autowired
    private IProfileRepository profileRepository;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private EntityManager em;

    @Override
    public Page<ProfileDTO> findAllByFilter(ProfileFilterDTO filter) {
        ModelMapper modelMapper = new ModelMapper();

        Page<Profile> profiles = findByCriteria(filter);
        return profiles.map(profile -> modelMapper.map(profile, ProfileDTO.class));
    }

    private Page<Profile> findByCriteria(ProfileFilterDTO filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Profile> cq = cb.createQuery(Profile.class);

        List<Predicate> predicates = new ArrayList<>();

        Root<Profile> root = cq.from(Profile.class);
        if (filter.getDescription() != null) {
            Predicate predicate = cb.like(cb.lower(root.get("description")),
                    "%" + filter.getDescription().toLowerCase() + "%");
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

        List<Profile> result = em.createQuery(cq).getResultList();
        if (filter.getSort() != null) {
            if (filter.getSort().get(0).equals("ASC")) {
                switch (filter.getSort().get(1)) {
                case "description":
                    result.sort(Comparator.comparing(Profile::getDescription, String::compareToIgnoreCase));
                    break;
                default:
                    break;
                }
            } else if (filter.getSort().get(0).equals("DESC")) {
                switch (filter.getSort().get(1)) {
                case "description":
                    result.sort(Comparator.comparing(Profile::getDescription, String::compareToIgnoreCase).reversed());
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
        return new PageImpl<Profile>(result.subList(fromIndex, toIndex), pageable, count);
    }

    @Override
    public ProfileDTO findById(Long id) {
        Optional<Profile> profile = profileRepository.findById(id);
        if (!profile.isPresent()) {
            throw new RuntimeException("Perfil no encontrado - " + id);
        }

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(profile.get(), ProfileDTO.class);
    }

    @Override
    public ProfileDTO create(ProfileDTO profileDTO) {
        ModelMapper modelMapper = new ModelMapper();

        OrganizationDTO organization = organizationService.findById(profileDTO.getOrganizationId());

        Profile profile = modelMapper.map(profileDTO, Profile.class);
        profile.setCreatedDate(LocalDateTime.now());
        profile.setActive(true);
        profile.setOrganization(modelMapper.map(organization, Organization.class));
        profile.setPermissions(addPermissions(profileDTO, modelMapper));

        try {
            profileRepository.save(profile);
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
        return modelMapper.map(profile, ProfileDTO.class);
    }

    @Override
    public ProfileDTO update(ProfileDTO profileDTO) {
        Optional<Profile> profile = profileRepository.findById(profileDTO.getId());
        if (!profile.isPresent()) {
            throw new RuntimeException("Perfil no encontrado - " + profileDTO.getId());
        }

        if (profile.get().getActive() && !profileDTO.getActive()
                && profile.get().getUsers().stream().filter(User::getActive).count() > 0) {
            throw new RuntimeException(
                    "Existen Usuarios activos con este Perfil asociado. Primero debe modificar los Usuarios para continuar.");
        }

        ModelMapper modelMapper = new ModelMapper();

        try {
            profile.ifPresent(p -> {
                p.setActive(profileDTO.getActive());
                p.setDescription(profileDTO.getDescription());
                p.setPermissions(addPermissions(profileDTO, modelMapper));

                profileRepository.save(p);
            });

        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
        return modelMapper.map(profile.get(), ProfileDTO.class);
    }

    private Set<Permission> addPermissions(ProfileDTO profileDTO, ModelMapper modelMapper) {
        if (profileDTO.getPermissions().stream().filter(x -> x.getCode().equals("home.index")).count() == 0) {
            PermissionDTO permissionHomeIndex = permissionService.findByCode("home.index");
            profileDTO.getPermissions().add(permissionHomeIndex);
        }

        Set<Permission> newPermissions = new HashSet<>();
        for (PermissionDTO permission : profileDTO.getPermissions()) {
            newPermissions.add(modelMapper.map(permission, Permission.class));
        }
        return newPermissions;
    }

    @Override
    public void deleteById(Long id) {
        Optional<Profile> profile = profileRepository.findById(id);
        if (!profile.isPresent()) {
            throw new RuntimeException("Perfil no encontrado - " + id);
        }

        try {
            profileRepository.delete(profile.get());
        } catch (DataIntegrityViolationException dive) {
            throw new RuntimeException("No se puede eliminar el Perfil porque tiene Usuarios asociados.");
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
    }
}
