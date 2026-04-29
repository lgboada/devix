package com.devix.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria for {@link com.devix.domain.UsuarioCentroBodega}.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsuarioCentroBodegaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter noCia;

    private BooleanFilter principal;

    private LongFilter centroId;

    private StringFilter userId;

    private LongFilter bodegaId;

    private Boolean distinct;

    public UsuarioCentroBodegaCriteria() {}

    public UsuarioCentroBodegaCriteria(UsuarioCentroBodegaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.noCia = other.optionalNoCia().map(LongFilter::copy).orElse(null);
        this.principal = other.optionalPrincipal().map(BooleanFilter::copy).orElse(null);
        this.centroId = other.optionalCentroId().map(LongFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(StringFilter::copy).orElse(null);
        this.bodegaId = other.optionalBodegaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UsuarioCentroBodegaCriteria copy() {
        return new UsuarioCentroBodegaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) setId(new LongFilter());
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
        if (noCia == null) setNoCia(new LongFilter());
        return noCia;
    }

    public void setNoCia(LongFilter noCia) {
        this.noCia = noCia;
    }

    public BooleanFilter getPrincipal() {
        return principal;
    }

    public Optional<BooleanFilter> optionalPrincipal() {
        return Optional.ofNullable(principal);
    }

    public BooleanFilter principal() {
        if (principal == null) setPrincipal(new BooleanFilter());
        return principal;
    }

    public void setPrincipal(BooleanFilter principal) {
        this.principal = principal;
    }

    public LongFilter getCentroId() {
        return centroId;
    }

    public Optional<LongFilter> optionalCentroId() {
        return Optional.ofNullable(centroId);
    }

    public LongFilter centroId() {
        if (centroId == null) setCentroId(new LongFilter());
        return centroId;
    }

    public void setCentroId(LongFilter centroId) {
        this.centroId = centroId;
    }

    public StringFilter getUserId() {
        return userId;
    }

    public Optional<StringFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public StringFilter userId() {
        if (userId == null) setUserId(new StringFilter());
        return userId;
    }

    public void setUserId(StringFilter userId) {
        this.userId = userId;
    }

    public LongFilter getBodegaId() {
        return bodegaId;
    }

    public Optional<LongFilter> optionalBodegaId() {
        return Optional.ofNullable(bodegaId);
    }

    public LongFilter bodegaId() {
        if (bodegaId == null) setBodegaId(new LongFilter());
        return bodegaId;
    }

    public void setBodegaId(LongFilter bodegaId) {
        this.bodegaId = bodegaId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) setDistinct(true);
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioCentroBodegaCriteria that = (UsuarioCentroBodegaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(noCia, that.noCia) &&
            Objects.equals(principal, that.principal) &&
            Objects.equals(centroId, that.centroId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(bodegaId, that.bodegaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, noCia, principal, centroId, userId, bodegaId, distinct);
    }

    @Override
    public String toString() {
        return (
            "UsuarioCentroBodegaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNoCia().map(f -> "noCia=" + f + ", ").orElse("") +
            optionalPrincipal().map(f -> "principal=" + f + ", ").orElse("") +
            optionalCentroId().map(f -> "centroId=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalBodegaId().map(f -> "bodegaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
            "}"
        );
    }
}
