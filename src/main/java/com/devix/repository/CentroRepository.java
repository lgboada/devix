package com.devix.repository;

import com.devix.domain.Centro;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Centro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CentroRepository extends JpaRepository<Centro, Long>, JpaSpecificationExecutor<Centro> {}
