package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.TipoCatalogo;
import com.devix.repository.TipoCatalogoRepository;
import com.devix.service.criteria.TipoCatalogoCriteria;
import com.devix.service.dto.TipoCatalogoDTO;
import com.devix.service.mapper.TipoCatalogoMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TipoCatalogo} entities in the database.
 * The main input is a {@link TipoCatalogoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TipoCatalogoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TipoCatalogoQueryService extends QueryService<TipoCatalogo> {

    private static final Logger LOG = LoggerFactory.getLogger(TipoCatalogoQueryService.class);

    private final TipoCatalogoRepository tipoCatalogoRepository;

    private final TipoCatalogoMapper tipoCatalogoMapper;

    public TipoCatalogoQueryService(TipoCatalogoRepository tipoCatalogoRepository, TipoCatalogoMapper tipoCatalogoMapper) {
        this.tipoCatalogoRepository = tipoCatalogoRepository;
        this.tipoCatalogoMapper = tipoCatalogoMapper;
    }

    /**
     * Return a {@link List} of {@link TipoCatalogoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TipoCatalogoDTO> findByCriteria(TipoCatalogoCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<TipoCatalogo> specification = createSpecification(criteria);
        return tipoCatalogoMapper.toDto(tipoCatalogoRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TipoCatalogoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TipoCatalogo> specification = createSpecification(criteria);
        return tipoCatalogoRepository.count(specification);
    }

    /**
     * Function to convert {@link TipoCatalogoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TipoCatalogo> createSpecification(TipoCatalogoCriteria criteria) {
        Specification<TipoCatalogo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TipoCatalogo_.id),
                buildRangeSpecification(criteria.getNoCia(), TipoCatalogo_.noCia),
                buildStringSpecification(criteria.getDescripcion(), TipoCatalogo_.descripcion),
                buildStringSpecification(criteria.getCategoria(), TipoCatalogo_.categoria),
                buildSpecification(criteria.getCatalogoId(), root -> root.join(TipoCatalogo_.catalogos, JoinType.LEFT).get(Catalogo_.id))
            );
        }
        return specification;
    }
}
