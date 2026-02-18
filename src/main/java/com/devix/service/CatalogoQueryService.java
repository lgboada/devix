package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.Catalogo;
import com.devix.repository.CatalogoRepository;
import com.devix.service.criteria.CatalogoCriteria;
import com.devix.service.dto.CatalogoDTO;
import com.devix.service.mapper.CatalogoMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Catalogo} entities in the database.
 * The main input is a {@link CatalogoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CatalogoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CatalogoQueryService extends QueryService<Catalogo> {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogoQueryService.class);

    private final CatalogoRepository catalogoRepository;

    private final CatalogoMapper catalogoMapper;

    public CatalogoQueryService(CatalogoRepository catalogoRepository, CatalogoMapper catalogoMapper) {
        this.catalogoRepository = catalogoRepository;
        this.catalogoMapper = catalogoMapper;
    }

    /**
     * Return a {@link List} of {@link CatalogoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CatalogoDTO> findByCriteria(CatalogoCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Catalogo> specification = createSpecification(criteria);
        return catalogoMapper.toDto(catalogoRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CatalogoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Catalogo> specification = createSpecification(criteria);
        return catalogoRepository.count(specification);
    }

    /**
     * Function to convert {@link CatalogoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Catalogo> createSpecification(CatalogoCriteria criteria) {
        Specification<Catalogo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Catalogo_.id),
                buildRangeSpecification(criteria.getNoCia(), Catalogo_.noCia),
                buildStringSpecification(criteria.getDescripcion1(), Catalogo_.descripcion1),
                buildStringSpecification(criteria.getDescripcion2(), Catalogo_.descripcion2),
                buildStringSpecification(criteria.getEstado(), Catalogo_.estado),
                buildRangeSpecification(criteria.getOrden(), Catalogo_.orden),
                buildStringSpecification(criteria.getTexto1(), Catalogo_.texto1),
                buildStringSpecification(criteria.getTexto2(), Catalogo_.texto2),
                buildSpecification(criteria.getTipoCatalogoId(), root ->
                    root.join(Catalogo_.tipoCatalogo, JoinType.LEFT).get(TipoCatalogo_.id)
                )
            );
        }
        return specification;
    }
}
