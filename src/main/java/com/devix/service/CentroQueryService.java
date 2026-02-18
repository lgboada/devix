package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.Centro;
import com.devix.repository.CentroRepository;
import com.devix.service.criteria.CentroCriteria;
import com.devix.service.dto.CentroDTO;
import com.devix.service.mapper.CentroMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Centro} entities in the database.
 * The main input is a {@link CentroCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CentroDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CentroQueryService extends QueryService<Centro> {

    private static final Logger LOG = LoggerFactory.getLogger(CentroQueryService.class);

    private final CentroRepository centroRepository;

    private final CentroMapper centroMapper;

    public CentroQueryService(CentroRepository centroRepository, CentroMapper centroMapper) {
        this.centroRepository = centroRepository;
        this.centroMapper = centroMapper;
    }

    /**
     * Return a {@link List} of {@link CentroDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CentroDTO> findByCriteria(CentroCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Centro> specification = createSpecification(criteria);
        return centroMapper.toDto(centroRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CentroCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Centro> specification = createSpecification(criteria);
        return centroRepository.count(specification);
    }

    /**
     * Function to convert {@link CentroCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Centro> createSpecification(CentroCriteria criteria) {
        Specification<Centro> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Centro_.id),
                buildRangeSpecification(criteria.getNoCia(), Centro_.noCia),
                buildStringSpecification(criteria.getDescripcion(), Centro_.descripcion),
                buildSpecification(criteria.getFacturaId(), root -> root.join(Centro_.facturas, JoinType.LEFT).get(Factura_.id)),
                buildSpecification(criteria.getEventoId(), root -> root.join(Centro_.eventos, JoinType.LEFT).get(Evento_.id)),
                buildSpecification(criteria.getCompaniaId(), root -> root.join(Centro_.compania, JoinType.LEFT).get(Compania_.id))
            );
        }
        return specification;
    }
}
