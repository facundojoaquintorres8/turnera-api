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

import com.f8.turnera.entities.Organization;
import com.f8.turnera.entities.Resource;
import com.f8.turnera.entities.ResourceType;
import com.f8.turnera.models.ResourceDTO;
import com.f8.turnera.models.ResourceFilterDTO;
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
public class ResourceService implements IResourceService {

    @Autowired
    private IResourceRepository resourceRepository;

    @Autowired
    private IOrganizationRepository organizationRepository;

    @Autowired
    private EntityManager em;

    @Override
    public Page<ResourceDTO> findAllByFilter(ResourceFilterDTO filter) {
        ModelMapper modelMapper = new ModelMapper();

        Page<Resource> resources = findByCriteria(filter);
        return resources.map(resource -> modelMapper.map(resource, ResourceDTO.class));
    }

    private Page<Resource> findByCriteria(ResourceFilterDTO filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Resource> cq = cb.createQuery(Resource.class);

        List<Predicate> predicates = new ArrayList<>();

        Root<Resource> root = cq.from(Resource.class);
        if (filter.getDescription() != null) {
            Predicate predicate = cb.like(cb.lower(root.get("description")),
                    "%" + filter.getDescription().toLowerCase() + "%");
            predicates.add(predicate);
        }
        if (filter.getCode() != null) {
            Predicate predicate = cb.like(cb.lower(root.get("code")), "%" + filter.getCode().toLowerCase() + "%");
            predicates.add(predicate);
        }
        if (filter.getResourceTypeDescription() != null) {
            Predicate predicate = cb.like(cb.lower(root.join("resourceType", JoinType.LEFT).get("description")),
                    "%" + filter.getResourceTypeDescription().toLowerCase() + "%");
            predicates.add(predicate);
        }
        if (filter.getOrganizationId() != null) {
            Predicate predicate = cb.equal(root.join("organization", JoinType.LEFT), filter.getOrganizationId());
            predicates.add(predicate);
        }
        if (filter.getResourceTypeId() != null) {
            Predicate predicate = cb.equal(root.join("resourceType", JoinType.LEFT), filter.getResourceTypeId());
            predicates.add(predicate);
        }
        if (filter.getActive() != null) {
            Predicate predicate = cb.equal(root.get("active"), filter.getActive());
            predicates.add(predicate);
        }

        cq.where(predicates.toArray(new Predicate[0]));

        List<Resource> result = em.createQuery(cq).getResultList();
        if (filter.getSort() != null) {
            if (filter.getSort().get(0).equals("ASC")) {
                switch (filter.getSort().get(1)) {
                case "description":
                    result.sort(Comparator.comparing(Resource::getDescription, String::compareToIgnoreCase));
                    break;
                case "code":
                    result.sort(Comparator.comparing(Resource::getCode,
                            Comparator.nullsFirst(String::compareToIgnoreCase)));
                    break;
                case "resourceTypeDescription":
                    result.sort(Comparator.comparing(x -> x.getResourceType().getDescription(),
                            String::compareToIgnoreCase));
                    break;
                default:
                    break;
                }
            } else if (filter.getSort().get(0).equals("DESC")) {
                switch (filter.getSort().get(1)) {
                case "description":
                    result.sort(Comparator.comparing(Resource::getDescription, String::compareToIgnoreCase).reversed());
                    break;
                case "code":
                    result.sort(
                            Comparator.comparing(Resource::getCode, Comparator.nullsFirst(String::compareToIgnoreCase))
                                    .reversed());
                    break;
                case "resourceTypeDescription":
                    result.sort(Comparator.comparing(x -> x.getResourceType().getDescription(),
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
        Pageable pageable = PageRequest.of(filter.getPage(), Constants.ITEMS_PER_PAGE);
        return new PageImpl<Resource>(result.subList(fromIndex, toIndex), pageable, count);
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
        } catch (DataIntegrityViolationException dive) {
            throw new RuntimeException("No se puede eliminar el Recurso porque tiene Disponibilidades asociadas.");
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
    }
}
