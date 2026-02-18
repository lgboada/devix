package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.Ciudad;
import com.devix.repository.CiudadRepository;
import com.devix.service.criteria.CiudadCriteria;
import com.devix.service.dto.CiudadDTO;
import com.devix.service.mapper.CiudadMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Ciudad} entities in the database.
 * The main input is a {@link CiudadCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CiudadDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CiudadQueryService extends QueryService<Ciudad> {

    private static final Logger LOG = LoggerFactory.getLogger(CiudadQueryService.class);

    private final CiudadRepository ciudadRepository;

    private final CiudadMapper ciudadMapper;

    public CiudadQueryService(CiudadRepository ciudadRepository, CiudadMapper ciudadMapper) {
        this.ciudadRepository = ciudadRepository;
        this.ciudadMapper = ciudadMapper;
    }

    /**
     * Return a {@link List} of {@link CiudadDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CiudadDTO> findByCriteria(CiudadCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Ciudad> specification = createSpecification(criteria);
        return ciudadMapper.toDto(ciudadRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CiudadCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Ciudad> specification = createSpecification(criteria);
        return ciudadRepository.count(specification);
    }

    /**
     * Function to convert {@link CiudadCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ciudad> createSpecification(CiudadCriteria criteria) {
        Specification<Ciudad> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Ciudad_.id),
                buildRangeSpecification(criteria.getNoCia(), Ciudad_.noCia),
                buildStringSpecification(criteria.getDescripcion(), Ciudad_.descripcion),
                buildSpecification(criteria.getClienteId(), root -> root.join(Ciudad_.clientes, JoinType.LEFT).get(Cliente_.id)),
                buildSpecification(criteria.getProvinciaId(), root -> root.join(Ciudad_.provincia, JoinType.LEFT).get(Provincia_.id))
            );
        }
        return specification;
    }
}
