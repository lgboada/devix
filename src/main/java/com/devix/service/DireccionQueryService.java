package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.Direccion;
import com.devix.repository.DireccionRepository;
import com.devix.service.criteria.DireccionCriteria;
import com.devix.service.dto.DireccionDTO;
import com.devix.service.mapper.DireccionMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Direccion} entities in the database.
 * The main input is a {@link DireccionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DireccionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DireccionQueryService extends QueryService<Direccion> {

    private static final Logger LOG = LoggerFactory.getLogger(DireccionQueryService.class);

    private final DireccionRepository direccionRepository;

    private final DireccionMapper direccionMapper;

    public DireccionQueryService(DireccionRepository direccionRepository, DireccionMapper direccionMapper) {
        this.direccionRepository = direccionRepository;
        this.direccionMapper = direccionMapper;
    }

    /**
     * Return a {@link List} of {@link DireccionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DireccionDTO> findByCriteria(DireccionCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Direccion> specification = createSpecification(criteria);
        return direccionMapper.toDto(direccionRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DireccionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Direccion> specification = createSpecification(criteria);
        return direccionRepository.count(specification);
    }

    /**
     * Function to convert {@link DireccionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Direccion> createSpecification(DireccionCriteria criteria) {
        Specification<Direccion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Direccion_.id),
                buildRangeSpecification(criteria.getNoCia(), Direccion_.noCia),
                buildStringSpecification(criteria.getDescripcion(), Direccion_.descripcion),
                buildStringSpecification(criteria.getTelefono(), Direccion_.telefono),
                buildRangeSpecification(criteria.getLatitud(), Direccion_.latitud),
                buildRangeSpecification(criteria.getLongitud(), Direccion_.longitud),
                buildSpecification(criteria.getTipoDireccionId(), root ->
                    root.join(Direccion_.tipoDireccion, JoinType.LEFT).get(TipoDireccion_.id)
                ),
                buildSpecification(criteria.getClienteId(), root -> root.join(Direccion_.cliente, JoinType.LEFT).get(Cliente_.id))
            );
        }
        return specification;
    }
}
