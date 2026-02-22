package com.devix.repository;

import com.devix.domain.UsuarioCentro;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UsuarioCentro entity.
 */
@Repository
public interface UsuarioCentroRepository extends JpaRepository<UsuarioCentro, Long>, JpaSpecificationExecutor<UsuarioCentro> {
    @Query("select usuarioCentro from UsuarioCentro usuarioCentro where usuarioCentro.user.login = ?#{authentication.name}")
    List<UsuarioCentro> findByUserIsCurrentUser();

    default Optional<UsuarioCentro> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UsuarioCentro> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UsuarioCentro> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select usuarioCentro from UsuarioCentro usuarioCentro left join fetch usuarioCentro.centro left join fetch usuarioCentro.user",
        countQuery = "select count(usuarioCentro) from UsuarioCentro usuarioCentro"
    )
    Page<UsuarioCentro> findAllWithToOneRelationships(Pageable pageable);

    @Query("select usuarioCentro from UsuarioCentro usuarioCentro left join fetch usuarioCentro.centro left join fetch usuarioCentro.user")
    List<UsuarioCentro> findAllWithToOneRelationships();

    @Query(
        "select usuarioCentro from UsuarioCentro usuarioCentro left join fetch usuarioCentro.centro left join fetch usuarioCentro.user where usuarioCentro.id =:id"
    )
    Optional<UsuarioCentro> findOneWithToOneRelationships(@Param("id") Long id);
}
