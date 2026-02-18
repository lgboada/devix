package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Modelo.
 */
@Entity
@Table(name = "modelo")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Modelo implements Serializable {

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "modelo")
    @JsonIgnoreProperties(value = { "detalleFacturas", "modelo", "tipoProducto", "proveedor" }, allowSetters = true)
    private Set<Producto> productos = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "modelos" }, allowSetters = true)
    private Marca marca;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Modelo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public Modelo noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Modelo nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPathImagen() {
        return this.pathImagen;
    }

    public Modelo pathImagen(String pathImagen) {
        this.setPathImagen(pathImagen);
        return this;
    }

    public void setPathImagen(String pathImagen) {
        this.pathImagen = pathImagen;
    }

    public Set<Producto> getProductos() {
        return this.productos;
    }

    public void setProductos(Set<Producto> productos) {
        if (this.productos != null) {
            this.productos.forEach(i -> i.setModelo(null));
        }
        if (productos != null) {
            productos.forEach(i -> i.setModelo(this));
        }
        this.productos = productos;
    }

    public Modelo productos(Set<Producto> productos) {
        this.setProductos(productos);
        return this;
    }

    public Modelo addProducto(Producto producto) {
        this.productos.add(producto);
        producto.setModelo(this);
        return this;
    }

    public Modelo removeProducto(Producto producto) {
        this.productos.remove(producto);
        producto.setModelo(null);
        return this;
    }

    public Marca getMarca() {
        return this.marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Modelo marca(Marca marca) {
        this.setMarca(marca);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Modelo)) {
            return false;
        }
        return getId() != null && getId().equals(((Modelo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Modelo{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", nombre='" + getNombre() + "'" +
            ", pathImagen='" + getPathImagen() + "'" +
            "}";
    }
}
