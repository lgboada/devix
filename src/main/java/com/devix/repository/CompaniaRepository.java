package com.devix.repository;

import com.devix.domain.Compania;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Compania entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompaniaRepository extends JpaRepository<Compania, Long>, JpaSpecificationExecutor<Compania> {
    boolean existsByDni(String dni);

    boolean existsByDniAndIdNot(String dni, Long id);

    boolean existsByDniAndNoCia(String dni, Long noCia);

    boolean existsByDniAndNoCiaAndIdNot(String dni, Long noCia, Long id);

    boolean existsByNoCia(Long noCia);

    boolean existsByNoCiaAndIdNot(Long noCia, Long id);

    java.util.Optional<Compania> findByNoCia(Long noCia);

    List<Compania> findAllByNoCiaIn(Collection<Long> noCias);
}
