package com.devix.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devix.domain.Compania} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompaniaDTO implements Serializable {

    private Long id;

    @NotNull
    private Long noCia;

    @NotNull
    private String dni;

    @NotNull
    private String nombre;

    @NotNull
    private String direccion;

    @NotNull
    @Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}")
    private String email;

    @NotNull
    private String telefono;

    @NotNull
    private String pathImage;

    @NotNull
    private Boolean activa;

    private String establecimiento;

    private String contribuyenteEspecial;

    private Boolean obligadoContabilidad;

    private Integer ambienteSri;

    private String pathCertificado;

    private String claveCertificado;

    private String pathFileServer;

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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public String getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    public String getContribuyenteEspecial() {
        return contribuyenteEspecial;
    }

    public void setContribuyenteEspecial(String contribuyenteEspecial) {
        this.contribuyenteEspecial = contribuyenteEspecial;
    }

    public Boolean getObligadoContabilidad() {
        return obligadoContabilidad;
    }

    public void setObligadoContabilidad(Boolean obligadoContabilidad) {
        this.obligadoContabilidad = obligadoContabilidad;
    }

    public Integer getAmbienteSri() {
        return ambienteSri;
    }

    public void setAmbienteSri(Integer ambienteSri) {
        this.ambienteSri = ambienteSri;
    }

    public String getPathCertificado() {
        return pathCertificado;
    }

    public void setPathCertificado(String pathCertificado) {
        this.pathCertificado = pathCertificado;
    }

    public String getClaveCertificado() {
        return claveCertificado;
    }

    public void setClaveCertificado(String claveCertificado) {
        this.claveCertificado = claveCertificado;
    }

    public String getPathFileServer() {
        return pathFileServer;
    }

    public void setPathFileServer(String pathFileServer) {
        this.pathFileServer = pathFileServer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompaniaDTO)) {
            return false;
        }

        CompaniaDTO companiaDTO = (CompaniaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, companiaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompaniaDTO{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", dni='" + getDni() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", pathImage='" + getPathImage() + "'" +
            ", activa='" + getActiva() + "'" +
            "}";
    }
}
