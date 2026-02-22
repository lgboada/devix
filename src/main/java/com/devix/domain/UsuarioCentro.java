package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A UsuarioCentro.
 */
@Entity
@Table(name = "usuario_centro")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsuarioCentro implements Serializable {

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
    @JsonIgnoreProperties(value = { "facturas", "eventos", "compania" }, allowSetters = true)
    private Centro centro;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UsuarioCentro id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public UsuarioCentro noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public Boolean getPrincipal() {
        return this.principal;
    }

    public UsuarioCentro principal(Boolean principal) {
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

    public UsuarioCentro centro(Centro centro) {
        this.setCentro(centro);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UsuarioCentro user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UsuarioCentro)) {
            return false;
        }
        return getId() != null && getId().equals(((UsuarioCentro) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioCentro{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", principal='" + getPrincipal() + "'" +
            "}";
    }
}
