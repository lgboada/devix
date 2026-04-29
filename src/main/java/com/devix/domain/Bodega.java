package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * Bodega de inventario asociada a un {@link Centro}.
 */
@Entity
@Table(
    name = "bodega",
    uniqueConstraints = { @UniqueConstraint(name = "ux_bodega__centro_id__codigo", columnNames = { "centro_id", "codigo" }) }
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Bodega implements Serializable {

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
    @Size(max = 50)
    @Column(name = "codigo", length = 50, nullable = false)
    private String codigo;

    @NotNull
    @Size(max = 255)
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "activa", nullable = false)
    private Boolean activa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "facturas", "eventos" }, allowSetters = true)
    private Centro centro;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bodega id(Long id) {
        this.setId(id);
        return this;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public Bodega noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Bodega codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Bodega nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public Boolean getActiva() {
        return this.activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public Bodega activa(Boolean activa) {
        this.setActiva(activa);
        return this;
    }

    public Centro getCentro() {
        return this.centro;
    }

    public void setCentro(Centro centro) {
        this.centro = centro;
    }

    public Bodega centro(Centro centro) {
        this.setCentro(centro);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bodega)) {
            return false;
        }
        return getId() != null && getId().equals(((Bodega) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Bodega{" + "id=" + getId() + ", codigo='" + getCodigo() + "'" + ", nombre='" + getNombre() + "'" + "}";
    }
}
