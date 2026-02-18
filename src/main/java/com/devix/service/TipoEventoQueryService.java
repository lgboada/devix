package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.TipoEvento;
import com.devix.repository.TipoEventoRepository;
import com.devix.service.criteria.TipoEventoCriteria;
import com.devix.service.dto.TipoEventoDTO;
import com.devix.service.mapper.TipoEventoMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TipoEvento} entities in the database.
 * The main input is a {@link TipoEventoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TipoEventoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TipoEventoQueryService extends QueryService<TipoEvento> {

    private static final Logger LOG = LoggerFactory.getLogger(TipoEventoQueryService.class);

    private final TipoEventoRepository tipoEventoRepository;

    private final TipoEventoMapper tipoEventoMapper;

    public TipoEventoQueryService(TipoEventoRepository tipoEventoRepository, TipoEventoMapper tipoEventoMapper) {
        this.tipoEventoRepository = tipoEventoRepository;
        this.tipoEventoMapper = tipoEventoMapper;
    }

    /**
     * Return a {@link List} of {@link TipoEventoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TipoEventoDTO> findByCriteria(TipoEventoCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<TipoEvento> specification = createSpecification(criteria);
        return tipoEventoMapper.toDto(tipoEventoRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TipoEventoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TipoEvento> specification = createSpecification(criteria);
        return tipoEventoRepository.count(specification);
    }

    /**
     * Function to convert {@link TipoEventoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TipoEvento> createSpecification(TipoEventoCriteria criteria) {
        Specification<TipoEvento> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TipoEvento_.id),
                buildRangeSpecification(criteria.getNoCia(), TipoEvento_.noCia),
                buildStringSpecification(criteria.getNombre(), TipoEvento_.nombre),
                buildSpecification(criteria.getEventoId(), root -> root.join(TipoEvento_.eventos, JoinType.LEFT).get(Evento_.id))
            );
        }
        return specification;
    }
}
