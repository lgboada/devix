package com.devix.service;

import com.devix.domain.TipoCliente;
import com.devix.repository.TipoClienteRepository;
import com.devix.service.dto.TipoClienteDTO;
import com.devix.service.mapper.TipoClienteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devix.domain.TipoCliente}.
 */
@Service
@Transactional
public class TipoClienteService {

    private static final Logger LOG = LoggerFactory.getLogger(TipoClienteService.class);

    private final TipoClienteRepository tipoClienteRepository;

    private final TipoClienteMapper tipoClienteMapper;

    public TipoClienteService(TipoClienteRepository tipoClienteRepository, TipoClienteMapper tipoClienteMapper) {
        this.tipoClienteRepository = tipoClienteRepository;
        this.tipoClienteMapper = tipoClienteMapper;
    }

    /**
     * Save a tipoCliente.
     *
     * @param tipoClienteDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoClienteDTO save(TipoClienteDTO tipoClienteDTO) {
        LOG.debug("Request to save TipoCliente : {}", tipoClienteDTO);
        TipoCliente tipoCliente = tipoClienteMapper.toEntity(tipoClienteDTO);
        tipoCliente = tipoClienteRepository.save(tipoCliente);
        return tipoClienteMapper.toDto(tipoCliente);
    }

    /**
     * Update a tipoCliente.
     *
     * @param tipoClienteDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoClienteDTO update(TipoClienteDTO tipoClienteDTO) {
        LOG.debug("Request to update TipoCliente : {}", tipoClienteDTO);
        TipoCliente tipoCliente = tipoClienteMapper.toEntity(tipoClienteDTO);
        tipoCliente = tipoClienteRepository.save(tipoCliente);
        return tipoClienteMapper.toDto(tipoCliente);
    }

    /**
     * Partially update a tipoCliente.
     *
     * @param tipoClienteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TipoClienteDTO> partialUpdate(TipoClienteDTO tipoClienteDTO) {
        LOG.debug("Request to partially update TipoCliente : {}", tipoClienteDTO);

        return tipoClienteRepository
            .findById(tipoClienteDTO.getId())
            .map(existingTipoCliente -> {
                tipoClienteMapper.partialUpdate(existingTipoCliente, tipoClienteDTO);

                return existingTipoCliente;
            })
            .map(tipoClienteRepository::save)
            .map(tipoClienteMapper::toDto);
    }

    /**
     * Get one tipoCliente by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TipoClienteDTO> findOne(Long id) {
        LOG.debug("Request to get TipoCliente : {}", id);
        return tipoClienteRepository.findById(id).map(tipoClienteMapper::toDto);
    }

    /**
     * Delete the tipoCliente by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TipoCliente : {}", id);
        tipoClienteRepository.deleteById(id);
    }
}
