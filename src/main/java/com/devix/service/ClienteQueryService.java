package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.Cliente;
import com.devix.repository.ClienteRepository;
import com.devix.service.criteria.ClienteCriteria;
import com.devix.service.dto.ClienteDTO;
import com.devix.service.mapper.ClienteMapper;
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
 * Service for executing complex queries for {@link Cliente} entities in the database.
 * The main input is a {@link ClienteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ClienteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClienteQueryService extends QueryService<Cliente> {

    private static final Logger LOG = LoggerFactory.getLogger(ClienteQueryService.class);

    private final ClienteRepository clienteRepository;

    private final ClienteMapper clienteMapper;

    public ClienteQueryService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    /**
     * Return a {@link Page} of {@link ClienteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClienteDTO> findByCriteria(ClienteCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cliente> specification = createSpecification(criteria);
        return clienteRepository.findAll(specification, page).map(clienteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClienteCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Cliente> specification = createSpecification(criteria);
        return clienteRepository.count(specification);
    }

    /**
     * Function to convert {@link ClienteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cliente> createSpecification(ClienteCriteria criteria) {
        Specification<Cliente> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Cliente_.id),
                buildRangeSpecification(criteria.getNoCia(), Cliente_.noCia),
                buildStringSpecification(criteria.getDni(), Cliente_.dni),
                buildStringSpecification(criteria.getNombres(), Cliente_.nombres),
                buildStringSpecification(criteria.getApellidos(), Cliente_.apellidos),
                buildStringSpecification(criteria.getNombreComercial(), Cliente_.nombreComercial),
                buildStringSpecification(criteria.getEmail(), Cliente_.email),
                buildStringSpecification(criteria.getTelefono(), Cliente_.telefono),
                buildRangeSpecification(criteria.getFechaNacimiento(), Cliente_.fechaNacimiento),
                buildStringSpecification(criteria.getSexo(), Cliente_.sexo),
                buildStringSpecification(criteria.getEstadoCivil(), Cliente_.estadoCivil),
                buildStringSpecification(criteria.getTipoSangre(), Cliente_.tipoSangre),
                buildStringSpecification(criteria.getPathImagen(), Cliente_.pathImagen),
                buildSpecification(criteria.getDireccionesId(), root -> root.join(Cliente_.direcciones, JoinType.LEFT).get(Direccion_.id)),
                buildSpecification(criteria.getFacturasId(), root -> root.join(Cliente_.facturas, JoinType.LEFT).get(Factura_.id)),
                buildSpecification(criteria.getEventoId(), root -> root.join(Cliente_.eventos, JoinType.LEFT).get(Evento_.id)),
                buildSpecification(criteria.getDocumentoId(), root -> root.join(Cliente_.documentos, JoinType.LEFT).get(Documento_.id)),
                buildSpecification(criteria.getTipoClienteId(), root -> root.join(Cliente_.tipoCliente, JoinType.LEFT).get(TipoCliente_.id)
                ),
                buildSpecification(criteria.getCiudadId(), root -> root.join(Cliente_.ciudad, JoinType.LEFT).get(Ciudad_.id))
            );
        }
        return specification;
    }
}
