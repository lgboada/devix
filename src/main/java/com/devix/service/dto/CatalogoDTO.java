package com.devix.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devix.domain.Catalogo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CatalogoDTO implements Serializable {

    private Long id;

    @NotNull
    private Long noCia;

    @NotNull
    private String descripcion1;

    private String descripcion2;

    private String estado;

    private Integer orden;

    private String texto1;

    private String texto2;

    private TipoCatalogoDTO tipoCatalogo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return noCia;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getDescripcion1() {
        return descripcion1;
    }

    public void setDescripcion1(String descripcion1) {
        this.descripcion1 = descripcion1;
    }

    public String getDescripcion2() {
        return descripcion2;
    }

    public void setDescripcion2(String descripcion2) {
        this.descripcion2 = descripcion2;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getTexto1() {
        return texto1;
    }

    public void setTexto1(String texto1) {
        this.texto1 = texto1;
    }

    public String getTexto2() {
        return texto2;
    }

    public void setTexto2(String texto2) {
        this.texto2 = texto2;
    }

    public TipoCatalogoDTO getTipoCatalogo() {
        return tipoCatalogo;
    }

    public void setTipoCatalogo(TipoCatalogoDTO tipoCatalogo) {
        this.tipoCatalogo = tipoCatalogo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatalogoDTO)) {
            return false;
        }

        CatalogoDTO catalogoDTO = (CatalogoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, catalogoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogoDTO{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", descripcion1='" + getDescripcion1() + "'" +
            ", descripcion2='" + getDescripcion2() + "'" +
            ", estado='" + getEstado() + "'" +
            ", orden=" + getOrden() +
            ", texto1='" + getTexto1() + "'" +
            ", texto2='" + getTexto2() + "'" +
            ", tipoCatalogo=" + getTipoCatalogo() +
            "}";
    }
}
