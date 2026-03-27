package com.devix.repository;

/**
 * Resultado de la agregación por compañía efectiva (usuario_centro + centro) para el menú de empresa.
 */
public interface AccountCompanyProjection {
    Long getEffectiveNoCia();

    Integer getPrincipalInt();

    String getLabel();
}
