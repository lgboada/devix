package com.devix.repository;

import com.devix.domain.TipoCatalogo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TipoCatalogo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoCatalogoRepository extends JpaRepository<TipoCatalogo, Long>, JpaSpecificationExecutor<TipoCatalogo> {}
