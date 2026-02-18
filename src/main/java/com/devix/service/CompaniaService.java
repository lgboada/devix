package com.devix.service;

import com.devix.domain.Compania;
import com.devix.repository.CompaniaRepository;
import com.devix.service.dto.CompaniaDTO;
import com.devix.service.mapper.CompaniaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devix.domain.Compania}.
 */
@Service
@Transactional
public class CompaniaService {

    private static final Logger LOG = LoggerFactory.getLogger(CompaniaService.class);

    private final CompaniaRepository companiaRepository;

    private final CompaniaMapper companiaMapper;

    public CompaniaService(CompaniaRepository companiaRepository, CompaniaMapper companiaMapper) {
        this.companiaRepository = companiaRepository;
        this.companiaMapper = companiaMapper;
    }

    /**
     * Save a compania.
     *
     * @param companiaDTO the entity to save.
     * @return the persisted entity.
     */
    public CompaniaDTO save(CompaniaDTO companiaDTO) {
        LOG.debug("Request to save Compania : {}", companiaDTO);
        Compania compania = companiaMapper.toEntity(companiaDTO);
        compania = companiaRepository.save(compania);
        return companiaMapper.toDto(compania);
    }

    /**
     * Update a compania.
     *
     * @param companiaDTO the entity to save.
     * @return the persisted entity.
     */
    public CompaniaDTO update(CompaniaDTO companiaDTO) {
        LOG.debug("Request to update Compania : {}", companiaDTO);
        Compania compania = companiaMapper.toEntity(companiaDTO);
        compania = companiaRepository.save(compania);
        return companiaMapper.toDto(compania);
    }

    /**
     * Partially update a compania.
     *
     * @param companiaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompaniaDTO> partialUpdate(CompaniaDTO companiaDTO) {
        LOG.debug("Request to partially update Compania : {}", companiaDTO);

        return companiaRepository
            .findById(companiaDTO.getId())
            .map(existingCompania -> {
                companiaMapper.partialUpdate(existingCompania, companiaDTO);

                return existingCompania;
            })
            .map(companiaRepository::save)
            .map(companiaMapper::toDto);
    }

    /**
     * Get one compania by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompaniaDTO> findOne(Long id) {
        LOG.debug("Request to get Compania : {}", id);
        return companiaRepository.findById(id).map(companiaMapper::toDto);
    }

    /**
     * Delete the compania by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Compania : {}", id);
        companiaRepository.deleteById(id);
    }
}
