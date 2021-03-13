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
import com.f8.turnera.entities.Resource;
import com.f8.turnera.entities.ResourceType;
import com.f8.turnera.models.ResourceDTO;
import com.f8.turnera.models.ResourceFilterDTO;
import com.f8.turnera.repositories.IOrganizationRepository;
import com.f8.turnera.repositories.IResourceRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceService implements IResourceService {

    @Autowired
    private IResourceRepository resourceRepository;

    @Autowired
    private IOrganizationRepository organizationRepository;

    @Autowired
    private EntityManager em;

    @Override
    public List<ResourceDTO> findAllByFilter(ResourceFilterDTO filter) {
        ModelMapper modelMapper = new ModelMapper();

        List<Resource> resources = findByCriteria(filter);
        resources.sort(Comparator.comparing(Resource::getDescription));
        return resources.stream().map(resource -> modelMapper.map(resource, ResourceDTO.class))
                .collect(Collectors.toList());
    }

    private List<Resource> findByCriteria(ResourceFilterDTO filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Resource> cq = cb.createQuery(Resource.class);

        List<Predicate> predicates = new ArrayList<>();

        Root<Resource> root = cq.from(Resource.class);
        if (filter.getOrganizationId() != null) {
            Predicate predicate = cb.equal(root.join("organization", JoinType.LEFT),
                    filter.getOrganizationId());
            predicates.add(predicate);
        }
        if (filter.getResourceTypeId() != null) {
            Predicate predicate = cb.equal(root.join("resourceType", JoinType.LEFT),
                    filter.getResourceTypeId());
            predicates.add(predicate);
        }
        if (filter.getActive() != null) {
            Predicate predicate = cb.equal(root.get("active"), filter.getActive());
            predicates.add(predicate);
        }

        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Resource> query = em.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public ResourceDTO findById(Long id) {
        Optional<Resource> resource = resourceRepository.findById(id);
        if (!resource.isPresent()) {
            throw new RuntimeException("Recurso no encontrado - " + id);
        }

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(resource.get(), ResourceDTO.class);
    }

    @Override
    public ResourceDTO create(ResourceDTO resourceDTO) {
        ModelMapper modelMapper = new ModelMapper();

        Optional<Organization> organization = organizationRepository.findById(resourceDTO.getOrganizationId());
        if (!organization.isPresent()) {
            throw new RuntimeException("El Recurso no tiene una Organización asociada válida.");
        }

        Resource resource = modelMapper.map(resourceDTO, Resource.class);
        resource.setCreatedDate(LocalDateTime.now());
        resource.setActive(true);
        resource.setOrganization(organization.get());
        resource.setResourceType(modelMapper.map(resourceDTO.getResourceType(), ResourceType.class));

        try {
            resourceRepository.save(resource);
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
        return modelMapper.map(resource, ResourceDTO.class);
    }

    @Override
    public ResourceDTO update(ResourceDTO resourceDTO) {
        Optional<Resource> resource = resourceRepository.findById(resourceDTO.getId());
        if (!resource.isPresent()) {
            throw new RuntimeException("Recurso no encontrado - " + resourceDTO.getId());
        }

        ModelMapper modelMapper = new ModelMapper();

        try {
            resource.ifPresent(r -> {
                r.setActive(resourceDTO.getActive());
                r.setDescription(resourceDTO.getDescription());
                r.setCode(resourceDTO.getCode());
                r.setResourceType(modelMapper.map(resourceDTO.getResourceType(), ResourceType.class));

                resourceRepository.save(r);
            });

        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
        return modelMapper.map(resource.get(), ResourceDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Resource> resource = resourceRepository.findById(id);
        if (!resource.isPresent()) {
            throw new RuntimeException("Recurso no encontrado - " + id);
        }

        try {
            resourceRepository.delete(resource.get());
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
    }
}
