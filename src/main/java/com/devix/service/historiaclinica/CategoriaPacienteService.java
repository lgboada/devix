package com.devix.service.historiaclinica;

import com.devix.domain.historiaclinica.CategoriaPaciente;
import com.devix.repository.historiaclinica.CategoriaPacienteRepository;
import com.devix.service.historiaclinica.dto.CategoriaPacienteDTO;
import com.devix.service.historiaclinica.mapper.CategoriaPacienteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoriaPacienteService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoriaPacienteService.class);

    private final CategoriaPacienteRepository categoriaPacienteRepository;
    private final CategoriaPacienteMapper categoriaPacienteMapper;

    public CategoriaPacienteService(
        CategoriaPacienteRepository categoriaPacienteRepository,
        CategoriaPacienteMapper categoriaPacienteMapper
    ) {
        this.categoriaPacienteRepository = categoriaPacienteRepository;
        this.categoriaPacienteMapper = categoriaPacienteMapper;
    }

    public CategoriaPacienteDTO save(CategoriaPacienteDTO categoriaPacienteDTO) {
        LOG.debug("Request to save CategoriaPaciente : {}", categoriaPacienteDTO);
        CategoriaPaciente categoriaPaciente = categoriaPacienteMapper.toEntity(categoriaPacienteDTO);
        categoriaPaciente = categoriaPacienteRepository.save(categoriaPaciente);
        return categoriaPacienteMapper.toDto(categoriaPaciente);
    }

    public CategoriaPacienteDTO update(CategoriaPacienteDTO categoriaPacienteDTO) {
        LOG.debug("Request to update CategoriaPaciente : {}", categoriaPacienteDTO);
        CategoriaPaciente categoriaPaciente = categoriaPacienteMapper.toEntity(categoriaPacienteDTO);
        categoriaPaciente = categoriaPacienteRepository.save(categoriaPaciente);
        return categoriaPacienteMapper.toDto(categoriaPaciente);
    }

    public Optional<CategoriaPacienteDTO> partialUpdate(CategoriaPacienteDTO categoriaPacienteDTO) {
        LOG.debug("Request to partially update CategoriaPaciente : {}", categoriaPacienteDTO);
        return categoriaPacienteRepository
            .findById(categoriaPacienteDTO.getId())
            .map(existing -> {
                categoriaPacienteMapper.partialUpdate(existing, categoriaPacienteDTO);
                return existing;
            })
            .map(categoriaPacienteRepository::save)
            .map(categoriaPacienteMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<CategoriaPacienteDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CategoriaPacientes");
        return categoriaPacienteRepository.findAll(pageable).map(categoriaPacienteMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<CategoriaPacienteDTO> findOne(Long id) {
        LOG.debug("Request to get CategoriaPaciente : {}", id);
        return categoriaPacienteRepository.findById(id).map(categoriaPacienteMapper::toDto);
    }

    public void delete(Long id) {
        LOG.debug("Request to delete CategoriaPaciente : {}", id);
        categoriaPacienteRepository.deleteById(id);
    }
}
