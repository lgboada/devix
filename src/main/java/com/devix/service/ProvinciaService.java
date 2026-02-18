package com.devix.service;

import com.devix.domain.Provincia;
import com.devix.repository.ProvinciaRepository;
import com.devix.service.dto.ProvinciaDTO;
import com.devix.service.mapper.ProvinciaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devix.domain.Provincia}.
 */
@Service
@Transactional
public class ProvinciaService {

    private static final Logger LOG = LoggerFactory.getLogger(ProvinciaService.class);

    private final ProvinciaRepository provinciaRepository;

    private final ProvinciaMapper provinciaMapper;

    public ProvinciaService(ProvinciaRepository provinciaRepository, ProvinciaMapper provinciaMapper) {
        this.provinciaRepository = provinciaRepository;
        this.provinciaMapper = provinciaMapper;
    }

    /**
     * Save a provincia.
     *
     * @param provinciaDTO the entity to save.
     * @return the persisted entity.
     */
    public ProvinciaDTO save(ProvinciaDTO provinciaDTO) {
        LOG.debug("Request to save Provincia : {}", provinciaDTO);
        Provincia provincia = provinciaMapper.toEntity(provinciaDTO);
        provincia = provinciaRepository.save(provincia);
        return provinciaMapper.toDto(provincia);
    }

    /**
     * Update a provincia.
     *
     * @param provinciaDTO the entity to save.
     * @return the persisted entity.
     */
    public ProvinciaDTO update(ProvinciaDTO provinciaDTO) {
        LOG.debug("Request to update Provincia : {}", provinciaDTO);
        Provincia provincia = provinciaMapper.toEntity(provinciaDTO);
        provincia = provinciaRepository.save(provincia);
        return provinciaMapper.toDto(provincia);
    }

    /**
     * Partially update a provincia.
     *
     * @param provinciaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProvinciaDTO> partialUpdate(ProvinciaDTO provinciaDTO) {
        LOG.debug("Request to partially update Provincia : {}", provinciaDTO);

        return provinciaRepository
            .findById(provinciaDTO.getId())
            .map(existingProvincia -> {
                provinciaMapper.partialUpdate(existingProvincia, provinciaDTO);

                return existingProvincia;
            })
            .map(provinciaRepository::save)
            .map(provinciaMapper::toDto);
    }

    /**
     * Get one provincia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProvinciaDTO> findOne(Long id) {
        LOG.debug("Request to get Provincia : {}", id);
        return provinciaRepository.findById(id).map(provinciaMapper::toDto);
    }

    /**
     * Delete the provincia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Provincia : {}", id);
        provinciaRepository.deleteById(id);
    }
}
