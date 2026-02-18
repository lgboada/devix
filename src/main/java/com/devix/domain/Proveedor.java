package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Proveedor.
 */
@Entity
@Table(name = "proveedor")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Proveedor implements Serializable {

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
    @Column(name = "dni", nullable = false)
    private String dni;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "contacto")
    private String contacto;

    @NotNull
    @Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "path_imagen", nullable = false)
    private String pathImagen;

    @NotNull
    @Column(name = "telefono", nullable = false)
    private String telefono;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "proveedor")
    @JsonIgnoreProperties(value = { "detalleFacturas", "modelo", "tipoProducto", "proveedor" }, allowSetters = true)
    private Set<Producto> productos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Proveedor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public Proveedor noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getDni() {
        return this.dni;
    }

    public Proveedor dni(String dni) {
        this.setDni(dni);
        return this;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Proveedor nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContacto() {
        return this.contacto;
    }

    public Proveedor contacto(String contacto) {
        this.setContacto(contacto);
        return this;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getEmail() {
        return this.email;
    }

    public Proveedor email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPathImagen() {
        return this.pathImagen;
    }

    public Proveedor pathImagen(String pathImagen) {
        this.setPathImagen(pathImagen);
        return this;
    }

    public void setPathImagen(String pathImagen) {
        this.pathImagen = pathImagen;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Proveedor telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Set<Producto> getProductos() {
        return this.productos;
    }

    public void setProductos(Set<Producto> productos) {
        if (this.productos != null) {
            this.productos.forEach(i -> i.setProveedor(null));
        }
        if (productos != null) {
            productos.forEach(i -> i.setProveedor(this));
        }
        this.productos = productos;
    }

    public Proveedor productos(Set<Producto> productos) {
        this.setProductos(productos);
        return this;
    }

    public Proveedor addProductos(Producto producto) {
        this.productos.add(producto);
        producto.setProveedor(this);
        return this;
    }

    public Proveedor removeProductos(Producto producto) {
        this.productos.remove(producto);
        producto.setProveedor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Proveedor)) {
            return false;
        }
        return getId() != null && getId().equals(((Proveedor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Proveedor{" +
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
