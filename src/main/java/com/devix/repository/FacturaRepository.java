package com.devix.repository;

import com.devix.domain.Factura;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Factura entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long>, JpaSpecificationExecutor<Factura> {
    @Query(
        "SELECT DISTINCT f FROM Factura f " +
        "LEFT JOIN FETCH f.cliente " +
        "LEFT JOIN FETCH f.detalles d LEFT JOIN FETCH d.producto " +
        "WHERE f.id = :id"
    )
    Optional<Factura> findByIdWithDetalles(@Param("id") Long id);
}
