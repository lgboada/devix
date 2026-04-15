package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Compania.
 */
@Entity
@Table(name = "compania")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Compania implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "no_cia", nullable = false, unique = true)
    private Long noCia;

    @NotNull
    @Column(name = "dni", nullable = false)
    private String dni;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "direccion", nullable = false)
    private String direccion;

    @NotNull
    @Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "telefono", nullable = false)
    private String telefono;

    @NotNull
    @Column(name = "path_image", nullable = false)
    private String pathImage;

    @NotNull
    @Column(name = "activa", nullable = false)
    private Boolean activa;

    @Column(name = "establecimiento", length = 3)
    private String establecimiento;

    @Column(name = "contribuyente_especial", length = 10)
    private String contribuyenteEspecial;

    @Column(name = "obligado_contabilidad")
    private Boolean obligadoContabilidad;

    @Column(name = "ambiente_sri")
    private Integer ambienteSri;

    @Column(name = "path_certificado", length = 500)
    private String pathCertificado;

    @Column(name = "clave_certificado", length = 200)
    private String claveCertificado;

    @Column(name = "path_file_server", length = 500)
    private String pathFileServer;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "no_cia", referencedColumnName = "no_cia", insertable = false, updatable = false)
    @JsonIgnoreProperties(value = { "facturas", "eventos" }, allowSetters = true)
    private Set<Centro> centros = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Compania id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public Compania noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getDni() {
        return this.dni;
    }

    public Compania dni(String dni) {
        this.setDni(dni);
        return this;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Compania nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Compania direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return this.email;
    }

    public Compania email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Compania telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPathImage() {
        return this.pathImage;
    }

    public Compania pathImage(String pathImage) {
        this.setPathImage(pathImage);
        return this;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public Boolean getActiva() {
        return this.activa;
    }

    public Compania activa(Boolean activa) {
        this.setActiva(activa);
        return this;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public Set<Centro> getCentros() {
        return this.centros;
    }

    public void setCentros(Set<Centro> centros) {
        this.centros = centros;
    }

    public Compania centros(Set<Centro> centros) {
        this.setCentros(centros);
        return this;
    }

    public Compania addCentros(Centro centro) {
        this.centros.add(centro);
        return this;
    }

    public Compania removeCentros(Centro centro) {
        this.centros.remove(centro);
        return this;
    }

    public String getEstablecimiento() {
        return this.establecimiento;
    }

    public Compania establecimiento(String establecimiento) {
        this.setEstablecimiento(establecimiento);
        return this;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    public String getContribuyenteEspecial() {
        return this.contribuyenteEspecial;
    }

    public Compania contribuyenteEspecial(String contribuyenteEspecial) {
        this.setContribuyenteEspecial(contribuyenteEspecial);
        return this;
    }

    public void setContribuyenteEspecial(String contribuyenteEspecial) {
        this.contribuyenteEspecial = contribuyenteEspecial;
    }

    public Boolean getObligadoContabilidad() {
        return this.obligadoContabilidad;
    }

    public Compania obligadoContabilidad(Boolean obligadoContabilidad) {
        this.setObligadoContabilidad(obligadoContabilidad);
        return this;
    }

    public void setObligadoContabilidad(Boolean obligadoContabilidad) {
        this.obligadoContabilidad = obligadoContabilidad;
    }

    public Integer getAmbienteSri() {
        return this.ambienteSri;
    }

    public Compania ambienteSri(Integer ambienteSri) {
        this.setAmbienteSri(ambienteSri);
        return this;
    }

    public void setAmbienteSri(Integer ambienteSri) {
        this.ambienteSri = ambienteSri;
    }

    public String getPathCertificado() {
        return this.pathCertificado;
    }

    public Compania pathCertificado(String pathCertificado) {
        this.setPathCertificado(pathCertificado);
        return this;
    }

    public void setPathCertificado(String pathCertificado) {
        this.pathCertificado = pathCertificado;
    }

    public String getClaveCertificado() {
        return this.claveCertificado;
    }

    public Compania claveCertificado(String claveCertificado) {
        this.setClaveCertificado(claveCertificado);
        return this;
    }

    public void setClaveCertificado(String claveCertificado) {
        this.claveCertificado = claveCertificado;
    }

    public String getPathFileServer() {
        return this.pathFileServer;
    }

    public Compania pathFileServer(String pathFileServer) {
        this.setPathFileServer(pathFileServer);
        return this;
    }

    public void setPathFileServer(String pathFileServer) {
        this.pathFileServer = pathFileServer;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Compania)) {
            return false;
        }
        return getId() != null && getId().equals(((Compania) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Compania{" +
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
