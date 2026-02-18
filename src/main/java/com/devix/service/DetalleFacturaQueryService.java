package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.DetalleFactura;
import com.devix.repository.DetalleFacturaRepository;
import com.devix.service.criteria.DetalleFacturaCriteria;
import com.devix.service.dto.DetalleFacturaDTO;
import com.devix.service.mapper.DetalleFacturaMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DetalleFactura} entities in the database.
 * The main input is a {@link DetalleFacturaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DetalleFacturaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DetalleFacturaQueryService extends QueryService<DetalleFactura> {

    private static final Logger LOG = LoggerFactory.getLogger(DetalleFacturaQueryService.class);

    private final DetalleFacturaRepository detalleFacturaRepository;

    private final DetalleFacturaMapper detalleFacturaMapper;

    public DetalleFacturaQueryService(DetalleFacturaRepository detalleFacturaRepository, DetalleFacturaMapper detalleFacturaMapper) {
        this.detalleFacturaRepository = detalleFacturaRepository;
        this.detalleFacturaMapper = detalleFacturaMapper;
    }

    /**
     * Return a {@link Page} of {@link DetalleFacturaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DetalleFacturaDTO> findByCriteria(DetalleFacturaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DetalleFactura> specification = createSpecification(criteria);
        return detalleFacturaRepository.findAll(specification, page).map(detalleFacturaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DetalleFacturaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DetalleFactura> specification = createSpecification(criteria);
        return detalleFacturaRepository.count(specification);
    }

    /**
     * Function to convert {@link DetalleFacturaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DetalleFactura> createSpecification(DetalleFacturaCriteria criteria) {
        Specification<DetalleFactura> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DetalleFactura_.id),
                buildRangeSpecification(criteria.getNoCia(), DetalleFactura_.noCia),
                buildRangeSpecification(criteria.getCantidad(), DetalleFactura_.cantidad),
                buildRangeSpecification(criteria.getPrecioUnitario(), DetalleFactura_.precioUnitario),
                buildRangeSpecification(criteria.getSubtotal(), DetalleFactura_.subtotal),
                buildRangeSpecification(criteria.getDescuento(), DetalleFactura_.descuento),
                buildRangeSpecification(criteria.getImpuesto(), DetalleFactura_.impuesto),
                buildRangeSpecification(criteria.getTotal(), DetalleFactura_.total),
                buildSpecification(criteria.getFacturaId(), root -> root.join(DetalleFactura_.factura, JoinType.LEFT).get(Factura_.id)),
                buildSpecification(criteria.getProductoId(), root -> root.join(DetalleFactura_.producto, JoinType.LEFT).get(Producto_.id))
            );
        }
        return specification;
    }
}
