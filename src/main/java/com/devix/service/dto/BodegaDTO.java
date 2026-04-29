package com.devix.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devix.domain.Bodega} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BodegaDTO implements Serializable {

    private Long id;

    @NotNull
    private Long noCia;

    @NotNull
    @Size(max = 50)
    private String codigo;

    @NotNull
    @Size(max = 255)
    private String nombre;

    @NotNull
    private Boolean activa;

    @NotNull
    private CentroDTO centro;

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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public CentroDTO getCentro() {
        return centro;
    }

    public void setCentro(CentroDTO centro) {
        this.centro = centro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BodegaDTO)) {
            return false;
        }

        BodegaDTO bodegaDTO = (BodegaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bodegaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "BodegaDTO{" +
            "id=" +
            getId() +
            ", noCia=" +
            getNoCia() +
            ", codigo='" +
            getCodigo() +
            "'" +
            ", nombre='" +
            getNombre() +
            "'" +
            ", activa=" +
            getActiva() +
            "}"
        );
    }
}
