package com.devix.service;

import com.devix.domain.TipoDocumento;
import com.devix.domain.TipoDocumentoId;
import com.devix.repository.TipoDocumentoRepository;
import com.devix.service.dto.TipoDocumentoDTO;
import com.devix.service.mapper.TipoDocumentoMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TipoDocumentoService {

    private final TipoDocumentoRepository repository;
    private final TipoDocumentoMapper mapper;

    public TipoDocumentoService(TipoDocumentoRepository repository, TipoDocumentoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public TipoDocumentoDTO save(TipoDocumentoDTO dto) {
        TipoDocumento entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<TipoDocumentoDTO> findByNoCia(Long noCia) {
        return mapper.toDto(repository.findByNoCiaOrderByIndice(noCia));
    }

    @Transactional(readOnly = true)
    public Optional<TipoDocumentoDTO> findOne(Long noCia, String tipoDocumento) {
        return repository.findByNoCiaAndTipoDocumento(noCia, tipoDocumento).map(mapper::toDto);
    }

    public void delete(Long noCia, String tipoDocumento) {
        repository.deleteById(new TipoDocumentoId(noCia, tipoDocumento));
    }
}
