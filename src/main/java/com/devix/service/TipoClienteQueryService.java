package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.TipoCliente;
import com.devix.repository.TipoClienteRepository;
import com.devix.service.criteria.TipoClienteCriteria;
import com.devix.service.dto.TipoClienteDTO;
import com.devix.service.mapper.TipoClienteMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TipoCliente} entities in the database.
 * The main input is a {@link TipoClienteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TipoClienteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TipoClienteQueryService extends QueryService<TipoCliente> {

    private static final Logger LOG = LoggerFactory.getLogger(TipoClienteQueryService.class);

    private final TipoClienteRepository tipoClienteRepository;

    private final TipoClienteMapper tipoClienteMapper;

    public TipoClienteQueryService(TipoClienteRepository tipoClienteRepository, TipoClienteMapper tipoClienteMapper) {
        this.tipoClienteRepository = tipoClienteRepository;
        this.tipoClienteMapper = tipoClienteMapper;
    }

    /**
     * Return a {@link List} of {@link TipoClienteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TipoClienteDTO> findByCriteria(TipoClienteCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<TipoCliente> specification = createSpecification(criteria);
        return tipoClienteMapper.toDto(tipoClienteRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TipoClienteCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TipoCliente> specification = createSpecification(criteria);
        return tipoClienteRepository.count(specification);
    }

    /**
     * Function to convert {@link TipoClienteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TipoCliente> createSpecification(TipoClienteCriteria criteria) {
        Specification<TipoCliente> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TipoCliente_.id),
                buildRangeSpecification(criteria.getNoCia(), TipoCliente_.noCia),
                buildStringSpecification(criteria.getDescripcion(), TipoCliente_.descripcion),
                buildSpecification(criteria.getClienteId(), root -> root.join(TipoCliente_.clientes, JoinType.LEFT).get(Cliente_.id))
            );
        }
        return specification;
    }
}
