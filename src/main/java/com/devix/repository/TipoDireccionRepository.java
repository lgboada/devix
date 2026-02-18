package com.devix.repository;

import com.devix.domain.TipoDireccion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TipoDireccion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoDireccionRepository extends JpaRepository<TipoDireccion, Long>, JpaSpecificationExecutor<TipoDireccion> {}
