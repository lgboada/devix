package com.devix.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Catálogo de tipos de documento por compañía.
 * PK compuesta: (no_cia, tipo_documento).
 * codigo_sri: "01"=Factura, "04"=NC, "05"=ND, "06"=Guía, "07"=Retención, "03"=LiqComp
 */
@Entity
@Table(name = "tipo_documento")
public class TipoDocumento implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private TipoDocumentoId id = new TipoDocumentoId();

    @Column(name = "descripcion", length = 60)
    private String descripcion;

    @Column(name = "indice", length = 2)
    private String indice;

    /** Código SRI: "01"=Factura,"04"=NC,"05"=ND,"06"=Guía,"07"=Retención,"03"=LiqComp */
    @Column(name = "codigo_sri", length = 2)
    private String codigoSri;

    // --- PK access delegates ---

    public TipoDocumentoId getId() {
        return id;
    }

    public void setId(TipoDocumentoId id) {
        this.id = id;
    }

    public Long getNoCia() {
        return id.getNoCia();
    }

    public void setNoCia(Long noCia) {
        id.setNoCia(noCia);
    }

    public String getTipoDocumento() {
        return id.getTipoDocumento();
    }

    public void setTipoDocumento(String tipoDocumento) {
        id.setTipoDocumento(tipoDocumento);
    }

    // --- Other fields ---

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public String getCodigoSri() {
        return codigoSri;
    }

    public void setCodigoSri(String codigoSri) {
        this.codigoSri = codigoSri;
    }
}
