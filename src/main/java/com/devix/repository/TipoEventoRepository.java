package com.devix.repository;

import com.devix.domain.TipoEvento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TipoEvento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoEventoRepository extends JpaRepository<TipoEvento, Long>, JpaSpecificationExecutor<TipoEvento> {}
