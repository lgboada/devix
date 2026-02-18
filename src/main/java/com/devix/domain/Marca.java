package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Marca.
 */
@Entity
@Table(name = "marca")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Marca implements Serializable {

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
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @NotNull
    @Column(name = "path_imagen", nullable = false)
    private String pathImagen;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "marca")
    @JsonIgnoreProperties(value = { "productos", "marca" }, allowSetters = true)
    private Set<Modelo> modelos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Marca id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public Marca noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Marca nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPathImagen() {
        return this.pathImagen;
    }

    public Marca pathImagen(String pathImagen) {
        this.setPathImagen(pathImagen);
        return this;
    }

    public void setPathImagen(String pathImagen) {
        this.pathImagen = pathImagen;
    }

    public Set<Modelo> getModelos() {
        return this.modelos;
    }

    public void setModelos(Set<Modelo> modelos) {
        if (this.modelos != null) {
            this.modelos.forEach(i -> i.setMarca(null));
        }
        if (modelos != null) {
            modelos.forEach(i -> i.setMarca(this));
        }
        this.modelos = modelos;
    }

    public Marca modelos(Set<Modelo> modelos) {
        this.setModelos(modelos);
        return this;
    }

    public Marca addModelos(Modelo modelo) {
        this.modelos.add(modelo);
        modelo.setMarca(this);
        return this;
    }

    public Marca removeModelos(Modelo modelo) {
        this.modelos.remove(modelo);
        modelo.setMarca(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Marca)) {
            return false;
        }
        return getId() != null && getId().equals(((Marca) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Marca{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", nombre='" + getNombre() + "'" +
            ", pathImagen='" + getPathImagen() + "'" +
            "}";
    }
}
