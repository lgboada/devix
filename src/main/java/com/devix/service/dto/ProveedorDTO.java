package com.devix.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devix.domain.Proveedor} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProveedorDTO implements Serializable {

    private Long id;

    @NotNull
    private Long noCia;

    @NotNull
    private String dni;

    @NotNull
    private String nombre;

    private String contacto;

    @NotNull
    @Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}")
    private String email;

    @NotNull
    private String pathImagen;

    @NotNull
    private String telefono;

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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPathImagen() {
        return pathImagen;
    }

    public void setPathImagen(String pathImagen) {
        this.pathImagen = pathImagen;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProveedorDTO)) {
            return false;
        }

        ProveedorDTO proveedorDTO = (ProveedorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, proveedorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProveedorDTO{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", dni='" + getDni() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", contacto='" + getContacto() + "'" +
            ", email='" + getEmail() + "'" +
            ", pathImagen='" + getPathImagen() + "'" +
            ", telefono='" + getTelefono() + "'" +
            "}";
    }
}
