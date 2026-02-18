package com.devix.service;

import com.devix.domain.Modelo;
import com.devix.repository.ModeloRepository;
import com.devix.service.dto.ModeloDTO;
import com.devix.service.mapper.ModeloMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devix.domain.Modelo}.
 */
@Service
@Transactional
public class ModeloService {

    private static final Logger LOG = LoggerFactory.getLogger(ModeloService.class);

    private final ModeloRepository modeloRepository;

    private final ModeloMapper modeloMapper;

    public ModeloService(ModeloRepository modeloRepository, ModeloMapper modeloMapper) {
        this.modeloRepository = modeloRepository;
        this.modeloMapper = modeloMapper;
    }

    /**
     * Save a modelo.
     *
     * @param modeloDTO the entity to save.
     * @return the persisted entity.
     */
    public ModeloDTO save(ModeloDTO modeloDTO) {
        LOG.debug("Request to save Modelo : {}", modeloDTO);
        Modelo modelo = modeloMapper.toEntity(modeloDTO);
        modelo = modeloRepository.save(modelo);
        return modeloMapper.toDto(modelo);
    }

    /**
     * Update a modelo.
     *
     * @param modeloDTO the entity to save.
     * @return the persisted entity.
     */
    public ModeloDTO update(ModeloDTO modeloDTO) {
        LOG.debug("Request to update Modelo : {}", modeloDTO);
        Modelo modelo = modeloMapper.toEntity(modeloDTO);
        modelo = modeloRepository.save(modelo);
        return modeloMapper.toDto(modelo);
    }

    /**
     * Partially update a modelo.
     *
     * @param modeloDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ModeloDTO> partialUpdate(ModeloDTO modeloDTO) {
        LOG.debug("Request to partially update Modelo : {}", modeloDTO);

        return modeloRepository
            .findById(modeloDTO.getId())
            .map(existingModelo -> {
                modeloMapper.partialUpdate(existingModelo, modeloDTO);

                return existingModelo;
            })
            .map(modeloRepository::save)
            .map(modeloMapper::toDto);
    }

    /**
     * Get one modelo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ModeloDTO> findOne(Long id) {
        LOG.debug("Request to get Modelo : {}", id);
        return modeloRepository.findById(id).map(modeloMapper::toDto);
    }

    /**
     * Delete the modelo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Modelo : {}", id);
        modeloRepository.deleteById(id);
    }
}
