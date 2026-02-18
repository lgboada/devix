package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.Modelo;
import com.devix.repository.ModeloRepository;
import com.devix.service.criteria.ModeloCriteria;
import com.devix.service.dto.ModeloDTO;
import com.devix.service.mapper.ModeloMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Modelo} entities in the database.
 * The main input is a {@link ModeloCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ModeloDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ModeloQueryService extends QueryService<Modelo> {

    private static final Logger LOG = LoggerFactory.getLogger(ModeloQueryService.class);

    private final ModeloRepository modeloRepository;

    private final ModeloMapper modeloMapper;

    public ModeloQueryService(ModeloRepository modeloRepository, ModeloMapper modeloMapper) {
        this.modeloRepository = modeloRepository;
        this.modeloMapper = modeloMapper;
    }

    /**
     * Return a {@link List} of {@link ModeloDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ModeloDTO> findByCriteria(ModeloCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Modelo> specification = createSpecification(criteria);
        return modeloMapper.toDto(modeloRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ModeloCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Modelo> specification = createSpecification(criteria);
        return modeloRepository.count(specification);
    }

    /**
     * Function to convert {@link ModeloCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Modelo> createSpecification(ModeloCriteria criteria) {
        Specification<Modelo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Modelo_.id),
                buildRangeSpecification(criteria.getNoCia(), Modelo_.noCia),
                buildStringSpecification(criteria.getNombre(), Modelo_.nombre),
                buildStringSpecification(criteria.getPathImagen(), Modelo_.pathImagen),
                buildSpecification(criteria.getProductoId(), root -> root.join(Modelo_.productos, JoinType.LEFT).get(Producto_.id)),
                buildSpecification(criteria.getMarcaId(), root -> root.join(Modelo_.marca, JoinType.LEFT).get(Marca_.id))
            );
        }
        return specification;
    }
}
