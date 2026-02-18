package com.devix.service;

import com.devix.domain.TipoDireccion;
import com.devix.repository.TipoDireccionRepository;
import com.devix.service.dto.TipoDireccionDTO;
import com.devix.service.mapper.TipoDireccionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devix.domain.TipoDireccion}.
 */
@Service
@Transactional
public class TipoDireccionService {

    private static final Logger LOG = LoggerFactory.getLogger(TipoDireccionService.class);

    private final TipoDireccionRepository tipoDireccionRepository;

    private final TipoDireccionMapper tipoDireccionMapper;

    public TipoDireccionService(TipoDireccionRepository tipoDireccionRepository, TipoDireccionMapper tipoDireccionMapper) {
        this.tipoDireccionRepository = tipoDireccionRepository;
        this.tipoDireccionMapper = tipoDireccionMapper;
    }

    /**
     * Save a tipoDireccion.
     *
     * @param tipoDireccionDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoDireccionDTO save(TipoDireccionDTO tipoDireccionDTO) {
        LOG.debug("Request to save TipoDireccion : {}", tipoDireccionDTO);
        TipoDireccion tipoDireccion = tipoDireccionMapper.toEntity(tipoDireccionDTO);
        tipoDireccion = tipoDireccionRepository.save(tipoDireccion);
        return tipoDireccionMapper.toDto(tipoDireccion);
    }

    /**
     * Update a tipoDireccion.
     *
     * @param tipoDireccionDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoDireccionDTO update(TipoDireccionDTO tipoDireccionDTO) {
        LOG.debug("Request to update TipoDireccion : {}", tipoDireccionDTO);
        TipoDireccion tipoDireccion = tipoDireccionMapper.toEntity(tipoDireccionDTO);
        tipoDireccion = tipoDireccionRepository.save(tipoDireccion);
        return tipoDireccionMapper.toDto(tipoDireccion);
    }

    /**
     * Partially update a tipoDireccion.
     *
     * @param tipoDireccionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TipoDireccionDTO> partialUpdate(TipoDireccionDTO tipoDireccionDTO) {
        LOG.debug("Request to partially update TipoDireccion : {}", tipoDireccionDTO);

        return tipoDireccionRepository
            .findById(tipoDireccionDTO.getId())
            .map(existingTipoDireccion -> {
                tipoDireccionMapper.partialUpdate(existingTipoDireccion, tipoDireccionDTO);

                return existingTipoDireccion;
            })
            .map(tipoDireccionRepository::save)
            .map(tipoDireccionMapper::toDto);
    }

    /**
     * Get one tipoDireccion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TipoDireccionDTO> findOne(Long id) {
        LOG.debug("Request to get TipoDireccion : {}", id);
        return tipoDireccionRepository.findById(id).map(tipoDireccionMapper::toDto);
    }

    /**
     * Delete the tipoDireccion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TipoDireccion : {}", id);
        tipoDireccionRepository.deleteById(id);
    }
}
