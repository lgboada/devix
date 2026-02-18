package com.devix.service;

import com.devix.domain.Marca;
import com.devix.repository.MarcaRepository;
import com.devix.service.dto.MarcaDTO;
import com.devix.service.mapper.MarcaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devix.domain.Marca}.
 */
@Service
@Transactional
public class MarcaService {

    private static final Logger LOG = LoggerFactory.getLogger(MarcaService.class);

    private final MarcaRepository marcaRepository;

    private final MarcaMapper marcaMapper;

    public MarcaService(MarcaRepository marcaRepository, MarcaMapper marcaMapper) {
        this.marcaRepository = marcaRepository;
        this.marcaMapper = marcaMapper;
    }

    /**
     * Save a marca.
     *
     * @param marcaDTO the entity to save.
     * @return the persisted entity.
     */
    public MarcaDTO save(MarcaDTO marcaDTO) {
        LOG.debug("Request to save Marca : {}", marcaDTO);
        Marca marca = marcaMapper.toEntity(marcaDTO);
        marca = marcaRepository.save(marca);
        return marcaMapper.toDto(marca);
    }

    /**
     * Update a marca.
     *
     * @param marcaDTO the entity to save.
     * @return the persisted entity.
     */
    public MarcaDTO update(MarcaDTO marcaDTO) {
        LOG.debug("Request to update Marca : {}", marcaDTO);
        Marca marca = marcaMapper.toEntity(marcaDTO);
        marca = marcaRepository.save(marca);
        return marcaMapper.toDto(marca);
    }

    /**
     * Partially update a marca.
     *
     * @param marcaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MarcaDTO> partialUpdate(MarcaDTO marcaDTO) {
        LOG.debug("Request to partially update Marca : {}", marcaDTO);

        return marcaRepository
            .findById(marcaDTO.getId())
            .map(existingMarca -> {
                marcaMapper.partialUpdate(existingMarca, marcaDTO);

                return existingMarca;
            })
            .map(marcaRepository::save)
            .map(marcaMapper::toDto);
    }

    /**
     * Get one marca by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MarcaDTO> findOne(Long id) {
        LOG.debug("Request to get Marca : {}", id);
        return marcaRepository.findById(id).map(marcaMapper::toDto);
    }

    /**
     * Delete the marca by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Marca : {}", id);
        marcaRepository.deleteById(id);
    }
}
