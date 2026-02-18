package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.Marca;
import com.devix.repository.MarcaRepository;
import com.devix.service.criteria.MarcaCriteria;
import com.devix.service.dto.MarcaDTO;
import com.devix.service.mapper.MarcaMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Marca} entities in the database.
 * The main input is a {@link MarcaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MarcaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MarcaQueryService extends QueryService<Marca> {

    private static final Logger LOG = LoggerFactory.getLogger(MarcaQueryService.class);

    private final MarcaRepository marcaRepository;

    private final MarcaMapper marcaMapper;

    public MarcaQueryService(MarcaRepository marcaRepository, MarcaMapper marcaMapper) {
        this.marcaRepository = marcaRepository;
        this.marcaMapper = marcaMapper;
    }

    /**
     * Return a {@link List} of {@link MarcaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MarcaDTO> findByCriteria(MarcaCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Marca> specification = createSpecification(criteria);
        return marcaMapper.toDto(marcaRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MarcaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Marca> specification = createSpecification(criteria);
        return marcaRepository.count(specification);
    }

    /**
     * Function to convert {@link MarcaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Marca> createSpecification(MarcaCriteria criteria) {
        Specification<Marca> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Marca_.id),
                buildRangeSpecification(criteria.getNoCia(), Marca_.noCia),
                buildStringSpecification(criteria.getNombre(), Marca_.nombre),
                buildStringSpecification(criteria.getPathImagen(), Marca_.pathImagen),
                buildSpecification(criteria.getModelosId(), root -> root.join(Marca_.modelos, JoinType.LEFT).get(Modelo_.id))
            );
        }
        return specification;
    }
}
