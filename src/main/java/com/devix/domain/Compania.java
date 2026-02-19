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
    @Column(name = "no_cia", nullable = false)
    private Long noCia;

    @NotNull
    @Column(name = "dni", nullable = false, unique = true)
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

    @Column(name = "path_image", nullable = true)
    private String pathImage;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "compania")
    @JsonIgnoreProperties(value = { "facturas", "eventos", "compania" }, allowSetters = true)
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

    public Set<Centro> getCentros() {
        return this.centros;
    }

    public void setCentros(Set<Centro> centros) {
        if (this.centros != null) {
            this.centros.forEach(i -> i.setCompania(null));
        }
        if (centros != null) {
            centros.forEach(i -> i.setCompania(this));
        }
        this.centros = centros;
    }

    public Compania centros(Set<Centro> centros) {
        this.setCentros(centros);
        return this;
    }

    public Compania addCentros(Centro centro) {
        this.centros.add(centro);
        centro.setCompania(this);
        return this;
    }

    public Compania removeCentros(Centro centro) {
        this.centros.remove(centro);
        centro.setCompania(null);
        return this;
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
            "}";
    }
}
