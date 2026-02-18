package com.devix.service;

import com.devix.domain.TipoProducto;
import com.devix.repository.TipoProductoRepository;
import com.devix.service.dto.TipoProductoDTO;
import com.devix.service.mapper.TipoProductoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devix.domain.TipoProducto}.
 */
@Service
@Transactional
public class TipoProductoService {

    private static final Logger LOG = LoggerFactory.getLogger(TipoProductoService.class);

    private final TipoProductoRepository tipoProductoRepository;

    private final TipoProductoMapper tipoProductoMapper;

    public TipoProductoService(TipoProductoRepository tipoProductoRepository, TipoProductoMapper tipoProductoMapper) {
        this.tipoProductoRepository = tipoProductoRepository;
        this.tipoProductoMapper = tipoProductoMapper;
    }

    /**
     * Save a tipoProducto.
     *
     * @param tipoProductoDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoProductoDTO save(TipoProductoDTO tipoProductoDTO) {
        LOG.debug("Request to save TipoProducto : {}", tipoProductoDTO);
        TipoProducto tipoProducto = tipoProductoMapper.toEntity(tipoProductoDTO);
        tipoProducto = tipoProductoRepository.save(tipoProducto);
        return tipoProductoMapper.toDto(tipoProducto);
    }

    /**
     * Update a tipoProducto.
     *
     * @param tipoProductoDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoProductoDTO update(TipoProductoDTO tipoProductoDTO) {
        LOG.debug("Request to update TipoProducto : {}", tipoProductoDTO);
        TipoProducto tipoProducto = tipoProductoMapper.toEntity(tipoProductoDTO);
        tipoProducto = tipoProductoRepository.save(tipoProducto);
        return tipoProductoMapper.toDto(tipoProducto);
    }

    /**
     * Partially update a tipoProducto.
     *
     * @param tipoProductoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TipoProductoDTO> partialUpdate(TipoProductoDTO tipoProductoDTO) {
        LOG.debug("Request to partially update TipoProducto : {}", tipoProductoDTO);

        return tipoProductoRepository
            .findById(tipoProductoDTO.getId())
            .map(existingTipoProducto -> {
                tipoProductoMapper.partialUpdate(existingTipoProducto, tipoProductoDTO);

                return existingTipoProducto;
            })
            .map(tipoProductoRepository::save)
            .map(tipoProductoMapper::toDto);
    }

    /**
     * Get one tipoProducto by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TipoProductoDTO> findOne(Long id) {
        LOG.debug("Request to get TipoProducto : {}", id);
        return tipoProductoRepository.findById(id).map(tipoProductoMapper::toDto);
    }

    /**
     * Delete the tipoProducto by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TipoProducto : {}", id);
        tipoProductoRepository.deleteById(id);
    }
}
