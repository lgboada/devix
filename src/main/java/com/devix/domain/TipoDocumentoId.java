package com.devix.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TipoDocumentoId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "no_cia")
    private Long noCia;

    @Column(name = "tipo_documento", length = 3)
    private String tipoDocumento;

    public TipoDocumentoId() {}

    public TipoDocumentoId(Long noCia, String tipoDocumento) {
        this.noCia = noCia;
        this.tipoDocumento = tipoDocumento;
    }

    public Long getNoCia() {
        return noCia;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TipoDocumentoId that)) return false;
        return Objects.equals(noCia, that.noCia) && Objects.equals(tipoDocumento, that.tipoDocumento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noCia, tipoDocumento);
    }
}
