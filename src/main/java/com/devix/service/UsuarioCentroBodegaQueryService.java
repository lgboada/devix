package com.devix.service;

import com.devix.domain.*;
import com.devix.domain.UsuarioCentroBodega;
import com.devix.repository.UsuarioCentroBodegaRepository;
import com.devix.service.criteria.UsuarioCentroBodegaCriteria;
import com.devix.service.dto.UsuarioCentroBodegaDTO;
import com.devix.service.mapper.UsuarioCentroBodegaMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Query service for {@link UsuarioCentroBodega}.
 */
@Service
@Transactional(readOnly = true)
public class UsuarioCentroBodegaQueryService extends QueryService<UsuarioCentroBodega> {

    private static final Logger LOG = LoggerFactory.getLogger(UsuarioCentroBodegaQueryService.class);

    private final UsuarioCentroBodegaRepository repository;
    private final UsuarioCentroBodegaMapper mapper;

    public UsuarioCentroBodegaQueryService(UsuarioCentroBodegaRepository repository, UsuarioCentroBodegaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<UsuarioCentroBodegaDTO> findByCriteria(UsuarioCentroBodegaCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        return mapper.toDto(repository.findAll(createSpecification(criteria)));
    }

    @Transactional(readOnly = true)
    public long countByCriteria(UsuarioCentroBodegaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        return repository.count(createSpecification(criteria));
    }

    protected Specification<UsuarioCentroBodega> createSpecification(UsuarioCentroBodegaCriteria criteria) {
        Specification<UsuarioCentroBodega> specification = Specification.where(null);
        if (criteria != null) {
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), UsuarioCentroBodega_.id),
                buildRangeSpecification(criteria.getNoCia(), UsuarioCentroBodega_.noCia),
                buildSpecification(criteria.getPrincipal(), UsuarioCentroBodega_.principal),
                buildSpecification(criteria.getCentroId(), root -> root.join(UsuarioCentroBodega_.centro, JoinType.LEFT).get(Centro_.id)),
                buildSpecification(criteria.getUserId(), root -> root.join(UsuarioCentroBodega_.user, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getBodegaId(), root -> root.join(UsuarioCentroBodega_.bodega, JoinType.LEFT).get(Bodega_.id))
            );
        }
        return specification;
    }
}
