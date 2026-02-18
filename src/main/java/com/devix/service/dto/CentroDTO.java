package com.devix.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devix.domain.Centro} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CentroDTO implements Serializable {

    private Long id;

    @NotNull
    private Long noCia;

    @NotNull
    private String descripcion;

    private CompaniaDTO compania;

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public CompaniaDTO getCompania() {
        return compania;
    }

    public void setCompania(CompaniaDTO compania) {
        this.compania = compania;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CentroDTO)) {
            return false;
        }

        CentroDTO centroDTO = (CentroDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, centroDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CentroDTO{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", descripcion='" + getDescripcion() + "'" +
            ", compania=" + getCompania() +
            "}";
    }
}
