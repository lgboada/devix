package com.devix.repository;

import com.devix.domain.Catalogo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Catalogo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatalogoRepository extends JpaRepository<Catalogo, Long>, JpaSpecificationExecutor<Catalogo> {}
