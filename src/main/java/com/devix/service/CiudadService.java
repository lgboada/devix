package com.devix.service;

import com.devix.domain.Ciudad;
import com.devix.repository.CiudadRepository;
import com.devix.service.dto.CiudadDTO;
import com.devix.service.mapper.CiudadMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devix.domain.Ciudad}.
 */
@Service
@Transactional
public class CiudadService {

    private static final Logger LOG = LoggerFactory.getLogger(CiudadService.class);

    private final CiudadRepository ciudadRepository;

    private final CiudadMapper ciudadMapper;

    public CiudadService(CiudadRepository ciudadRepository, CiudadMapper ciudadMapper) {
        this.ciudadRepository = ciudadRepository;
        this.ciudadMapper = ciudadMapper;
    }

    /**
     * Save a ciudad.
     *
     * @param ciudadDTO the entity to save.
     * @return the persisted entity.
     */
    public CiudadDTO save(CiudadDTO ciudadDTO) {
        LOG.debug("Request to save Ciudad : {}", ciudadDTO);
        Ciudad ciudad = ciudadMapper.toEntity(ciudadDTO);
        ciudad = ciudadRepository.save(ciudad);
        return ciudadMapper.toDto(ciudad);
    }

    /**
     * Update a ciudad.
     *
     * @param ciudadDTO the entity to save.
     * @return the persisted entity.
     */
    public CiudadDTO update(CiudadDTO ciudadDTO) {
        LOG.debug("Request to update Ciudad : {}", ciudadDTO);
        Ciudad ciudad = ciudadMapper.toEntity(ciudadDTO);
        ciudad = ciudadRepository.save(ciudad);
        return ciudadMapper.toDto(ciudad);
    }

    /**
     * Partially update a ciudad.
     *
     * @param ciudadDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CiudadDTO> partialUpdate(CiudadDTO ciudadDTO) {
        LOG.debug("Request to partially update Ciudad : {}", ciudadDTO);

        return ciudadRepository
            .findById(ciudadDTO.getId())
            .map(existingCiudad -> {
                ciudadMapper.partialUpdate(existingCiudad, ciudadDTO);

                return existingCiudad;
            })
            .map(ciudadRepository::save)
            .map(ciudadMapper::toDto);
    }

    /**
     * Get one ciudad by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CiudadDTO> findOne(Long id) {
        LOG.debug("Request to get Ciudad : {}", id);
        return ciudadRepository.findById(id).map(ciudadMapper::toDto);
    }

    /**
     * Delete the ciudad by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Ciudad : {}", id);
        ciudadRepository.deleteById(id);
    }
}
