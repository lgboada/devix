package com.devix.repository;

import com.devix.domain.ProductoImagen;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoImagenRepository extends JpaRepository<ProductoImagen, Long> {
    List<ProductoImagen> findByProductoIdOrderByOrdenAscIdAsc(Long productoId);

    Optional<ProductoImagen> findByIdAndProductoId(Long id, Long productoId);

    void deleteByIdAndProductoId(Long id, Long productoId);

    @Modifying
    @Query("update ProductoImagen p set p.principal = false where p.producto.id = :productoId and p.id <> :id")
    void clearPrincipalForProductoExcept(Long productoId, Long id);

    @Modifying
    @Query("update ProductoImagen p set p.principal = false where p.producto.id = :productoId")
    void clearPrincipalForProducto(Long productoId);
}
