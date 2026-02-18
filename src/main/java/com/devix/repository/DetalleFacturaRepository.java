package com.devix.repository;

import com.devix.domain.DetalleFactura;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DetalleFactura entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetalleFacturaRepository extends JpaRepository<DetalleFactura, Long>, JpaSpecificationExecutor<DetalleFactura> {}
