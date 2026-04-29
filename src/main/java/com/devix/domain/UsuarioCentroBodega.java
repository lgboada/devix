package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * Permiso de un usuario sobre una bodega específica dentro de un centro.
 */
@Entity
@Table(
    name = "usuario_centro_bodega",
    uniqueConstraints = { @UniqueConstraint(name = "ux_ucb__user_id__bodega_id", columnNames = { "user_id", "bodega_id" }) }
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsuarioCentroBodega implements Serializable {

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
    @Column(name = "principal", nullable = false)
    private Boolean principal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "facturas", "eventos" }, allowSetters = true)
    private Centro centro;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "centro" }, allowSetters = true)
    private Bodega bodega;

    public Long getId() {
        return this.id;
    }

    public UsuarioCentroBodega id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public UsuarioCentroBodega noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public Boolean getPrincipal() {
        return this.principal;
    }

    public UsuarioCentroBodega principal(Boolean principal) {
        this.setPrincipal(principal);
        return this;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public Centro getCentro() {
        return this.centro;
    }

    public void setCentro(Centro centro) {
        this.centro = centro;
    }

    public UsuarioCentroBodega centro(Centro centro) {
        this.setCentro(centro);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UsuarioCentroBodega user(User user) {
        this.setUser(user);
        return this;
    }

    public Bodega getBodega() {
        return this.bodega;
    }

    public void setBodega(Bodega bodega) {
        this.bodega = bodega;
    }

    public UsuarioCentroBodega bodega(Bodega bodega) {
        this.setBodega(bodega);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UsuarioCentroBodega)) {
            return false;
        }
        return getId() != null && getId().equals(((UsuarioCentroBodega) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "UsuarioCentroBodega{" + "id=" + getId() + ", noCia=" + getNoCia() + ", principal='" + getPrincipal() + "'" + "}";
    }
}
