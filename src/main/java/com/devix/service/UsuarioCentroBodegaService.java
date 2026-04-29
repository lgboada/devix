package com.devix.service;

import com.devix.domain.UsuarioCentroBodega;
import com.devix.repository.UsuarioCentroBodegaRepository;
import com.devix.service.dto.UsuarioCentroBodegaDTO;
import com.devix.service.mapper.UsuarioCentroBodegaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing {@link UsuarioCentroBodega}.
 */
@Service
@Transactional
public class UsuarioCentroBodegaService {

    private static final Logger LOG = LoggerFactory.getLogger(UsuarioCentroBodegaService.class);

    private final UsuarioCentroBodegaRepository usuarioCentroBodegaRepository;

    private final UsuarioCentroBodegaMapper usuarioCentroBodegaMapper;

    public UsuarioCentroBodegaService(
        UsuarioCentroBodegaRepository usuarioCentroBodegaRepository,
        UsuarioCentroBodegaMapper usuarioCentroBodegaMapper
    ) {
        this.usuarioCentroBodegaRepository = usuarioCentroBodegaRepository;
        this.usuarioCentroBodegaMapper = usuarioCentroBodegaMapper;
    }

    public UsuarioCentroBodegaDTO save(UsuarioCentroBodegaDTO dto) {
        LOG.debug("Request to save UsuarioCentroBodega : {}", dto);
        UsuarioCentroBodega entity = usuarioCentroBodegaMapper.toEntity(dto);
        entity = usuarioCentroBodegaRepository.save(entity);
        return usuarioCentroBodegaMapper.toDto(entity);
    }

    public UsuarioCentroBodegaDTO update(UsuarioCentroBodegaDTO dto) {
        LOG.debug("Request to update UsuarioCentroBodega : {}", dto);
        UsuarioCentroBodega entity = usuarioCentroBodegaMapper.toEntity(dto);
        entity = usuarioCentroBodegaRepository.save(entity);
        return usuarioCentroBodegaMapper.toDto(entity);
    }

    public Optional<UsuarioCentroBodegaDTO> partialUpdate(UsuarioCentroBodegaDTO dto) {
        LOG.debug("Request to partially update UsuarioCentroBodega : {}", dto);
        return usuarioCentroBodegaRepository
            .findById(dto.getId())
            .map(existing -> {
                usuarioCentroBodegaMapper.partialUpdate(existing, dto);
                return existing;
            })
            .map(usuarioCentroBodegaRepository::save)
            .map(usuarioCentroBodegaMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioCentroBodegaDTO> findOne(Long id) {
        LOG.debug("Request to get UsuarioCentroBodega : {}", id);
        return usuarioCentroBodegaRepository.findById(id).map(usuarioCentroBodegaMapper::toDto);
    }

    public void delete(Long id) {
        LOG.debug("Request to delete UsuarioCentroBodega : {}", id);
        usuarioCentroBodegaRepository.deleteById(id);
    }
}
