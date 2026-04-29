package com.devix.repository;

/**
 * Resultado de la consulta de bodegas accesibles por usuario para el menú de bodega activa.
 */
public interface AccountBodegaProjection {
    Long getBodegaId();

    Integer getPrincipalInt();

    String getCodigo();

    String getLabel();
}
