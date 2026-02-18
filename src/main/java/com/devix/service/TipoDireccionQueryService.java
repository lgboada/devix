package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.TipoDireccion;
import com.devix.repository.TipoDireccionRepository;
import com.devix.service.criteria.TipoDireccionCriteria;
import com.devix.service.dto.TipoDireccionDTO;
import com.devix.service.mapper.TipoDireccionMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TipoDireccion} entities in the database.
 * The main input is a {@link TipoDireccionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TipoDireccionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TipoDireccionQueryService extends QueryService<TipoDireccion> {

    private static final Logger LOG = LoggerFactory.getLogger(TipoDireccionQueryService.class);

    private final TipoDireccionRepository tipoDireccionRepository;

    private final TipoDireccionMapper tipoDireccionMapper;

    public TipoDireccionQueryService(TipoDireccionRepository tipoDireccionRepository, TipoDireccionMapper tipoDireccionMapper) {
        this.tipoDireccionRepository = tipoDireccionRepository;
        this.tipoDireccionMapper = tipoDireccionMapper;
    }

    /**
     * Return a {@link List} of {@link TipoDireccionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TipoDireccionDTO> findByCriteria(TipoDireccionCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<TipoDireccion> specification = createSpecification(criteria);
        return tipoDireccionMapper.toDto(tipoDireccionRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TipoDireccionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TipoDireccion> specification = createSpecification(criteria);
        return tipoDireccionRepository.count(specification);
    }

    /**
     * Function to convert {@link TipoDireccionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TipoDireccion> createSpecification(TipoDireccionCriteria criteria) {
        Specification<TipoDireccion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TipoDireccion_.id),
                buildRangeSpecification(criteria.getNoCia(), TipoDireccion_.noCia),
                buildStringSpecification(criteria.getDescripcion(), TipoDireccion_.descripcion),
                buildSpecification(criteria.getDireccionId(), root -> root.join(TipoDireccion_.direccions, JoinType.LEFT).get(Direccion_.id)
                )
            );
        }
        return specification;
    }
}
