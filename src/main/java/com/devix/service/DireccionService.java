package com.devix.service;

import com.devix.domain.Direccion;
import com.devix.repository.DireccionRepository;
import com.devix.service.dto.DireccionDTO;
import com.devix.service.mapper.DireccionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devix.domain.Direccion}.
 */
@Service
@Transactional
public class DireccionService {

    private static final Logger LOG = LoggerFactory.getLogger(DireccionService.class);

    private final DireccionRepository direccionRepository;

    private final DireccionMapper direccionMapper;

    public DireccionService(DireccionRepository direccionRepository, DireccionMapper direccionMapper) {
        this.direccionRepository = direccionRepository;
        this.direccionMapper = direccionMapper;
    }

    /**
     * Save a direccion.
     *
     * @param direccionDTO the entity to save.
     * @return the persisted entity.
     */
    public DireccionDTO save(DireccionDTO direccionDTO) {
        LOG.debug("Request to save Direccion : {}", direccionDTO);
        Direccion direccion = direccionMapper.toEntity(direccionDTO);
        direccion = direccionRepository.save(direccion);
        return direccionMapper.toDto(direccion);
    }

    /**
     * Update a direccion.
     *
     * @param direccionDTO the entity to save.
     * @return the persisted entity.
     */
    public DireccionDTO update(DireccionDTO direccionDTO) {
        LOG.debug("Request to update Direccion : {}", direccionDTO);
        Direccion direccion = direccionMapper.toEntity(direccionDTO);
        direccion = direccionRepository.save(direccion);
        return direccionMapper.toDto(direccion);
    }

    /**
     * Partially update a direccion.
     *
     * @param direccionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DireccionDTO> partialUpdate(DireccionDTO direccionDTO) {
        LOG.debug("Request to partially update Direccion : {}", direccionDTO);

        return direccionRepository
            .findById(direccionDTO.getId())
            .map(existingDireccion -> {
                direccionMapper.partialUpdate(existingDireccion, direccionDTO);

                return existingDireccion;
            })
            .map(direccionRepository::save)
            .map(direccionMapper::toDto);
    }

    /**
     * Get one direccion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DireccionDTO> findOne(Long id) {
        LOG.debug("Request to get Direccion : {}", id);
        return direccionRepository.findById(id).map(direccionMapper::toDto);
    }

    /**
     * Delete the direccion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Direccion : {}", id);
        direccionRepository.deleteById(id);
    }
}
