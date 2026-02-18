package com.devix.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.devix.domain.Modelo} entity. This class is used
 * in {@link com.devix.web.rest.ModeloResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /modelos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ModeloCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter noCia;

    private StringFilter nombre;

    private StringFilter pathImagen;

    private LongFilter productoId;

    private LongFilter marcaId;

    private Boolean distinct;

    public ModeloCriteria() {}

    public ModeloCriteria(ModeloCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.noCia = other.optionalNoCia().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.pathImagen = other.optionalPathImagen().map(StringFilter::copy).orElse(null);
        this.productoId = other.optionalProductoId().map(LongFilter::copy).orElse(null);
        this.marcaId = other.optionalMarcaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ModeloCriteria copy() {
        return new ModeloCriteria(this);
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

    public LongFilter getMarcaId() {
        return marcaId;
    }

    public Optional<LongFilter> optionalMarcaId() {
        return Optional.ofNullable(marcaId);
    }

    public LongFilter marcaId() {
        if (marcaId == null) {
            setMarcaId(new LongFilter());
        }
        return marcaId;
    }

    public void setMarcaId(LongFilter marcaId) {
        this.marcaId = marcaId;
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
        final ModeloCriteria that = (ModeloCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(noCia, that.noCia) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(pathImagen, that.pathImagen) &&
            Objects.equals(productoId, that.productoId) &&
            Objects.equals(marcaId, that.marcaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, noCia, nombre, pathImagen, productoId, marcaId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModeloCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNoCia().map(f -> "noCia=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalPathImagen().map(f -> "pathImagen=" + f + ", ").orElse("") +
            optionalProductoId().map(f -> "productoId=" + f + ", ").orElse("") +
            optionalMarcaId().map(f -> "marcaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
