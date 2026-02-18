package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.Evento;
import com.devix.repository.EventoRepository;
import com.devix.service.criteria.EventoCriteria;
import com.devix.service.dto.EventoDTO;
import com.devix.service.mapper.EventoMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Evento} entities in the database.
 * The main input is a {@link EventoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventoQueryService extends QueryService<Evento> {

    private static final Logger LOG = LoggerFactory.getLogger(EventoQueryService.class);

    private final EventoRepository eventoRepository;

    private final EventoMapper eventoMapper;

    public EventoQueryService(EventoRepository eventoRepository, EventoMapper eventoMapper) {
        this.eventoRepository = eventoRepository;
        this.eventoMapper = eventoMapper;
    }

    /**
     * Return a {@link List} of {@link EventoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventoDTO> findByCriteria(EventoCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Evento> specification = createSpecification(criteria);
        return eventoMapper.toDto(eventoRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Evento> specification = createSpecification(criteria);
        return eventoRepository.count(specification);
    }

    /**
     * Function to convert {@link EventoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Evento> createSpecification(EventoCriteria criteria) {
        Specification<Evento> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Evento_.id),
                buildRangeSpecification(criteria.getNoCia(), Evento_.noCia),
                buildStringSpecification(criteria.getDescripcion(), Evento_.descripcion),
                buildRangeSpecification(criteria.getFecha(), Evento_.fecha),
                buildStringSpecification(criteria.getEstado(), Evento_.estado),
                buildStringSpecification(criteria.getMotivoConsulta(), Evento_.motivoConsulta),
                buildStringSpecification(criteria.getTratamiento(), Evento_.tratamiento),
                buildStringSpecification(criteria.getIndicaciones(), Evento_.indicaciones),
                buildStringSpecification(criteria.getDiagnostico1(), Evento_.diagnostico1),
                buildStringSpecification(criteria.getDiagnostico2(), Evento_.diagnostico2),
                buildStringSpecification(criteria.getDiagnostico3(), Evento_.diagnostico3),
                buildStringSpecification(criteria.getDiagnostico4(), Evento_.diagnostico4),
                buildStringSpecification(criteria.getDiagnostico5(), Evento_.diagnostico5),
                buildStringSpecification(criteria.getDiagnostico6(), Evento_.diagnostico6),
                buildStringSpecification(criteria.getDiagnostico7(), Evento_.diagnostico7),
                buildStringSpecification(criteria.getObservacion(), Evento_.observacion),
                buildSpecification(criteria.getDocumentoId(), root -> root.join(Evento_.documentos, JoinType.LEFT).get(Documento_.id)),
                buildSpecification(criteria.getTipoEventoId(), root -> root.join(Evento_.tipoEvento, JoinType.LEFT).get(TipoEvento_.id)),
                buildSpecification(criteria.getCentroId(), root -> root.join(Evento_.centro, JoinType.LEFT).get(Centro_.id)),
                buildSpecification(criteria.getClienteId(), root -> root.join(Evento_.cliente, JoinType.LEFT).get(Cliente_.id))
            );
        }
        return specification;
    }
}
