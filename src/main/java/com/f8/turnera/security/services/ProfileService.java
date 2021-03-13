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

import com.f8.turnera.entities.Organization;
import com.f8.turnera.security.entities.Permission;
import com.f8.turnera.security.entities.Profile;
import com.f8.turnera.security.models.PermissionDTO;
import com.f8.turnera.security.models.ProfileDTO;
import com.f8.turnera.security.models.ProfileFilterDTO;
import com.f8.turnera.security.repositories.IProfileRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService implements IProfileService {

    @Autowired
    private IProfileRepository profileRepository;

    @Autowired
    private com.f8.turnera.repositories.IOrganizationRepository organizationRepository;

    @Autowired
    private EntityManager em;

    @Override
    public List<ProfileDTO> findAllByFilter(ProfileFilterDTO filter) {
        ModelMapper modelMapper = new ModelMapper();

        List<Profile> profiles = findByCriteria(filter);
        profiles.sort(Comparator.comparing(Profile::getDescription));
        return profiles.stream().map(profile -> modelMapper.map(profile, ProfileDTO.class))
                .collect(Collectors.toList());
    }

    private List<Profile> findByCriteria(ProfileFilterDTO filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Profile> cq = cb.createQuery(Profile.class);

        List<Predicate> predicates = new ArrayList<>();

        Root<Profile> root = cq.from(Profile.class);
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

        TypedQuery<Profile> query = em.createQuery(cq);
        return query.getResultList();
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

        Optional<Organization> organization = organizationRepository.findById(profileDTO.getOrganizationId());
        if (!organization.isPresent()) {
            throw new RuntimeException("El Perfil no tiene una Organización asociada válida.");
        }

        Profile profile = modelMapper.map(profileDTO, Profile.class);
        profile.setCreatedDate(LocalDateTime.now());
        profile.setActive(true);
        profile.setOrganization(organization.get());
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
            && profile.get().getUsers().stream().filter(x -> x.getActive()).count() > 0) {
            throw new RuntimeException("Existen Usuarios activos con este Perfil asociado. Primero debe modificar los Usuarios para continuar.");
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
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
    }
}
