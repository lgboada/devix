package com.devix.service;

import com.devix.domain.Centro;
import com.devix.repository.CentroRepository;
import com.devix.service.dto.CentroDTO;
import com.devix.service.mapper.CentroMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devix.domain.Centro}.
 */
@Service
@Transactional
public class CentroService {

    private static final Logger LOG = LoggerFactory.getLogger(CentroService.class);

    private final CentroRepository centroRepository;

    private final CentroMapper centroMapper;

    public CentroService(CentroRepository centroRepository, CentroMapper centroMapper) {
        this.centroRepository = centroRepository;
        this.centroMapper = centroMapper;
    }

    /**
     * Save a centro.
     *
     * @param centroDTO the entity to save.
     * @return the persisted entity.
     */
    public CentroDTO save(CentroDTO centroDTO) {
        LOG.debug("Request to save Centro : {}", centroDTO);
        Centro centro = centroMapper.toEntity(centroDTO);
        centro = centroRepository.save(centro);
        return centroMapper.toDto(centro);
    }

    /**
     * Update a centro.
     *
     * @param centroDTO the entity to save.
     * @return the persisted entity.
     */
    public CentroDTO update(CentroDTO centroDTO) {
        LOG.debug("Request to update Centro : {}", centroDTO);
        Centro centro = centroMapper.toEntity(centroDTO);
        centro = centroRepository.save(centro);
        return centroMapper.toDto(centro);
    }

    /**
     * Partially update a centro.
     *
     * @param centroDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CentroDTO> partialUpdate(CentroDTO centroDTO) {
        LOG.debug("Request to partially update Centro : {}", centroDTO);

        return centroRepository
            .findById(centroDTO.getId())
            .map(existingCentro -> {
                centroMapper.partialUpdate(existingCentro, centroDTO);

                return existingCentro;
            })
            .map(centroRepository::save)
            .map(centroMapper::toDto);
    }

    /**
     * Get one centro by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CentroDTO> findOne(Long id) {
        LOG.debug("Request to get Centro : {}", id);
        return centroRepository.findById(id).map(centroMapper::toDto);
    }

    /**
     * Delete the centro by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Centro : {}", id);
        centroRepository.deleteById(id);
    }
}
