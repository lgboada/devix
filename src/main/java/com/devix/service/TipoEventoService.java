package com.devix.service;

import com.devix.domain.TipoEvento;
import com.devix.repository.TipoEventoRepository;
import com.devix.service.dto.TipoEventoDTO;
import com.devix.service.mapper.TipoEventoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devix.domain.TipoEvento}.
 */
@Service
@Transactional
public class TipoEventoService {

    private static final Logger LOG = LoggerFactory.getLogger(TipoEventoService.class);

    private final TipoEventoRepository tipoEventoRepository;

    private final TipoEventoMapper tipoEventoMapper;

    public TipoEventoService(TipoEventoRepository tipoEventoRepository, TipoEventoMapper tipoEventoMapper) {
        this.tipoEventoRepository = tipoEventoRepository;
        this.tipoEventoMapper = tipoEventoMapper;
    }

    /**
     * Save a tipoEvento.
     *
     * @param tipoEventoDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoEventoDTO save(TipoEventoDTO tipoEventoDTO) {
        LOG.debug("Request to save TipoEvento : {}", tipoEventoDTO);
        TipoEvento tipoEvento = tipoEventoMapper.toEntity(tipoEventoDTO);
        tipoEvento = tipoEventoRepository.save(tipoEvento);
        return tipoEventoMapper.toDto(tipoEvento);
    }

    /**
     * Update a tipoEvento.
     *
     * @param tipoEventoDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoEventoDTO update(TipoEventoDTO tipoEventoDTO) {
        LOG.debug("Request to update TipoEvento : {}", tipoEventoDTO);
        TipoEvento tipoEvento = tipoEventoMapper.toEntity(tipoEventoDTO);
        tipoEvento = tipoEventoRepository.save(tipoEvento);
        return tipoEventoMapper.toDto(tipoEvento);
    }

    /**
     * Partially update a tipoEvento.
     *
     * @param tipoEventoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TipoEventoDTO> partialUpdate(TipoEventoDTO tipoEventoDTO) {
        LOG.debug("Request to partially update TipoEvento : {}", tipoEventoDTO);

        return tipoEventoRepository
            .findById(tipoEventoDTO.getId())
            .map(existingTipoEvento -> {
                tipoEventoMapper.partialUpdate(existingTipoEvento, tipoEventoDTO);

                return existingTipoEvento;
            })
            .map(tipoEventoRepository::save)
            .map(tipoEventoMapper::toDto);
    }

    /**
     * Get one tipoEvento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TipoEventoDTO> findOne(Long id) {
        LOG.debug("Request to get TipoEvento : {}", id);
        return tipoEventoRepository.findById(id).map(tipoEventoMapper::toDto);
    }

    /**
     * Delete the tipoEvento by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TipoEvento : {}", id);
        tipoEventoRepository.deleteById(id);
    }
}
