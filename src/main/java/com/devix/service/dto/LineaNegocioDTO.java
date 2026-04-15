package com.devix.service.dto;

import java.io.Serializable;

public class LineaNegocioDTO implements Serializable {

    /** PK compuesta: (noCia, lineaNo) — sin surrogate id */
    private Long noCia;
    private String lineaNo;
    private String descripcion;

    public Long getNoCia() {
        return noCia;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getLineaNo() {
        return lineaNo;
    }

    public void setLineaNo(String lineaNo) {
        this.lineaNo = lineaNo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
