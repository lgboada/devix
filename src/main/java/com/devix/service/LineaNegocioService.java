package com.devix.service;

import com.devix.domain.LineaNegocio;
import com.devix.domain.LineaNegocioId;
import com.devix.repository.LineaNegocioRepository;
import com.devix.service.dto.LineaNegocioDTO;
import com.devix.service.mapper.LineaNegocioMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LineaNegocioService {

    private final LineaNegocioRepository repository;
    private final LineaNegocioMapper mapper;

    public LineaNegocioService(LineaNegocioRepository repository, LineaNegocioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public LineaNegocioDTO save(LineaNegocioDTO dto) {
        LineaNegocio entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<LineaNegocioDTO> findByNoCia(Long noCia) {
        return mapper.toDto(repository.findByNoCiaOrderByLineaNo(noCia));
    }

    @Transactional(readOnly = true)
    public Optional<LineaNegocioDTO> findOne(Long noCia, String lineaNo) {
        return repository.findByNoCiaAndLineaNo(noCia, lineaNo).map(mapper::toDto);
    }

    public void delete(Long noCia, String lineaNo) {
        repository.deleteById(new LineaNegocioId(noCia, lineaNo));
    }
}
