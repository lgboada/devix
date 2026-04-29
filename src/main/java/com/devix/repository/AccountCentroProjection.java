package com.devix.repository;

/**
 * Resultado de la consulta de centros accesibles por usuario para el menú de centro activo.
 */
public interface AccountCentroProjection {
    Long getCentroId();

    Integer getPrincipalInt();

    String getLabel();
}
