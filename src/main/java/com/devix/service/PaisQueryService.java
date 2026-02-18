package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.Pais;
import com.devix.repository.PaisRepository;
import com.devix.service.criteria.PaisCriteria;
import com.devix.service.dto.PaisDTO;
import com.devix.service.mapper.PaisMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Pais} entities in the database.
 * The main input is a {@link PaisCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaisDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaisQueryService extends QueryService<Pais> {

    private static final Logger LOG = LoggerFactory.getLogger(PaisQueryService.class);

    private final PaisRepository paisRepository;

    private final PaisMapper paisMapper;

    public PaisQueryService(PaisRepository paisRepository, PaisMapper paisMapper) {
        this.paisRepository = paisRepository;
        this.paisMapper = paisMapper;
    }

    /**
     * Return a {@link List} of {@link PaisDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaisDTO> findByCriteria(PaisCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Pais> specification = createSpecification(criteria);
        return paisMapper.toDto(paisRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaisCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Pais> specification = createSpecification(criteria);
        return paisRepository.count(specification);
    }

    /**
     * Function to convert {@link PaisCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Pais> createSpecification(PaisCriteria criteria) {
        Specification<Pais> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Pais_.id),
                buildRangeSpecification(criteria.getNoCia(), Pais_.noCia),
                buildStringSpecification(criteria.getDescripcion(), Pais_.descripcion),
                buildSpecification(criteria.getProvinciaId(), root -> root.join(Pais_.provincias, JoinType.LEFT).get(Provincia_.id))
            );
        }
        return specification;
    }
}
