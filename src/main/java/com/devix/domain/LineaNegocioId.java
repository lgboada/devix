package com.devix.domain;

import java.io.Serializable;
import java.util.Objects;

public class LineaNegocioId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long noCia;
    private String lineaNo;

    public LineaNegocioId() {}

    public LineaNegocioId(Long noCia, String lineaNo) {
        this.noCia = noCia;
        this.lineaNo = lineaNo;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LineaNegocioId that)) return false;
        return Objects.equals(noCia, that.noCia) && Objects.equals(lineaNo, that.lineaNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noCia, lineaNo);
    }
}
