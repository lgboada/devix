package com.devix.service.historiaclinica;

import com.devix.domain.historiaclinica.Paciente;
import com.devix.repository.historiaclinica.PacienteRepository;
import com.devix.service.historiaclinica.dto.PacienteDTO;
import com.devix.service.historiaclinica.mapper.PacienteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PacienteService {

    private static final Logger LOG = LoggerFactory.getLogger(PacienteService.class);

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;

    public PacienteService(PacienteRepository pacienteRepository, PacienteMapper pacienteMapper) {
        this.pacienteRepository = pacienteRepository;
        this.pacienteMapper = pacienteMapper;
    }

    public PacienteDTO save(PacienteDTO pacienteDTO) {
        LOG.debug("Request to save Paciente : {}", pacienteDTO);
        Paciente paciente = pacienteMapper.toEntity(pacienteDTO);
        paciente = pacienteRepository.save(paciente);
        return pacienteMapper.toDto(paciente);
    }

    public PacienteDTO update(PacienteDTO pacienteDTO) {
        LOG.debug("Request to update Paciente : {}", pacienteDTO);
        Paciente paciente = pacienteMapper.toEntity(pacienteDTO);
        paciente = pacienteRepository.save(paciente);
        return pacienteMapper.toDto(paciente);
    }

    public Optional<PacienteDTO> partialUpdate(PacienteDTO pacienteDTO) {
        LOG.debug("Request to partially update Paciente : {}", pacienteDTO);
        return pacienteRepository
            .findById(pacienteDTO.getId())
            .map(existing -> {
                pacienteMapper.partialUpdate(existing, pacienteDTO);
                return existing;
            })
            .map(pacienteRepository::save)
            .map(pacienteMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PacienteDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Pacientes");
        return pacienteRepository.findAll(pageable).map(pacienteMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<PacienteDTO> findOne(Long id) {
        LOG.debug("Request to get Paciente : {}", id);
        return pacienteRepository.findById(id).map(pacienteMapper::toDto);
    }

    public void delete(Long id) {
        LOG.debug("Request to delete Paciente : {}", id);
        pacienteRepository.deleteById(id);
    }
}
