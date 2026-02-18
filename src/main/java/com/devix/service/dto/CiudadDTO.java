package com.devix.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devix.domain.Ciudad} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CiudadDTO implements Serializable {

    private Long id;

    @NotNull
    private Long noCia;

    @NotNull
    private String descripcion;

    private ProvinciaDTO provincia;

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

    public ProvinciaDTO getProvincia() {
        return provincia;
    }

    public void setProvincia(ProvinciaDTO provincia) {
        this.provincia = provincia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CiudadDTO)) {
            return false;
        }

        CiudadDTO ciudadDTO = (CiudadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ciudadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CiudadDTO{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", descripcion='" + getDescripcion() + "'" +
            ", provincia=" + getProvincia() +
            "}";
    }
}
