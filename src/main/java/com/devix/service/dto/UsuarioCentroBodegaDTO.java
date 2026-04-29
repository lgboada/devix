package com.devix.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devix.domain.UsuarioCentroBodega} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsuarioCentroBodegaDTO implements Serializable {

    private Long id;

    @NotNull
    private Long noCia;

    @NotNull
    private Boolean principal;

    private CentroDTO centro;

    private UserDTO user;

    @NotNull
    private BodegaDTO bodega;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return noCia;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public CentroDTO getCentro() {
        return centro;
    }

    public void setCentro(CentroDTO centro) {
        this.centro = centro;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public BodegaDTO getBodega() {
        return bodega;
    }

    public void setBodega(BodegaDTO bodega) {
        this.bodega = bodega;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UsuarioCentroBodegaDTO)) {
            return false;
        }
        UsuarioCentroBodegaDTO that = (UsuarioCentroBodegaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "UsuarioCentroBodegaDTO{" +
            "id=" +
            getId() +
            ", noCia=" +
            getNoCia() +
            ", principal='" +
            getPrincipal() +
            "'" +
            ", centro=" +
            getCentro() +
            ", user=" +
            getUser() +
            ", bodega=" +
            getBodega() +
            "}"
        );
    }
}
