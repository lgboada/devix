package com.devix.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devix.domain.UsuarioCentro} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsuarioCentroDTO implements Serializable {

    private Long id;

    @NotNull
    private Long noCia;

    @NotNull
    private Boolean principal;

    private CentroDTO centro;

    private UserDTO user;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UsuarioCentroDTO)) {
            return false;
        }

        UsuarioCentroDTO usuarioCentroDTO = (UsuarioCentroDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, usuarioCentroDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioCentroDTO{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", principal='" + getPrincipal() + "'" +
            ", centro=" + getCentro() +
            ", user=" + getUser() +
            "}";
    }
}
