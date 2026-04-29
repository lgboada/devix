package com.devix.service;

import com.devix.domain.Bodega;
import com.devix.repository.BodegaRepository;
import com.devix.service.dto.BodegaDTO;
import com.devix.service.mapper.BodegaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing {@link Bodega}.
 */
@Service
@Transactional
public class BodegaService {

    private static final Logger LOG = LoggerFactory.getLogger(BodegaService.class);

    private final BodegaRepository bodegaRepository;

    private final BodegaMapper bodegaMapper;

    public BodegaService(BodegaRepository bodegaRepository, BodegaMapper bodegaMapper) {
        this.bodegaRepository = bodegaRepository;
        this.bodegaMapper = bodegaMapper;
    }

    public BodegaDTO save(BodegaDTO bodegaDTO) {
        LOG.debug("Request to save Bodega : {}", bodegaDTO);
        Bodega bodega = bodegaMapper.toEntity(bodegaDTO);
        bodega = bodegaRepository.save(bodega);
        return bodegaMapper.toDto(bodega);
    }

    public BodegaDTO update(BodegaDTO bodegaDTO) {
        LOG.debug("Request to update Bodega : {}", bodegaDTO);
        Bodega bodega = bodegaMapper.toEntity(bodegaDTO);
        bodega = bodegaRepository.save(bodega);
        return bodegaMapper.toDto(bodega);
    }

    public Optional<BodegaDTO> partialUpdate(BodegaDTO bodegaDTO) {
        LOG.debug("Request to partially update Bodega : {}", bodegaDTO);

        return bodegaRepository
            .findById(bodegaDTO.getId())
            .map(existingBodega -> {
                bodegaMapper.partialUpdate(existingBodega, bodegaDTO);

                return existingBodega;
            })
            .map(bodegaRepository::save)
            .map(bodegaMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<BodegaDTO> findOne(Long id) {
        LOG.debug("Request to get Bodega : {}", id);
        return bodegaRepository.findById(id).map(bodegaMapper::toDto);
    }

    public void delete(Long id) {
        LOG.debug("Request to delete Bodega : {}", id);
        bodegaRepository.deleteById(id);
    }
}
