package com.devix.repository;

import com.devix.domain.TipoCliente;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TipoCliente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoClienteRepository extends JpaRepository<TipoCliente, Long>, JpaSpecificationExecutor<TipoCliente> {}
