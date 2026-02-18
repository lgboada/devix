package com.devix.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devix.domain.Modelo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ModeloDTO implements Serializable {

    private Long id;

    @NotNull
    private Long noCia;

    @NotNull
    private String nombre;

    @NotNull
    private String pathImagen;

    private MarcaDTO marca;

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

    public String getPathImagen() {
        return pathImagen;
    }

    public void setPathImagen(String pathImagen) {
        this.pathImagen = pathImagen;
    }

    public MarcaDTO getMarca() {
        return marca;
    }

    public void setMarca(MarcaDTO marca) {
        this.marca = marca;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModeloDTO)) {
            return false;
        }

        ModeloDTO modeloDTO = (ModeloDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, modeloDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModeloDTO{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", nombre='" + getNombre() + "'" +
            ", pathImagen='" + getPathImagen() + "'" +
            ", marca=" + getMarca() +
            "}";
    }
}
