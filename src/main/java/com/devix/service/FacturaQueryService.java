package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.Factura;
import com.devix.repository.FacturaRepository;
import com.devix.service.criteria.FacturaCriteria;
import com.devix.service.dto.FacturaDTO;
import com.devix.service.mapper.FacturaMapper;
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
 * Service for executing complex queries for {@link Factura} entities in the database.
 * The main input is a {@link FacturaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FacturaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacturaQueryService extends QueryService<Factura> {

    private static final Logger LOG = LoggerFactory.getLogger(FacturaQueryService.class);

    private final FacturaRepository facturaRepository;

    private final FacturaMapper facturaMapper;

    public FacturaQueryService(FacturaRepository facturaRepository, FacturaMapper facturaMapper) {
        this.facturaRepository = facturaRepository;
        this.facturaMapper = facturaMapper;
    }

    /**
     * Return a {@link Page} of {@link FacturaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FacturaDTO> findByCriteria(FacturaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Factura> specification = createSpecification(criteria);
        return facturaRepository.findAll(specification, page).map(facturaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacturaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Factura> specification = createSpecification(criteria);
        return facturaRepository.count(specification);
    }

    /**
     * Function to convert {@link FacturaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Factura> createSpecification(FacturaCriteria criteria) {
        Specification<Factura> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Factura_.id),
                buildRangeSpecification(criteria.getNoCia(), Factura_.noCia),
                buildStringSpecification(criteria.getSerie(), Factura_.serie),
                buildStringSpecification(criteria.getNoFisico(), Factura_.noFisico),
                buildRangeSpecification(criteria.getFecha(), Factura_.fecha),
                buildRangeSpecification(criteria.getSubtotal(), Factura_.subtotal),
                buildRangeSpecification(criteria.getImpuesto(), Factura_.impuesto),
                buildRangeSpecification(criteria.getImpuestoCero(), Factura_.impuestoCero),
                buildRangeSpecification(criteria.getDescuento(), Factura_.descuento),
                buildRangeSpecification(criteria.getTotal(), Factura_.total),
                buildRangeSpecification(criteria.getPorcentajeImpuesto(), Factura_.porcentajeImpuesto),
                buildStringSpecification(criteria.getCedula(), Factura_.cedula),
                buildStringSpecification(criteria.getDireccion(), Factura_.direccion),
                buildStringSpecification(criteria.getEmail(), Factura_.email),
                buildStringSpecification(criteria.getEstado(), Factura_.estado),
                buildSpecification(criteria.getDetallesId(), root -> root.join(Factura_.detalles, JoinType.LEFT).get(DetalleFactura_.id)),
                buildSpecification(criteria.getCentroId(), root -> root.join(Factura_.centro, JoinType.LEFT).get(Centro_.id)),
                buildSpecification(criteria.getClienteId(), root -> root.join(Factura_.cliente, JoinType.LEFT).get(Cliente_.id))
            );
        }
        return specification;
    }
}
