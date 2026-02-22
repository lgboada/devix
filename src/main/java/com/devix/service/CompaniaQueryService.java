package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.Compania;
import com.devix.repository.CompaniaRepository;
import com.devix.service.criteria.CompaniaCriteria;
import com.devix.service.dto.CompaniaDTO;
import com.devix.service.mapper.CompaniaMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Compania} entities in the database.
 * The main input is a {@link CompaniaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CompaniaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CompaniaQueryService extends QueryService<Compania> {

    private static final Logger LOG = LoggerFactory.getLogger(CompaniaQueryService.class);

    private final CompaniaRepository companiaRepository;

    private final CompaniaMapper companiaMapper;

    public CompaniaQueryService(CompaniaRepository companiaRepository, CompaniaMapper companiaMapper) {
        this.companiaRepository = companiaRepository;
        this.companiaMapper = companiaMapper;
    }

    /**
     * Return a {@link List} of {@link CompaniaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CompaniaDTO> findByCriteria(CompaniaCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Compania> specification = createSpecification(criteria);
        return companiaMapper.toDto(companiaRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CompaniaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Compania> specification = createSpecification(criteria);
        return companiaRepository.count(specification);
    }

    /**
     * Function to convert {@link CompaniaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Compania> createSpecification(CompaniaCriteria criteria) {
        Specification<Compania> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Compania_.id),
                buildRangeSpecification(criteria.getNoCia(), Compania_.noCia),
                buildStringSpecification(criteria.getDni(), Compania_.dni),
                buildStringSpecification(criteria.getNombre(), Compania_.nombre),
                buildStringSpecification(criteria.getDireccion(), Compania_.direccion),
                buildStringSpecification(criteria.getEmail(), Compania_.email),
                buildStringSpecification(criteria.getTelefono(), Compania_.telefono),
                buildStringSpecification(criteria.getPathImage(), Compania_.pathImage),
                buildSpecification(criteria.getActiva(), Compania_.activa),
                buildSpecification(criteria.getCentrosId(), root -> root.join(Compania_.centros, JoinType.LEFT).get(Centro_.id))
            );
        }
        return specification;
    }
}
