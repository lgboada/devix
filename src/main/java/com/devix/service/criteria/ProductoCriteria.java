package com.devix.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.devix.domain.Producto} entity. This class is used
 * in {@link com.devix.web.rest.ProductoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /productos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter noCia;

    private StringFilter nombre;

    private StringFilter descripcion;

    private BigDecimalFilter precio;

    private IntegerFilter stock;

    private StringFilter pathImagen;

    private StringFilter codigo;

    private LongFilter detalleFacturaId;

    private LongFilter modeloId;

    private LongFilter tipoProductoId;

    private LongFilter proveedorId;

    private Boolean distinct;

    public ProductoCriteria() {}

    public ProductoCriteria(ProductoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.noCia = other.optionalNoCia().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.descripcion = other.optionalDescripcion().map(StringFilter::copy).orElse(null);
        this.precio = other.optionalPrecio().map(BigDecimalFilter::copy).orElse(null);
        this.stock = other.optionalStock().map(IntegerFilter::copy).orElse(null);
        this.pathImagen = other.optionalPathImagen().map(StringFilter::copy).orElse(null);
        this.codigo = other.optionalCodigo().map(StringFilter::copy).orElse(null);
        this.detalleFacturaId = other.optionalDetalleFacturaId().map(LongFilter::copy).orElse(null);
        this.modeloId = other.optionalModeloId().map(LongFilter::copy).orElse(null);
        this.tipoProductoId = other.optionalTipoProductoId().map(LongFilter::copy).orElse(null);
        this.proveedorId = other.optionalProveedorId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductoCriteria copy() {
        return new ProductoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getNoCia() {
        return noCia;
    }

    public Optional<LongFilter> optionalNoCia() {
        return Optional.ofNullable(noCia);
    }

    public LongFilter noCia() {
        if (noCia == null) {
            setNoCia(new LongFilter());
        }
        return noCia;
    }

    public void setNoCia(LongFilter noCia) {
        this.noCia = noCia;
    }

    public StringFilter getNombre() {
        return nombre;
    }

    public Optional<StringFilter> optionalNombre() {
        return Optional.ofNullable(nombre);
    }

    public StringFilter nombre() {
        if (nombre == null) {
            setNombre(new StringFilter());
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public Optional<StringFilter> optionalDescripcion() {
        return Optional.ofNullable(descripcion);
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            setDescripcion(new StringFilter());
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimalFilter getPrecio() {
        return precio;
    }

    public Optional<BigDecimalFilter> optionalPrecio() {
        return Optional.ofNullable(precio);
    }

    public BigDecimalFilter precio() {
        if (precio == null) {
            setPrecio(new BigDecimalFilter());
        }
        return precio;
    }

    public void setPrecio(BigDecimalFilter precio) {
        this.precio = precio;
    }

    public IntegerFilter getStock() {
        return stock;
    }

    public Optional<IntegerFilter> optionalStock() {
        return Optional.ofNullable(stock);
    }

    public IntegerFilter stock() {
        if (stock == null) {
            setStock(new IntegerFilter());
        }
        return stock;
    }

    public void setStock(IntegerFilter stock) {
        this.stock = stock;
    }

    public StringFilter getPathImagen() {
        return pathImagen;
    }

    public Optional<StringFilter> optionalPathImagen() {
        return Optional.ofNullable(pathImagen);
    }

    public StringFilter pathImagen() {
        if (pathImagen == null) {
            setPathImagen(new StringFilter());
        }
        return pathImagen;
    }

    public void setPathImagen(StringFilter pathImagen) {
        this.pathImagen = pathImagen;
    }

    public StringFilter getCodigo() {
        return codigo;
    }

    public Optional<StringFilter> optionalCodigo() {
        return Optional.ofNullable(codigo);
    }

    public StringFilter codigo() {
        if (codigo == null) {
            setCodigo(new StringFilter());
        }
        return codigo;
    }

    public void setCodigo(StringFilter codigo) {
        this.codigo = codigo;
    }

    public LongFilter getDetalleFacturaId() {
        return detalleFacturaId;
    }

    public Optional<LongFilter> optionalDetalleFacturaId() {
        return Optional.ofNullable(detalleFacturaId);
    }

    public LongFilter detalleFacturaId() {
        if (detalleFacturaId == null) {
            setDetalleFacturaId(new LongFilter());
        }
        return detalleFacturaId;
    }

    public void setDetalleFacturaId(LongFilter detalleFacturaId) {
        this.detalleFacturaId = detalleFacturaId;
    }

    public LongFilter getModeloId() {
        return modeloId;
    }

    public Optional<LongFilter> optionalModeloId() {
        return Optional.ofNullable(modeloId);
    }

    public LongFilter modeloId() {
        if (modeloId == null) {
            setModeloId(new LongFilter());
        }
        return modeloId;
    }

    public void setModeloId(LongFilter modeloId) {
        this.modeloId = modeloId;
    }

    public LongFilter getTipoProductoId() {
        return tipoProductoId;
    }

    public Optional<LongFilter> optionalTipoProductoId() {
        return Optional.ofNullable(tipoProductoId);
    }

    public LongFilter tipoProductoId() {
        if (tipoProductoId == null) {
            setTipoProductoId(new LongFilter());
        }
        return tipoProductoId;
    }

    public void setTipoProductoId(LongFilter tipoProductoId) {
        this.tipoProductoId = tipoProductoId;
    }

    public LongFilter getProveedorId() {
        return proveedorId;
    }

    public Optional<LongFilter> optionalProveedorId() {
        return Optional.ofNullable(proveedorId);
    }

    public LongFilter proveedorId() {
        if (proveedorId == null) {
            setProveedorId(new LongFilter());
        }
        return proveedorId;
    }

    public void setProveedorId(LongFilter proveedorId) {
        this.proveedorId = proveedorId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductoCriteria that = (ProductoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(noCia, that.noCia) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(precio, that.precio) &&
            Objects.equals(stock, that.stock) &&
            Objects.equals(pathImagen, that.pathImagen) &&
            Objects.equals(codigo, that.codigo) &&
            Objects.equals(detalleFacturaId, that.detalleFacturaId) &&
            Objects.equals(modeloId, that.modeloId) &&
            Objects.equals(tipoProductoId, that.tipoProductoId) &&
            Objects.equals(proveedorId, that.proveedorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            noCia,
            nombre,
            descripcion,
            precio,
            stock,
            pathImagen,
            codigo,
            detalleFacturaId,
            modeloId,
            tipoProductoId,
            proveedorId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNoCia().map(f -> "noCia=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalDescripcion().map(f -> "descripcion=" + f + ", ").orElse("") +
            optionalPrecio().map(f -> "precio=" + f + ", ").orElse("") +
            optionalStock().map(f -> "stock=" + f + ", ").orElse("") +
            optionalPathImagen().map(f -> "pathImagen=" + f + ", ").orElse("") +
            optionalCodigo().map(f -> "codigo=" + f + ", ").orElse("") +
            optionalDetalleFacturaId().map(f -> "detalleFacturaId=" + f + ", ").orElse("") +
            optionalModeloId().map(f -> "modeloId=" + f + ", ").orElse("") +
            optionalTipoProductoId().map(f -> "tipoProductoId=" + f + ", ").orElse("") +
            optionalProveedorId().map(f -> "proveedorId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
