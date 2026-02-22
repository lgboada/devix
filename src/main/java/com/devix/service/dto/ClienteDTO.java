package com.devix.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.devix.domain.Cliente} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClienteDTO implements Serializable {

    private Long id;

    @NotNull
    private Long noCia;

    @NotNull
    private String dni;

    @NotNull
    private String nombres;

    @NotNull
    private String apellidos;

    private String nombreComercial;

    @NotNull
    @Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}")
    private String email;

    private String telefono1;

    private String telefono2;

    @NotNull
    private LocalDate fechaNacimiento;

    @NotNull
    private String sexo;

    @NotNull
    private String estadoCivil;

    @NotNull
    private String tipoSangre;

    @NotNull
    private String pathImagen;

    private TipoClienteDTO tipoCliente;

    private CiudadDTO ciudad;

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

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public String getPathImagen() {
        return pathImagen;
    }

    public void setPathImagen(String pathImagen) {
        this.pathImagen = pathImagen;
    }

    public TipoClienteDTO getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoClienteDTO tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public CiudadDTO getCiudad() {
        return ciudad;
    }

    public void setCiudad(CiudadDTO ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClienteDTO)) {
            return false;
        }

        ClienteDTO clienteDTO = (ClienteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clienteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteDTO{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", dni='" + getDni() + "'" +
            ", nombres='" + getNombres() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", nombreComercial='" + getNombreComercial() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefono1='" + getTelefono1() + "'" +
            ", telefono2='" + getTelefono2() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", sexo='" + getSexo() + "'" +
            ", estadoCivil='" + getEstadoCivil() + "'" +
            ", tipoSangre='" + getTipoSangre() + "'" +
            ", pathImagen='" + getPathImagen() + "'" +
            ", tipoCliente=" + getTipoCliente() +
            ", ciudad=" + getCiudad() +
            "}";
    }
}
