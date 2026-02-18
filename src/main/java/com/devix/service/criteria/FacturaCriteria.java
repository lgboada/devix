package com.devix.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.devix.domain.Factura} entity. This class is used
 * in {@link com.devix.web.rest.FacturaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /facturas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FacturaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter noCia;

    private StringFilter serie;

    private StringFilter noFisico;

    private InstantFilter fecha;

    private DoubleFilter subtotal;

    private DoubleFilter impuesto;

    private DoubleFilter impuestoCero;

    private DoubleFilter descuento;

    private DoubleFilter total;

    private DoubleFilter porcentajeImpuesto;

    private StringFilter cedula;

    private StringFilter direccion;

    private StringFilter email;

    private StringFilter estado;

    private LongFilter detallesId;

    private LongFilter centroId;

    private LongFilter clienteId;

    private Boolean distinct;

    public FacturaCriteria() {}

    public FacturaCriteria(FacturaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.noCia = other.optionalNoCia().map(LongFilter::copy).orElse(null);
        this.serie = other.optionalSerie().map(StringFilter::copy).orElse(null);
        this.noFisico = other.optionalNoFisico().map(StringFilter::copy).orElse(null);
        this.fecha = other.optionalFecha().map(InstantFilter::copy).orElse(null);
        this.subtotal = other.optionalSubtotal().map(DoubleFilter::copy).orElse(null);
        this.impuesto = other.optionalImpuesto().map(DoubleFilter::copy).orElse(null);
        this.impuestoCero = other.optionalImpuestoCero().map(DoubleFilter::copy).orElse(null);
        this.descuento = other.optionalDescuento().map(DoubleFilter::copy).orElse(null);
        this.total = other.optionalTotal().map(DoubleFilter::copy).orElse(null);
        this.porcentajeImpuesto = other.optionalPorcentajeImpuesto().map(DoubleFilter::copy).orElse(null);
        this.cedula = other.optionalCedula().map(StringFilter::copy).orElse(null);
        this.direccion = other.optionalDireccion().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.estado = other.optionalEstado().map(StringFilter::copy).orElse(null);
        this.detallesId = other.optionalDetallesId().map(LongFilter::copy).orElse(null);
        this.centroId = other.optionalCentroId().map(LongFilter::copy).orElse(null);
        this.clienteId = other.optionalClienteId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FacturaCriteria copy() {
        return new FacturaCriteria(this);
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

    public StringFilter getSerie() {
        return serie;
    }

    public Optional<StringFilter> optionalSerie() {
        return Optional.ofNullable(serie);
    }

    public StringFilter serie() {
        if (serie == null) {
            setSerie(new StringFilter());
        }
        return serie;
    }

    public void setSerie(StringFilter serie) {
        this.serie = serie;
    }

    public StringFilter getNoFisico() {
        return noFisico;
    }

    public Optional<StringFilter> optionalNoFisico() {
        return Optional.ofNullable(noFisico);
    }

    public StringFilter noFisico() {
        if (noFisico == null) {
            setNoFisico(new StringFilter());
        }
        return noFisico;
    }

    public void setNoFisico(StringFilter noFisico) {
        this.noFisico = noFisico;
    }

    public InstantFilter getFecha() {
        return fecha;
    }

    public Optional<InstantFilter> optionalFecha() {
        return Optional.ofNullable(fecha);
    }

    public InstantFilter fecha() {
        if (fecha == null) {
            setFecha(new InstantFilter());
        }
        return fecha;
    }

    public void setFecha(InstantFilter fecha) {
        this.fecha = fecha;
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

    public DoubleFilter getImpuestoCero() {
        return impuestoCero;
    }

    public Optional<DoubleFilter> optionalImpuestoCero() {
        return Optional.ofNullable(impuestoCero);
    }

    public DoubleFilter impuestoCero() {
        if (impuestoCero == null) {
            setImpuestoCero(new DoubleFilter());
        }
        return impuestoCero;
    }

    public void setImpuestoCero(DoubleFilter impuestoCero) {
        this.impuestoCero = impuestoCero;
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

    public DoubleFilter getPorcentajeImpuesto() {
        return porcentajeImpuesto;
    }

    public Optional<DoubleFilter> optionalPorcentajeImpuesto() {
        return Optional.ofNullable(porcentajeImpuesto);
    }

    public DoubleFilter porcentajeImpuesto() {
        if (porcentajeImpuesto == null) {
            setPorcentajeImpuesto(new DoubleFilter());
        }
        return porcentajeImpuesto;
    }

    public void setPorcentajeImpuesto(DoubleFilter porcentajeImpuesto) {
        this.porcentajeImpuesto = porcentajeImpuesto;
    }

    public StringFilter getCedula() {
        return cedula;
    }

    public Optional<StringFilter> optionalCedula() {
        return Optional.ofNullable(cedula);
    }

    public StringFilter cedula() {
        if (cedula == null) {
            setCedula(new StringFilter());
        }
        return cedula;
    }

    public void setCedula(StringFilter cedula) {
        this.cedula = cedula;
    }

    public StringFilter getDireccion() {
        return direccion;
    }

    public Optional<StringFilter> optionalDireccion() {
        return Optional.ofNullable(direccion);
    }

    public StringFilter direccion() {
        if (direccion == null) {
            setDireccion(new StringFilter());
        }
        return direccion;
    }

    public void setDireccion(StringFilter direccion) {
        this.direccion = direccion;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getEstado() {
        return estado;
    }

    public Optional<StringFilter> optionalEstado() {
        return Optional.ofNullable(estado);
    }

    public StringFilter estado() {
        if (estado == null) {
            setEstado(new StringFilter());
        }
        return estado;
    }

    public void setEstado(StringFilter estado) {
        this.estado = estado;
    }

    public LongFilter getDetallesId() {
        return detallesId;
    }

    public Optional<LongFilter> optionalDetallesId() {
        return Optional.ofNullable(detallesId);
    }

    public LongFilter detallesId() {
        if (detallesId == null) {
            setDetallesId(new LongFilter());
        }
        return detallesId;
    }

    public void setDetallesId(LongFilter detallesId) {
        this.detallesId = detallesId;
    }

    public LongFilter getCentroId() {
        return centroId;
    }

    public Optional<LongFilter> optionalCentroId() {
        return Optional.ofNullable(centroId);
    }

    public LongFilter centroId() {
        if (centroId == null) {
            setCentroId(new LongFilter());
        }
        return centroId;
    }

    public void setCentroId(LongFilter centroId) {
        this.centroId = centroId;
    }

    public LongFilter getClienteId() {
        return clienteId;
    }

    public Optional<LongFilter> optionalClienteId() {
        return Optional.ofNullable(clienteId);
    }

    public LongFilter clienteId() {
        if (clienteId == null) {
            setClienteId(new LongFilter());
        }
        return clienteId;
    }

    public void setClienteId(LongFilter clienteId) {
        this.clienteId = clienteId;
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
        final FacturaCriteria that = (FacturaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(noCia, that.noCia) &&
            Objects.equals(serie, that.serie) &&
            Objects.equals(noFisico, that.noFisico) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(subtotal, that.subtotal) &&
            Objects.equals(impuesto, that.impuesto) &&
            Objects.equals(impuestoCero, that.impuestoCero) &&
            Objects.equals(descuento, that.descuento) &&
            Objects.equals(total, that.total) &&
            Objects.equals(porcentajeImpuesto, that.porcentajeImpuesto) &&
            Objects.equals(cedula, that.cedula) &&
            Objects.equals(direccion, that.direccion) &&
            Objects.equals(email, that.email) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(detallesId, that.detallesId) &&
            Objects.equals(centroId, that.centroId) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            noCia,
            serie,
            noFisico,
            fecha,
            subtotal,
            impuesto,
            impuestoCero,
            descuento,
            total,
            porcentajeImpuesto,
            cedula,
            direccion,
            email,
            estado,
            detallesId,
            centroId,
            clienteId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNoCia().map(f -> "noCia=" + f + ", ").orElse("") +
            optionalSerie().map(f -> "serie=" + f + ", ").orElse("") +
            optionalNoFisico().map(f -> "noFisico=" + f + ", ").orElse("") +
            optionalFecha().map(f -> "fecha=" + f + ", ").orElse("") +
            optionalSubtotal().map(f -> "subtotal=" + f + ", ").orElse("") +
            optionalImpuesto().map(f -> "impuesto=" + f + ", ").orElse("") +
            optionalImpuestoCero().map(f -> "impuestoCero=" + f + ", ").orElse("") +
            optionalDescuento().map(f -> "descuento=" + f + ", ").orElse("") +
            optionalTotal().map(f -> "total=" + f + ", ").orElse("") +
            optionalPorcentajeImpuesto().map(f -> "porcentajeImpuesto=" + f + ", ").orElse("") +
            optionalCedula().map(f -> "cedula=" + f + ", ").orElse("") +
            optionalDireccion().map(f -> "direccion=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalEstado().map(f -> "estado=" + f + ", ").orElse("") +
            optionalDetallesId().map(f -> "detallesId=" + f + ", ").orElse("") +
            optionalCentroId().map(f -> "centroId=" + f + ", ").orElse("") +
            optionalClienteId().map(f -> "clienteId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
