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

import com.f8.turnera.entities.Organization;
import com.f8.turnera.entities.ResourceType;
import com.f8.turnera.models.ResourceTypeDTO;
import com.f8.turnera.models.ResourceTypeFilterDTO;
import com.f8.turnera.repositories.IOrganizationRepository;
import com.f8.turnera.repositories.IResourceTypeRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class ResourceTypeService implements IResourceTypeService {

    @Autowired
    private IResourceTypeRepository resourceTypeRepository;

    @Autowired
    private IOrganizationRepository organizationRepository;

    @Autowired
    private EntityManager em;

    @Override
    public List<ResourceTypeDTO> findAllByFilter(ResourceTypeFilterDTO filter) {
        ModelMapper modelMapper = new ModelMapper();

        List<ResourceType> resourcesTypes = findByCriteria(filter);
        resourcesTypes.sort(Comparator.comparing(ResourceType::getDescription));
        return resourcesTypes.stream().map(resourceType -> modelMapper.map(resourceType, ResourceTypeDTO.class))
                .collect(Collectors.toList());
    }

    private List<ResourceType> findByCriteria(ResourceTypeFilterDTO filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ResourceType> cq = cb.createQuery(ResourceType.class);

        List<Predicate> predicates = new ArrayList<>();

        Root<ResourceType> root = cq.from(ResourceType.class);
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

        TypedQuery<ResourceType> query = em.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public ResourceTypeDTO findById(Long id) {
        Optional<ResourceType> resourceType = resourceTypeRepository.findById(id);
        if (!resourceType.isPresent()) {
            throw new RuntimeException("Tipo de Recurso no encontrado - " + id);
        }

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(resourceType.get(), ResourceTypeDTO.class);
    }

    @Override
    public ResourceTypeDTO create(ResourceTypeDTO resourceTypeDTO) {
        ModelMapper modelMapper = new ModelMapper();

        Optional<Organization> organization = organizationRepository.findById(resourceTypeDTO.getOrganizationId());
        if (!organization.isPresent()) {
            throw new RuntimeException("El Tipo de Recurso no tiene una Organización asociada válida.");
        }

        ResourceType resourceType = modelMapper.map(resourceTypeDTO, ResourceType.class);
        resourceType.setCreatedDate(LocalDateTime.now());
        resourceType.setActive(true);
        resourceType.setOrganization(organization.get());

        try {
            resourceTypeRepository.save(resourceType);
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
        return modelMapper.map(resourceType, ResourceTypeDTO.class);
    }

    @Override
    public ResourceTypeDTO update(ResourceTypeDTO resourceTypeDTO) {
        Optional<ResourceType> resourceType = resourceTypeRepository.findById(resourceTypeDTO.getId());
        if (!resourceType.isPresent()) {
            throw new RuntimeException("Tipo de Recurso no encontrado - " + resourceTypeDTO.getId());
        }

        if (resourceType.get().getActive() && !resourceTypeDTO.getActive() 
            && resourceType.get().getResources().stream().filter(x -> x.getActive()).count() > 0) {
            throw new RuntimeException("Existen Recursos activos con este Tipo de Recurso asociado. Primero debe modificar los Recursos para continuar.");
        }

        ModelMapper modelMapper = new ModelMapper();

        try {
            resourceType.ifPresent(rt -> {
                rt.setActive(resourceTypeDTO.getActive());
                rt.setDescription(resourceTypeDTO.getDescription());

                resourceTypeRepository.save(rt);
            });

        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
        return modelMapper.map(resourceType.get(), ResourceTypeDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        Optional<ResourceType> resourceType = resourceTypeRepository.findById(id);
        if (!resourceType.isPresent()) {
            throw new RuntimeException("Tipo de Recurso no encontrado - " + id);
        }

        try {
            resourceTypeRepository.delete(resourceType.get());
        } catch (DataIntegrityViolationException dive) {
            throw new RuntimeException("No se puede borrar el Tipo de Recurso porque tiene Recursos asociados.");
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
    }
}
