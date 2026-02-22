package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.UsuarioCentro;
import com.devix.repository.UsuarioCentroRepository;
import com.devix.service.criteria.UsuarioCentroCriteria;
import com.devix.service.dto.UsuarioCentroDTO;
import com.devix.service.mapper.UsuarioCentroMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link UsuarioCentro} entities in the database.
 * The main input is a {@link UsuarioCentroCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UsuarioCentroDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UsuarioCentroQueryService extends QueryService<UsuarioCentro> {

    private static final Logger LOG = LoggerFactory.getLogger(UsuarioCentroQueryService.class);

    private final UsuarioCentroRepository usuarioCentroRepository;

    private final UsuarioCentroMapper usuarioCentroMapper;

    public UsuarioCentroQueryService(UsuarioCentroRepository usuarioCentroRepository, UsuarioCentroMapper usuarioCentroMapper) {
        this.usuarioCentroRepository = usuarioCentroRepository;
        this.usuarioCentroMapper = usuarioCentroMapper;
    }

    /**
     * Return a {@link List} of {@link UsuarioCentroDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UsuarioCentroDTO> findByCriteria(UsuarioCentroCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<UsuarioCentro> specification = createSpecification(criteria);
        return usuarioCentroMapper.toDto(usuarioCentroRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UsuarioCentroCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<UsuarioCentro> specification = createSpecification(criteria);
        return usuarioCentroRepository.count(specification);
    }

    /**
     * Function to convert {@link UsuarioCentroCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UsuarioCentro> createSpecification(UsuarioCentroCriteria criteria) {
        Specification<UsuarioCentro> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), UsuarioCentro_.id),
                buildRangeSpecification(criteria.getNoCia(), UsuarioCentro_.noCia),
                buildSpecification(criteria.getPrincipal(), UsuarioCentro_.principal),
                buildSpecification(criteria.getCentroId(), root -> root.join(UsuarioCentro_.centro, JoinType.LEFT).get(Centro_.id)),
                buildSpecification(criteria.getUserId(), root -> root.join(UsuarioCentro_.user, JoinType.LEFT).get(User_.id))
            );
        }
        return specification;
    }
}
