package com.devix.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.devix.domain.DetalleFactura} entity. This class is used
 * in {@link com.devix.web.rest.DetalleFacturaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /detalle-facturas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DetalleFacturaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter noCia;

    private IntegerFilter cantidad;

    private DoubleFilter precioUnitario;

    private DoubleFilter subtotal;

    private DoubleFilter descuento;

    private DoubleFilter impuesto;

    private DoubleFilter total;

    private LongFilter facturaId;

    private LongFilter productoId;

    private Boolean distinct;

    public DetalleFacturaCriteria() {}

    public DetalleFacturaCriteria(DetalleFacturaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.noCia = other.optionalNoCia().map(LongFilter::copy).orElse(null);
        this.cantidad = other.optionalCantidad().map(IntegerFilter::copy).orElse(null);
        this.precioUnitario = other.optionalPrecioUnitario().map(DoubleFilter::copy).orElse(null);
        this.subtotal = other.optionalSubtotal().map(DoubleFilter::copy).orElse(null);
        this.descuento = other.optionalDescuento().map(DoubleFilter::copy).orElse(null);
        this.impuesto = other.optionalImpuesto().map(DoubleFilter::copy).orElse(null);
        this.total = other.optionalTotal().map(DoubleFilter::copy).orElse(null);
        this.facturaId = other.optionalFacturaId().map(LongFilter::copy).orElse(null);
        this.productoId = other.optionalProductoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DetalleFacturaCriteria copy() {
        return new DetalleFacturaCriteria(this);
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

    public IntegerFilter getCantidad() {
        return cantidad;
    }

    public Optional<IntegerFilter> optionalCantidad() {
        return Optional.ofNullable(cantidad);
    }

    public IntegerFilter cantidad() {
        if (cantidad == null) {
            setCantidad(new IntegerFilter());
        }
        return cantidad;
    }

    public void setCantidad(IntegerFilter cantidad) {
        this.cantidad = cantidad;
    }

    public DoubleFilter getPrecioUnitario() {
        return precioUnitario;
    }

    public Optional<DoubleFilter> optionalPrecioUnitario() {
        return Optional.ofNullable(precioUnitario);
    }

    public DoubleFilter precioUnitario() {
        if (precioUnitario == null) {
            setPrecioUnitario(new DoubleFilter());
        }
        return precioUnitario;
    }

    public void setPrecioUnitario(DoubleFilter precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public DoubleFilter getSubtotal() {
        return subtotal;
    }

    public Optional<DoubleFilter> optionalSubtotal() {
        return Optional.ofNullable(subtotal);
    }

    public DoubleFilter subtotal() {
        if (subtotal == null) {
            setSubtotal(new DoubleFilter());
        }
        return subtotal;
    }

    public void setSubtotal(DoubleFilter subtotal) {
        this.subtotal = subtotal;
    }

    public DoubleFilter getDescuento() {
        return descuento;
    }

    public Optional<DoubleFilter> optionalDescuento() {
        return Optional.ofNullable(descuento);
    }

    public DoubleFilter descuento() {
        if (descuento == null) {
            setDescuento(new DoubleFilter());
        }
        return descuento;
    }

    public void setDescuento(DoubleFilter descuento) {
        this.descuento = descuento;
    }

    public DoubleFilter getImpuesto() {
        return impuesto;
    }

    public Optional<DoubleFilter> optionalImpuesto() {
        return Optional.ofNullable(impuesto);
    }

    public DoubleFilter impuesto() {
        if (impuesto == null) {
            setImpuesto(new DoubleFilter());
        }
        return impuesto;
    }

    public void setImpuesto(DoubleFilter impuesto) {
        this.impuesto = impuesto;
    }

    public DoubleFilter getTotal() {
        return total;
    }

    public Optional<DoubleFilter> optionalTotal() {
        return Optional.ofNullable(total);
    }

    public DoubleFilter total() {
        if (total == null) {
            setTotal(new DoubleFilter());
        }
        return total;
    }

    public void setTotal(DoubleFilter total) {
        this.total = total;
    }

    public LongFilter getFacturaId() {
        return facturaId;
    }

    public Optional<LongFilter> optionalFacturaId() {
        return Optional.ofNullable(facturaId);
    }

    public LongFilter facturaId() {
        if (facturaId == null) {
            setFacturaId(new LongFilter());
        }
        return facturaId;
    }

    public void setFacturaId(LongFilter facturaId) {
        this.facturaId = facturaId;
    }

    public LongFilter getProductoId() {
        return productoId;
    }

    public Optional<LongFilter> optionalProductoId() {
        return Optional.ofNullable(productoId);
    }

    public LongFilter productoId() {
        if (productoId == null) {
            setProductoId(new LongFilter());
        }
        return productoId;
    }

    public void setProductoId(LongFilter productoId) {
        this.productoId = productoId;
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
        final DetalleFacturaCriteria that = (DetalleFacturaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(noCia, that.noCia) &&
            Objects.equals(cantidad, that.cantidad) &&
            Objects.equals(precioUnitario, that.precioUnitario) &&
            Objects.equals(subtotal, that.subtotal) &&
            Objects.equals(descuento, that.descuento) &&
            Objects.equals(impuesto, that.impuesto) &&
            Objects.equals(total, that.total) &&
            Objects.equals(facturaId, that.facturaId) &&
            Objects.equals(productoId, that.productoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, noCia, cantidad, precioUnitario, subtotal, descuento, impuesto, total, facturaId, productoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetalleFacturaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNoCia().map(f -> "noCia=" + f + ", ").orElse("") +
            optionalCantidad().map(f -> "cantidad=" + f + ", ").orElse("") +
            optionalPrecioUnitario().map(f -> "precioUnitario=" + f + ", ").orElse("") +
            optionalSubtotal().map(f -> "subtotal=" + f + ", ").orElse("") +
            optionalDescuento().map(f -> "descuento=" + f + ", ").orElse("") +
            optionalImpuesto().map(f -> "impuesto=" + f + ", ").orElse("") +
            optionalTotal().map(f -> "total=" + f + ", ").orElse("") +
            optionalFacturaId().map(f -> "facturaId=" + f + ", ").orElse("") +
            optionalProductoId().map(f -> "productoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
