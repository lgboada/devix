package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "producto_imagen")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductoImagen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "path_imagen", nullable = false)
    private String pathImagen;

    @NotNull
    @Column(name = "no_cia", nullable = false)
    private Long noCia;

    @NotNull
    @Column(name = "orden", nullable = false)
    private Integer orden = 0;

    @NotNull
    @Column(name = "principal", nullable = false)
    private Boolean principal = false;

    @NotNull
    @Column(name = "visible", nullable = false)
    private Boolean visible = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonIgnoreProperties(value = { "detalleFacturas", "modelo", "tipoProducto", "proveedor", "productoImagens" }, allowSetters = true)
    private Producto producto;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPathImagen() {
        return this.pathImagen;
    }

    public void setPathImagen(String pathImagen) {
        this.pathImagen = pathImagen;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public Integer getOrden() {
        return this.orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Boolean getPrincipal() {
        return this.principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public Boolean getVisible() {
        return this.visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Producto getProducto() {
        return this.producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductoImagen)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductoImagen) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
