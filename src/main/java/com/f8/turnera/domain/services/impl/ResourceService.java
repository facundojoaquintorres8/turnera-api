package com.f8.turnera.domain.services.impl;

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

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.config.TokenUtil;
import com.f8.turnera.domain.dtos.OrganizationDTO;
import com.f8.turnera.domain.dtos.ResourceDTO;
import com.f8.turnera.domain.dtos.ResourceFilterDTO;
import com.f8.turnera.domain.entities.Organization;
import com.f8.turnera.domain.entities.Resource;
import com.f8.turnera.domain.entities.ResourceType;
import com.f8.turnera.domain.repositories.IResourceRepository;
import com.f8.turnera.domain.services.IOrganizationService;
import com.f8.turnera.domain.services.IResourceService;
import com.f8.turnera.exception.NoContentException;
import com.f8.turnera.util.Constants;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    private IOrganizationService organizationService;

    @Autowired
    private EntityManager em;

    @Override
    public Page<ResourceDTO> findAllByFilter(String token, ResourceFilterDTO filter) throws Exception {
        ModelMapper modelMapper = new ModelMapper();

        filter.setOrganizationId(Long.parseLong(TokenUtil.getClaimByToken(token, SecurityConstants.ORGANIZATION_KEY).toString()));

        Page<Resource> resources = findByCriteria(filter);
        return resources.map(resource -> modelMapper.map(resource, ResourceDTO.class));
    }

    private Page<Resource> findByCriteria(ResourceFilterDTO filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Resource> cq = cb.createQuery(Resource.class);

        List<Predicate> predicates = new ArrayList<>();

        Root<Resource> root = cq.from(Resource.class);
        predicates.add(cb.equal(root.join("organization", JoinType.LEFT), filter.getOrganizationId()));
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
    public ResourceDTO findById(String token, Long id) throws Exception {
        Long orgId = Long.parseLong(TokenUtil.getClaimByToken(token, SecurityConstants.ORGANIZATION_KEY).toString());
        Optional<Resource> resource = resourceRepository.findByIdAndOrganizationId(id, orgId);
        if (!resource.isPresent()) {
            throw new NoContentException("Recurso no encontrado - " + id);
        }

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(resource.get(), ResourceDTO.class);
    }

    @Override
    public ResourceDTO create(String token, ResourceDTO resourceDTO) throws Exception {
        ModelMapper modelMapper = new ModelMapper();

        OrganizationDTO organization = organizationService.findById(token);

        Resource resource = modelMapper.map(resourceDTO, Resource.class);
        resource.setCreatedDate(LocalDateTime.now());
        resource.setActive(true);
        resource.setOrganization(modelMapper.map(organization, Organization.class));
        resource.setResourceType(modelMapper.map(resourceDTO.getResourceType(), ResourceType.class));

        resourceRepository.save(resource);

        return modelMapper.map(resource, ResourceDTO.class);
    }

    @Override
    public ResourceDTO update(String token, ResourceDTO resourceDTO) throws Exception {
        Long orgId = Long.parseLong(TokenUtil.getClaimByToken(token, SecurityConstants.ORGANIZATION_KEY).toString());
        Optional<Resource> resource = resourceRepository.findByIdAndOrganizationId(resourceDTO.getId(), orgId);
        if (!resource.isPresent()) {
            throw new NoContentException("Recurso no encontrado - " + resourceDTO.getId());
        }

        ModelMapper modelMapper = new ModelMapper();

        resource.ifPresent(r -> {
            r.setActive(resourceDTO.getActive());
            r.setDescription(resourceDTO.getDescription());
            r.setCode(resourceDTO.getCode());
            r.setResourceType(modelMapper.map(resourceDTO.getResourceType(), ResourceType.class));

            resourceRepository.save(r);
        });

        return modelMapper.map(resource.get(), ResourceDTO.class);
    }

    @Override
    public void deleteById(String token, Long id) throws Exception {
        Long orgId = Long.parseLong(TokenUtil.getClaimByToken(token, SecurityConstants.ORGANIZATION_KEY).toString());
        Optional<Resource> resource = resourceRepository.findByIdAndOrganizationId(id, orgId);
        if (!resource.isPresent()) {
            throw new NoContentException("Recurso no encontrado - " + id);
        }

        resourceRepository.delete(resource.get());
    }
}
