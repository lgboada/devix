package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TipoProducto.
 */
@Entity
@Table(name = "tipo_producto")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TipoProducto implements Serializable {

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoProducto")
    @JsonIgnoreProperties(value = { "detalleFacturas", "modelo", "tipoProducto", "proveedor" }, allowSetters = true)
    private Set<Producto> productos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TipoProducto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public TipoProducto noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getNombre() {
        return this.nombre;
    }

    public TipoProducto nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Producto> getProductos() {
        return this.productos;
    }

    public void setProductos(Set<Producto> productos) {
        if (this.productos != null) {
            this.productos.forEach(i -> i.setTipoProducto(null));
        }
        if (productos != null) {
            productos.forEach(i -> i.setTipoProducto(this));
        }
        this.productos = productos;
    }

    public TipoProducto productos(Set<Producto> productos) {
        this.setProductos(productos);
        return this;
    }

    public TipoProducto addProducto(Producto producto) {
        this.productos.add(producto);
        producto.setTipoProducto(this);
        return this;
    }

    public TipoProducto removeProducto(Producto producto) {
        this.productos.remove(producto);
        producto.setTipoProducto(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoProducto)) {
            return false;
        }
        return getId() != null && getId().equals(((TipoProducto) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoProducto{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
