package com.devix.repository;

import com.devix.domain.TipoDocumento;
import com.devix.domain.TipoDocumentoId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, TipoDocumentoId> {
    @Query("SELECT t FROM TipoDocumento t WHERE t.id.noCia = :noCia ORDER BY t.indice")
    List<TipoDocumento> findByNoCiaOrderByIndice(@Param("noCia") Long noCia);

    @Query("SELECT t FROM TipoDocumento t WHERE t.id.noCia = :noCia AND t.id.tipoDocumento = :codigo")
    Optional<TipoDocumento> findByNoCiaAndTipoDocumento(@Param("noCia") Long noCia, @Param("codigo") String codigo);
}
