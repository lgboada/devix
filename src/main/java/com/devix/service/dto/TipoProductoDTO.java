package com.devix.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devix.domain.TipoProducto} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TipoProductoDTO implements Serializable {

    private Long id;

    @NotNull
    private Long noCia;

    @NotNull
    private String nombre;

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoProductoDTO)) {
            return false;
        }

        TipoProductoDTO tipoProductoDTO = (TipoProductoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tipoProductoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoProductoDTO{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
