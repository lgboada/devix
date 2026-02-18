package com.devix.service;

import com.devix.domain.DetalleFactura;
import com.devix.repository.DetalleFacturaRepository;
import com.devix.service.dto.DetalleFacturaDTO;
import com.devix.service.mapper.DetalleFacturaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devix.domain.DetalleFactura}.
 */
@Service
@Transactional
public class DetalleFacturaService {

    private static final Logger LOG = LoggerFactory.getLogger(DetalleFacturaService.class);

    private final DetalleFacturaRepository detalleFacturaRepository;

    private final DetalleFacturaMapper detalleFacturaMapper;

    public DetalleFacturaService(DetalleFacturaRepository detalleFacturaRepository, DetalleFacturaMapper detalleFacturaMapper) {
        this.detalleFacturaRepository = detalleFacturaRepository;
        this.detalleFacturaMapper = detalleFacturaMapper;
    }

    /**
     * Save a detalleFactura.
     *
     * @param detalleFacturaDTO the entity to save.
     * @return the persisted entity.
     */
    public DetalleFacturaDTO save(DetalleFacturaDTO detalleFacturaDTO) {
        LOG.debug("Request to save DetalleFactura : {}", detalleFacturaDTO);
        DetalleFactura detalleFactura = detalleFacturaMapper.toEntity(detalleFacturaDTO);
        detalleFactura = detalleFacturaRepository.save(detalleFactura);
        return detalleFacturaMapper.toDto(detalleFactura);
    }

    /**
     * Update a detalleFactura.
     *
     * @param detalleFacturaDTO the entity to save.
     * @return the persisted entity.
     */
    public DetalleFacturaDTO update(DetalleFacturaDTO detalleFacturaDTO) {
        LOG.debug("Request to update DetalleFactura : {}", detalleFacturaDTO);
        DetalleFactura detalleFactura = detalleFacturaMapper.toEntity(detalleFacturaDTO);
        detalleFactura = detalleFacturaRepository.save(detalleFactura);
        return detalleFacturaMapper.toDto(detalleFactura);
    }

    /**
     * Partially update a detalleFactura.
     *
     * @param detalleFacturaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DetalleFacturaDTO> partialUpdate(DetalleFacturaDTO detalleFacturaDTO) {
        LOG.debug("Request to partially update DetalleFactura : {}", detalleFacturaDTO);

        return detalleFacturaRepository
            .findById(detalleFacturaDTO.getId())
            .map(existingDetalleFactura -> {
                detalleFacturaMapper.partialUpdate(existingDetalleFactura, detalleFacturaDTO);

                return existingDetalleFactura;
            })
            .map(detalleFacturaRepository::save)
            .map(detalleFacturaMapper::toDto);
    }

    /**
     * Get one detalleFactura by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DetalleFacturaDTO> findOne(Long id) {
        LOG.debug("Request to get DetalleFactura : {}", id);
        return detalleFacturaRepository.findById(id).map(detalleFacturaMapper::toDto);
    }

    /**
     * Delete the detalleFactura by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DetalleFactura : {}", id);
        detalleFacturaRepository.deleteById(id);
    }
}
