package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.Documento;
import com.devix.repository.DocumentoRepository;
import com.devix.service.criteria.DocumentoCriteria;
import com.devix.service.dto.DocumentoDTO;
import com.devix.service.mapper.DocumentoMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Documento} entities in the database.
 * The main input is a {@link DocumentoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DocumentoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentoQueryService extends QueryService<Documento> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentoQueryService.class);

    private final DocumentoRepository documentoRepository;

    private final DocumentoMapper documentoMapper;

    public DocumentoQueryService(DocumentoRepository documentoRepository, DocumentoMapper documentoMapper) {
        this.documentoRepository = documentoRepository;
        this.documentoMapper = documentoMapper;
    }

    /**
     * Return a {@link List} of {@link DocumentoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DocumentoDTO> findByCriteria(DocumentoCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Documento> specification = createSpecification(criteria);
        return documentoMapper.toDto(documentoRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Documento> specification = createSpecification(criteria);
        return documentoRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Documento> createSpecification(DocumentoCriteria criteria) {
        Specification<Documento> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Documento_.id),
                buildRangeSpecification(criteria.getNoCia(), Documento_.noCia),
                buildStringSpecification(criteria.getTipo(), Documento_.tipo),
                buildStringSpecification(criteria.getObservacion(), Documento_.observacion),
                buildRangeSpecification(criteria.getFechaCreacion(), Documento_.fechaCreacion),
                buildRangeSpecification(criteria.getFechaVencimiento(), Documento_.fechaVencimiento),
                buildStringSpecification(criteria.getPath(), Documento_.path),
                buildSpecification(criteria.getClienteId(), root -> root.join(Documento_.cliente, JoinType.LEFT).get(Cliente_.id)),
                buildSpecification(criteria.getEventoId(), root -> root.join(Documento_.evento, JoinType.LEFT).get(Evento_.id))
            );
        }
        return specification;
    }
}
