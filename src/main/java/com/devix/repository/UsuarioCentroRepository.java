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

    @Query(
        "select usuarioCentro from UsuarioCentro usuarioCentro left join fetch usuarioCentro.centro where usuarioCentro.user.login = :login order by usuarioCentro.principal desc, usuarioCentro.noCia asc"
    )
    List<UsuarioCentro> findAllByUserLoginOrderByPrincipalDescNoCiaAsc(@Param("login") String login);

    /**
     * Una fila por compañía efectiva ({@code COALESCE(centro.noCia, usuario_centro.noCia)}): nombre de {@link com.devix.domain.Compania}, no del centro.
     */
    @Query(
        """
        SELECT COALESCE(c.noCia, uc.noCia) AS effectiveNoCia,
               MAX(CASE WHEN uc.principal = true THEN 1 ELSE 0 END) AS principalInt,
               MAX(comp.nombre) AS label
        FROM UsuarioCentro uc
        LEFT JOIN uc.centro c
        LEFT JOIN Compania comp ON comp.noCia = COALESCE(c.noCia, uc.noCia)
        WHERE uc.user.login = :login
        GROUP BY COALESCE(c.noCia, uc.noCia)
        ORDER BY MAX(CASE WHEN uc.principal = true THEN 1 ELSE 0 END) DESC, COALESCE(c.noCia, uc.noCia) ASC
        """
    )
    List<AccountCompanyProjection> findDistinctAccountCompaniesByUserLogin(@Param("login") String login);

    Optional<UsuarioCentro> findFirstByUser_LoginAndPrincipalTrue(String login);

    Optional<UsuarioCentro> findFirstByUser_LoginOrderByNoCiaAsc(String login);

    boolean existsByUser_LoginAndNoCia(String login, Long noCia);

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
