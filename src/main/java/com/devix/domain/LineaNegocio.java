package com.devix.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Catálogo de líneas de negocio por compañía.
 * PK compuesta: (no_cia, linea_no).
 * Ejemplos: VEH=Vehículos, REP=Repuestos, TAL=Talleres.
 */
@Entity
@Table(name = "linea_negocio")
public class LineaNegocio implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private LineaNegocioId id = new LineaNegocioId();

    @Column(name = "descripcion", length = 60)
    private String descripcion;

    // --- PK access delegates ---

    public LineaNegocioId getId() {
        return id;
    }

    public void setId(LineaNegocioId id) {
        this.id = id;
    }

    public Long getNoCia() {
        return id.getNoCia();
    }

    public void setNoCia(Long noCia) {
        id.setNoCia(noCia);
    }

    public String getLineaNo() {
        return id.getLineaNo();
    }

    public void setLineaNo(String lineaNo) {
        id.setLineaNo(lineaNo);
    }

    // --- Other fields ---

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
