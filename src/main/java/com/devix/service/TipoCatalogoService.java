package com.devix.service;

import com.devix.domain.TipoCatalogo;
import com.devix.repository.TipoCatalogoRepository;
import com.devix.service.dto.TipoCatalogoDTO;
import com.devix.service.mapper.TipoCatalogoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devix.domain.TipoCatalogo}.
 */
@Service
@Transactional
public class TipoCatalogoService {

    private static final Logger LOG = LoggerFactory.getLogger(TipoCatalogoService.class);

    private final TipoCatalogoRepository tipoCatalogoRepository;

    private final TipoCatalogoMapper tipoCatalogoMapper;

    public TipoCatalogoService(TipoCatalogoRepository tipoCatalogoRepository, TipoCatalogoMapper tipoCatalogoMapper) {
        this.tipoCatalogoRepository = tipoCatalogoRepository;
        this.tipoCatalogoMapper = tipoCatalogoMapper;
    }

    /**
     * Save a tipoCatalogo.
     *
     * @param tipoCatalogoDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoCatalogoDTO save(TipoCatalogoDTO tipoCatalogoDTO) {
        LOG.debug("Request to save TipoCatalogo : {}", tipoCatalogoDTO);
        TipoCatalogo tipoCatalogo = tipoCatalogoMapper.toEntity(tipoCatalogoDTO);
        tipoCatalogo = tipoCatalogoRepository.save(tipoCatalogo);
        return tipoCatalogoMapper.toDto(tipoCatalogo);
    }

    /**
     * Update a tipoCatalogo.
     *
     * @param tipoCatalogoDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoCatalogoDTO update(TipoCatalogoDTO tipoCatalogoDTO) {
        LOG.debug("Request to update TipoCatalogo : {}", tipoCatalogoDTO);
        TipoCatalogo tipoCatalogo = tipoCatalogoMapper.toEntity(tipoCatalogoDTO);
        tipoCatalogo = tipoCatalogoRepository.save(tipoCatalogo);
        return tipoCatalogoMapper.toDto(tipoCatalogo);
    }

    /**
     * Partially update a tipoCatalogo.
     *
     * @param tipoCatalogoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TipoCatalogoDTO> partialUpdate(TipoCatalogoDTO tipoCatalogoDTO) {
        LOG.debug("Request to partially update TipoCatalogo : {}", tipoCatalogoDTO);

        return tipoCatalogoRepository
            .findById(tipoCatalogoDTO.getId())
            .map(existingTipoCatalogo -> {
                tipoCatalogoMapper.partialUpdate(existingTipoCatalogo, tipoCatalogoDTO);

                return existingTipoCatalogo;
            })
            .map(tipoCatalogoRepository::save)
            .map(tipoCatalogoMapper::toDto);
    }

    /**
     * Get one tipoCatalogo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TipoCatalogoDTO> findOne(Long id) {
        LOG.debug("Request to get TipoCatalogo : {}", id);
        return tipoCatalogoRepository.findById(id).map(tipoCatalogoMapper::toDto);
    }

    /**
     * Delete the tipoCatalogo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TipoCatalogo : {}", id);
        tipoCatalogoRepository.deleteById(id);
    }
}
