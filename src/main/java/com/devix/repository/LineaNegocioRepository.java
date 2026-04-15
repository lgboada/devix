package com.devix.repository;

import com.devix.domain.LineaNegocio;
import com.devix.domain.LineaNegocioId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LineaNegocioRepository extends JpaRepository<LineaNegocio, LineaNegocioId> {
    @Query("SELECT l FROM LineaNegocio l WHERE l.id.noCia = :noCia ORDER BY l.id.lineaNo")
    List<LineaNegocio> findByNoCiaOrderByLineaNo(@Param("noCia") Long noCia);

    @Query("SELECT l FROM LineaNegocio l WHERE l.id.noCia = :noCia AND l.id.lineaNo = :lineaNo")
    Optional<LineaNegocio> findByNoCiaAndLineaNo(@Param("noCia") Long noCia, @Param("lineaNo") String lineaNo);
}
