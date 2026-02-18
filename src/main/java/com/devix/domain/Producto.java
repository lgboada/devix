package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A Producto.
 */
@Entity
@Table(name = "producto")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Producto implements Serializable {

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
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "precio", precision = 21, scale = 2, nullable = false)
    private BigDecimal precio;

    @NotNull
    @Min(value = 0)
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @NotNull
    @Column(name = "path_imagen", nullable = false)
    private String pathImagen;

    @NotNull
    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "producto")
    @JsonIgnoreProperties(value = { "factura", "producto" }, allowSetters = true)
    private Set<DetalleFactura> detalleFacturas = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "productos", "marca" }, allowSetters = true)
    private Modelo modelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "productos" }, allowSetters = true)
    private TipoProducto tipoProducto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "productos" }, allowSetters = true)
    private Proveedor proveedor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Producto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public Producto noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Producto nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Producto descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return this.precio;
    }

    public Producto precio(BigDecimal precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return this.stock;
    }

    public Producto stock(Integer stock) {
        this.setStock(stock);
        return this;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getPathImagen() {
        return this.pathImagen;
    }

    public Producto pathImagen(String pathImagen) {
        this.setPathImagen(pathImagen);
        return this;
    }

    public void setPathImagen(String pathImagen) {
        this.pathImagen = pathImagen;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Producto codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Set<DetalleFactura> getDetalleFacturas() {
        return this.detalleFacturas;
    }

    public void setDetalleFacturas(Set<DetalleFactura> detalleFacturas) {
        if (this.detalleFacturas != null) {
            this.detalleFacturas.forEach(i -> i.setProducto(null));
        }
        if (detalleFacturas != null) {
            detalleFacturas.forEach(i -> i.setProducto(this));
        }
        this.detalleFacturas = detalleFacturas;
    }

    public Producto detalleFacturas(Set<DetalleFactura> detalleFacturas) {
        this.setDetalleFacturas(detalleFacturas);
        return this;
    }

    public Producto addDetalleFactura(DetalleFactura detalleFactura) {
        this.detalleFacturas.add(detalleFactura);
        detalleFactura.setProducto(this);
        return this;
    }

    public Producto removeDetalleFactura(DetalleFactura detalleFactura) {
        this.detalleFacturas.remove(detalleFactura);
        detalleFactura.setProducto(null);
        return this;
    }

    public Modelo getModelo() {
        return this.modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public Producto modelo(Modelo modelo) {
        this.setModelo(modelo);
        return this;
    }

    public TipoProducto getTipoProducto() {
        return this.tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public Producto tipoProducto(TipoProducto tipoProducto) {
        this.setTipoProducto(tipoProducto);
        return this;
    }

    public Proveedor getProveedor() {
        return this.proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Producto proveedor(Proveedor proveedor) {
        this.setProveedor(proveedor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Producto)) {
            return false;
        }
        return getId() != null && getId().equals(((Producto) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Producto{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", precio=" + getPrecio() +
            ", stock=" + getStock() +
            ", pathImagen='" + getPathImagen() + "'" +
            ", codigo='" + getCodigo() + "'" +
            "}";
    }
}
