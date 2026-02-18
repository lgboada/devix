package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.Provincia;
import com.devix.repository.ProvinciaRepository;
import com.devix.service.criteria.ProvinciaCriteria;
import com.devix.service.dto.ProvinciaDTO;
import com.devix.service.mapper.ProvinciaMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Provincia} entities in the database.
 * The main input is a {@link ProvinciaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProvinciaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProvinciaQueryService extends QueryService<Provincia> {

    private static final Logger LOG = LoggerFactory.getLogger(ProvinciaQueryService.class);

    private final ProvinciaRepository provinciaRepository;

    private final ProvinciaMapper provinciaMapper;

    public ProvinciaQueryService(ProvinciaRepository provinciaRepository, ProvinciaMapper provinciaMapper) {
        this.provinciaRepository = provinciaRepository;
        this.provinciaMapper = provinciaMapper;
    }

    /**
     * Return a {@link List} of {@link ProvinciaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProvinciaDTO> findByCriteria(ProvinciaCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Provincia> specification = createSpecification(criteria);
        return provinciaMapper.toDto(provinciaRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProvinciaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Provincia> specification = createSpecification(criteria);
        return provinciaRepository.count(specification);
    }

    /**
     * Function to convert {@link ProvinciaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Provincia> createSpecification(ProvinciaCriteria criteria) {
        Specification<Provincia> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Provincia_.id),
                buildRangeSpecification(criteria.getNoCia(), Provincia_.noCia),
                buildStringSpecification(criteria.getDescripcion(), Provincia_.descripcion),
                buildSpecification(criteria.getCiudadId(), root -> root.join(Provincia_.ciudads, JoinType.LEFT).get(Ciudad_.id)),
                buildSpecification(criteria.getPaisId(), root -> root.join(Provincia_.pais, JoinType.LEFT).get(Pais_.id))
            );
        }
        return specification;
    }
}
