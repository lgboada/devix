package com.devix.service;

import com.devix.domain.FacturaLog;
import com.devix.repository.FacturaLogRepository;
import com.devix.service.dto.FacturaLogDTO;
import com.devix.service.mapper.FacturaLogMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FacturaLogService {

    private static final Logger LOG = LoggerFactory.getLogger(FacturaLogService.class);

    private final FacturaLogRepository facturaLogRepository;
    private final FacturaLogMapper facturaLogMapper;

    public FacturaLogService(FacturaLogRepository facturaLogRepository, FacturaLogMapper facturaLogMapper) {
        this.facturaLogRepository = facturaLogRepository;
        this.facturaLogMapper = facturaLogMapper;
    }

    public FacturaLogDTO save(FacturaLog facturaLog) {
        LOG.debug(
            "Saving FacturaLog: tipoDocumento={}, documentoId={}, tipoAccion={}, estado={}",
            facturaLog.getTipoDocumento(),
            facturaLog.getDocumentoId(),
            facturaLog.getTipoAccion(),
            facturaLog.getEstado()
        );
        FacturaLog saved = facturaLogRepository.save(facturaLog);
        return facturaLogMapper.toDto(saved);
    }

    /**
     * Persiste un log en una transacción nueva (p. ej. fallo de firma/envío dentro de {@code enviar} que aborta la transacción principal).
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public FacturaLogDTO saveInNewTransaction(FacturaLog facturaLog) {
        return save(facturaLog);
    }

    @Transactional(readOnly = true)
    public List<FacturaLogDTO> findByDocumentoId(Long documentoId, String tipoDocumento) {
        LOG.debug("Finding FacturaLogs: documentoId={}, tipoDocumento={}", documentoId, tipoDocumento);
        return facturaLogRepository
            .findByDocumentoIdAndTipoDocumentoOrderByFechaIntentoDesc(documentoId, tipoDocumento)
            .stream()
            .map(facturaLogMapper::toDto)
            .toList();
    }
}
