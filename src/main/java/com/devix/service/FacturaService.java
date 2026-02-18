package com.devix.service;

import com.devix.domain.Factura;
import com.devix.repository.FacturaRepository;
import com.devix.service.dto.FacturaDTO;
import com.devix.service.mapper.FacturaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devix.domain.Factura}.
 */
@Service
@Transactional
public class FacturaService {

    private static final Logger LOG = LoggerFactory.getLogger(FacturaService.class);

    private final FacturaRepository facturaRepository;

    private final FacturaMapper facturaMapper;

    public FacturaService(FacturaRepository facturaRepository, FacturaMapper facturaMapper) {
        this.facturaRepository = facturaRepository;
        this.facturaMapper = facturaMapper;
    }

    /**
     * Save a factura.
     *
     * @param facturaDTO the entity to save.
     * @return the persisted entity.
     */
    public FacturaDTO save(FacturaDTO facturaDTO) {
        LOG.debug("Request to save Factura : {}", facturaDTO);
        Factura factura = facturaMapper.toEntity(facturaDTO);
        factura = facturaRepository.save(factura);
        return facturaMapper.toDto(factura);
    }

    /**
     * Update a factura.
     *
     * @param facturaDTO the entity to save.
     * @return the persisted entity.
     */
    public FacturaDTO update(FacturaDTO facturaDTO) {
        LOG.debug("Request to update Factura : {}", facturaDTO);
        Factura factura = facturaMapper.toEntity(facturaDTO);
        factura = facturaRepository.save(factura);
        return facturaMapper.toDto(factura);
    }

    /**
     * Partially update a factura.
     *
     * @param facturaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FacturaDTO> partialUpdate(FacturaDTO facturaDTO) {
        LOG.debug("Request to partially update Factura : {}", facturaDTO);

        return facturaRepository
            .findById(facturaDTO.getId())
            .map(existingFactura -> {
                facturaMapper.partialUpdate(existingFactura, facturaDTO);

                return existingFactura;
            })
            .map(facturaRepository::save)
            .map(facturaMapper::toDto);
    }

    /**
     * Get one factura by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FacturaDTO> findOne(Long id) {
        LOG.debug("Request to get Factura : {}", id);
        return facturaRepository.findById(id).map(facturaMapper::toDto);
    }

    /**
     * Delete the factura by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Factura : {}", id);
        facturaRepository.deleteById(id);
    }
}
