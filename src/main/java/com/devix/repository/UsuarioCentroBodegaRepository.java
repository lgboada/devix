package com.devix.repository;

import com.devix.domain.UsuarioCentroBodega;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UsuarioCentroBodega entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UsuarioCentroBodegaRepository
    extends JpaRepository<UsuarioCentroBodega, Long>, JpaSpecificationExecutor<UsuarioCentroBodega> {
    /**
     * Bodegas accesibles por el usuario en un centro: una fila por {@code bodega.id}.
     */
    @Query(
        """
        SELECT ucb.bodega.id AS bodegaId,
               MAX(CASE WHEN ucb.principal = true THEN 1 ELSE 0 END) AS principalInt,
               MAX(ucb.bodega.codigo) AS codigo,
               MAX(ucb.bodega.nombre) AS label
        FROM UsuarioCentroBodega ucb
        WHERE ucb.user.login = :login
          AND ucb.centro.id = :centroId
        GROUP BY ucb.bodega.id
        ORDER BY MAX(CASE WHEN ucb.principal = true THEN 1 ELSE 0 END) DESC, MAX(ucb.bodega.codigo) ASC
        """
    )
    List<AccountBodegaProjection> findDistinctAccountBodegasByUserLoginAndCentroId(
        @Param("login") String login,
        @Param("centroId") Long centroId
    );
}
