package com.devix.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devix.domain.Direccion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DireccionDTO implements Serializable {

    private Long id;

    @NotNull
    private Long noCia;

    @NotNull
    private String descripcion;

    private String telefono;

    private Double latitud;

    private Double longitud;

    private TipoDireccionDTO tipoDireccion;

    private ClienteDTO cliente;

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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public TipoDireccionDTO getTipoDireccion() {
        return tipoDireccion;
    }

    public void setTipoDireccion(TipoDireccionDTO tipoDireccion) {
        this.tipoDireccion = tipoDireccion;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DireccionDTO)) {
            return false;
        }

        DireccionDTO direccionDTO = (DireccionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, direccionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DireccionDTO{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", descripcion='" + getDescripcion() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", latitud=" + getLatitud() +
            ", longitud=" + getLongitud() +
            ", tipoDireccion=" + getTipoDireccion() +
            ", cliente=" + getCliente() +
            "}";
    }
}
