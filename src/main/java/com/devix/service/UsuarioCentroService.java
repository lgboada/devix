package com.devix.service;

import com.devix.domain.UsuarioCentro;
import com.devix.repository.UsuarioCentroRepository;
import com.devix.service.dto.UsuarioCentroDTO;
import com.devix.service.mapper.UsuarioCentroMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devix.domain.UsuarioCentro}.
 */
@Service
@Transactional
public class UsuarioCentroService {

    private static final Logger LOG = LoggerFactory.getLogger(UsuarioCentroService.class);

    private final UsuarioCentroRepository usuarioCentroRepository;

    private final UsuarioCentroMapper usuarioCentroMapper;

    private final UsuarioCentroDtoEnricher usuarioCentroDtoEnricher;

    public UsuarioCentroService(
        UsuarioCentroRepository usuarioCentroRepository,
        UsuarioCentroMapper usuarioCentroMapper,
        UsuarioCentroDtoEnricher usuarioCentroDtoEnricher
    ) {
        this.usuarioCentroRepository = usuarioCentroRepository;
        this.usuarioCentroMapper = usuarioCentroMapper;
        this.usuarioCentroDtoEnricher = usuarioCentroDtoEnricher;
    }

    /**
     * Save a usuarioCentro.
     *
     * @param usuarioCentroDTO the entity to save.
     * @return the persisted entity.
     */
    public UsuarioCentroDTO save(UsuarioCentroDTO usuarioCentroDTO) {
        LOG.debug("Request to save UsuarioCentro : {}", usuarioCentroDTO);
        UsuarioCentro usuarioCentro = usuarioCentroMapper.toEntity(usuarioCentroDTO);
        usuarioCentro = usuarioCentroRepository.save(usuarioCentro);
        UsuarioCentroDTO dto = usuarioCentroMapper.toDto(usuarioCentro);
        usuarioCentroDtoEnricher.enrichCompaniaNombre(dto);
        return dto;
    }

    /**
     * Update a usuarioCentro.
     *
     * @param usuarioCentroDTO the entity to save.
     * @return the persisted entity.
     */
    public UsuarioCentroDTO update(UsuarioCentroDTO usuarioCentroDTO) {
        LOG.debug("Request to update UsuarioCentro : {}", usuarioCentroDTO);
        UsuarioCentro usuarioCentro = usuarioCentroMapper.toEntity(usuarioCentroDTO);
        usuarioCentro = usuarioCentroRepository.save(usuarioCentro);
        UsuarioCentroDTO dto = usuarioCentroMapper.toDto(usuarioCentro);
        usuarioCentroDtoEnricher.enrichCompaniaNombre(dto);
        return dto;
    }

    /**
     * Partially update a usuarioCentro.
     *
     * @param usuarioCentroDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UsuarioCentroDTO> partialUpdate(UsuarioCentroDTO usuarioCentroDTO) {
        LOG.debug("Request to partially update UsuarioCentro : {}", usuarioCentroDTO);

        return usuarioCentroRepository
            .findById(usuarioCentroDTO.getId())
            .map(existingUsuarioCentro -> {
                usuarioCentroMapper.partialUpdate(existingUsuarioCentro, usuarioCentroDTO);

                return existingUsuarioCentro;
            })
            .map(usuarioCentroRepository::save)
            .map(usuarioCentroMapper::toDto)
            .map(dto -> {
                usuarioCentroDtoEnricher.enrichCompaniaNombre(dto);
                return dto;
            });
    }

    /**
     * Get all the usuarioCentros with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UsuarioCentroDTO> findAllWithEagerRelationships(Pageable pageable) {
        Page<UsuarioCentro> page = usuarioCentroRepository.findAllWithEagerRelationships(pageable);
        List<UsuarioCentroDTO> dtos = usuarioCentroMapper.toDto(page.getContent());
        usuarioCentroDtoEnricher.enrichCompaniaNombre(dtos);
        return new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());
    }

    /**
     * Get one usuarioCentro by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UsuarioCentroDTO> findOne(Long id) {
        LOG.debug("Request to get UsuarioCentro : {}", id);
        return usuarioCentroRepository
            .findOneWithEagerRelationships(id)
            .map(usuarioCentroMapper::toDto)
            .map(dto -> {
                usuarioCentroDtoEnricher.enrichCompaniaNombre(dto);
                return dto;
            });
    }

    /**
     * Delete the usuarioCentro by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UsuarioCentro : {}", id);
        usuarioCentroRepository.deleteById(id);
    }
}
