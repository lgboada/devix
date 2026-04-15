package com.devix.repository;

import com.devix.domain.FacturaLog;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FacturaLog entity.
 */
@Repository
public interface FacturaLogRepository extends JpaRepository<FacturaLog, Long> {
    List<FacturaLog> findByDocumentoIdAndTipoDocumentoOrderByFechaIntentoDesc(Long documentoId, String tipoDocumento);
}
