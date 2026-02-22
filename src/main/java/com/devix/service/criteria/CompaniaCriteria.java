package com.devix.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.devix.domain.Compania} entity. This class is used
 * in {@link com.devix.web.rest.CompaniaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /companias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompaniaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter noCia;

    private StringFilter dni;

    private StringFilter nombre;

    private StringFilter direccion;

    private StringFilter email;

    private StringFilter telefono;

    private StringFilter pathImage;

    private BooleanFilter activa;

    private LongFilter centrosId;

    private Boolean distinct;

    public CompaniaCriteria() {}

    public CompaniaCriteria(CompaniaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.noCia = other.optionalNoCia().map(LongFilter::copy).orElse(null);
        this.dni = other.optionalDni().map(StringFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.direccion = other.optionalDireccion().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.telefono = other.optionalTelefono().map(StringFilter::copy).orElse(null);
        this.pathImage = other.optionalPathImage().map(StringFilter::copy).orElse(null);
        this.activa = other.optionalActiva().map(BooleanFilter::copy).orElse(null);
        this.centrosId = other.optionalCentrosId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CompaniaCriteria copy() {
        return new CompaniaCriteria(this);
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

    public StringFilter getDni() {
        return dni;
    }

    public Optional<StringFilter> optionalDni() {
        return Optional.ofNullable(dni);
    }

    public StringFilter dni() {
        if (dni == null) {
            setDni(new StringFilter());
        }
        return dni;
    }

    public void setDni(StringFilter dni) {
        this.dni = dni;
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

    public StringFilter getTelefono() {
        return telefono;
    }

    public Optional<StringFilter> optionalTelefono() {
        return Optional.ofNullable(telefono);
    }

    public StringFilter telefono() {
        if (telefono == null) {
            setTelefono(new StringFilter());
        }
        return telefono;
    }

    public void setTelefono(StringFilter telefono) {
        this.telefono = telefono;
    }

    public StringFilter getPathImage() {
        return pathImage;
    }

    public Optional<StringFilter> optionalPathImage() {
        return Optional.ofNullable(pathImage);
    }

    public StringFilter pathImage() {
        if (pathImage == null) {
            setPathImage(new StringFilter());
        }
        return pathImage;
    }

    public void setPathImage(StringFilter pathImage) {
        this.pathImage = pathImage;
    }

    public BooleanFilter getActiva() {
        return activa;
    }

    public Optional<BooleanFilter> optionalActiva() {
        return Optional.ofNullable(activa);
    }

    public BooleanFilter activa() {
        if (activa == null) {
            setActiva(new BooleanFilter());
        }
        return activa;
    }

    public void setActiva(BooleanFilter activa) {
        this.activa = activa;
    }

    public LongFilter getCentrosId() {
        return centrosId;
    }

    public Optional<LongFilter> optionalCentrosId() {
        return Optional.ofNullable(centrosId);
    }

    public LongFilter centrosId() {
        if (centrosId == null) {
            setCentrosId(new LongFilter());
        }
        return centrosId;
    }

    public void setCentrosId(LongFilter centrosId) {
        this.centrosId = centrosId;
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
        final CompaniaCriteria that = (CompaniaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(noCia, that.noCia) &&
            Objects.equals(dni, that.dni) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(direccion, that.direccion) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telefono, that.telefono) &&
            Objects.equals(pathImage, that.pathImage) &&
            Objects.equals(activa, that.activa) &&
            Objects.equals(centrosId, that.centrosId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, noCia, dni, nombre, direccion, email, telefono, pathImage, activa, centrosId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompaniaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNoCia().map(f -> "noCia=" + f + ", ").orElse("") +
            optionalDni().map(f -> "dni=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalDireccion().map(f -> "direccion=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalTelefono().map(f -> "telefono=" + f + ", ").orElse("") +
            optionalPathImage().map(f -> "pathImage=" + f + ", ").orElse("") +
            optionalActiva().map(f -> "activa=" + f + ", ").orElse("") +
            optionalCentrosId().map(f -> "centrosId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
