package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.Bodega;
import com.devix.repository.BodegaRepository;
import com.devix.service.criteria.BodegaCriteria;
import com.devix.service.dto.BodegaDTO;
import com.devix.service.mapper.BodegaMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Query service for {@link Bodega}.
 */
@Service
@Transactional(readOnly = true)
public class BodegaQueryService extends QueryService<Bodega> {

    private static final Logger LOG = LoggerFactory.getLogger(BodegaQueryService.class);

    private final BodegaRepository bodegaRepository;

    private final BodegaMapper bodegaMapper;

    public BodegaQueryService(BodegaRepository bodegaRepository, BodegaMapper bodegaMapper) {
        this.bodegaRepository = bodegaRepository;
        this.bodegaMapper = bodegaMapper;
    }

    @Transactional(readOnly = true)
    public List<BodegaDTO> findByCriteria(BodegaCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Bodega> specification = createSpecification(criteria);
        return bodegaMapper.toDto(bodegaRepository.findAll(specification));
    }

    @Transactional(readOnly = true)
    public long countByCriteria(BodegaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Bodega> specification = createSpecification(criteria);
        return bodegaRepository.count(specification);
    }

    protected Specification<Bodega> createSpecification(BodegaCriteria criteria) {
        Specification<Bodega> specification = Specification.where(null);
        if (criteria != null) {
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Bodega_.id),
                buildRangeSpecification(criteria.getNoCia(), Bodega_.noCia),
                buildStringSpecification(criteria.getCodigo(), Bodega_.codigo),
                buildStringSpecification(criteria.getNombre(), Bodega_.nombre),
                buildSpecification(criteria.getActiva(), Bodega_.activa),
                buildSpecification(criteria.getCentroId(), root -> root.join(Bodega_.centro, JoinType.LEFT).get(Centro_.id))
            );
        }
        return specification;
    }
}
