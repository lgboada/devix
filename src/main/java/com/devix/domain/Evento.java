package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Evento.
 */
@Entity
@Table(name = "evento")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Evento implements Serializable {

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
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @Column(name = "estado")
    private String estado;

    @Column(name = "motivo_consulta")
    private String motivoConsulta;

    @Column(name = "tratamiento")
    private String tratamiento;

    @Column(name = "indicaciones")
    private String indicaciones;

    @Column(name = "diagnostico_1")
    private String diagnostico1;

    @Column(name = "diagnostico_2")
    private String diagnostico2;

    @Column(name = "diagnostico_3")
    private String diagnostico3;

    @Column(name = "diagnostico_4")
    private String diagnostico4;

    @Column(name = "diagnostico_5")
    private String diagnostico5;

    @Column(name = "diagnostico_6")
    private String diagnostico6;

    @Column(name = "diagnostico_7")
    private String diagnostico7;

    @Column(name = "observacion")
    private String observacion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "evento")
    @JsonIgnoreProperties(value = { "cliente", "evento" }, allowSetters = true)
    private Set<Documento> documentos = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "eventos" }, allowSetters = true)
    private TipoEvento tipoEvento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "facturas", "eventos", "compania" }, allowSetters = true)
    private Centro centro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "direcciones", "facturas", "eventos", "documentos", "tipoCliente", "ciudad" }, allowSetters = true)
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Evento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public Evento noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Evento descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Evento fecha(Instant fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return this.estado;
    }

    public Evento estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivoConsulta() {
        return this.motivoConsulta;
    }

    public Evento motivoConsulta(String motivoConsulta) {
        this.setMotivoConsulta(motivoConsulta);
        return this;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getTratamiento() {
        return this.tratamiento;
    }

    public Evento tratamiento(String tratamiento) {
        this.setTratamiento(tratamiento);
        return this;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getIndicaciones() {
        return this.indicaciones;
    }

    public Evento indicaciones(String indicaciones) {
        this.setIndicaciones(indicaciones);
        return this;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public String getDiagnostico1() {
        return this.diagnostico1;
    }

    public Evento diagnostico1(String diagnostico1) {
        this.setDiagnostico1(diagnostico1);
        return this;
    }

    public void setDiagnostico1(String diagnostico1) {
        this.diagnostico1 = diagnostico1;
    }

    public String getDiagnostico2() {
        return this.diagnostico2;
    }

    public Evento diagnostico2(String diagnostico2) {
        this.setDiagnostico2(diagnostico2);
        return this;
    }

    public void setDiagnostico2(String diagnostico2) {
        this.diagnostico2 = diagnostico2;
    }

    public String getDiagnostico3() {
        return this.diagnostico3;
    }

    public Evento diagnostico3(String diagnostico3) {
        this.setDiagnostico3(diagnostico3);
        return this;
    }

    public void setDiagnostico3(String diagnostico3) {
        this.diagnostico3 = diagnostico3;
    }

    public String getDiagnostico4() {
        return this.diagnostico4;
    }

    public Evento diagnostico4(String diagnostico4) {
        this.setDiagnostico4(diagnostico4);
        return this;
    }

    public void setDiagnostico4(String diagnostico4) {
        this.diagnostico4 = diagnostico4;
    }

    public String getDiagnostico5() {
        return this.diagnostico5;
    }

    public Evento diagnostico5(String diagnostico5) {
        this.setDiagnostico5(diagnostico5);
        return this;
    }

    public void setDiagnostico5(String diagnostico5) {
        this.diagnostico5 = diagnostico5;
    }

    public String getDiagnostico6() {
        return this.diagnostico6;
    }

    public Evento diagnostico6(String diagnostico6) {
        this.setDiagnostico6(diagnostico6);
        return this;
    }

    public void setDiagnostico6(String diagnostico6) {
        this.diagnostico6 = diagnostico6;
    }

    public String getDiagnostico7() {
        return this.diagnostico7;
    }

    public Evento diagnostico7(String diagnostico7) {
        this.setDiagnostico7(diagnostico7);
        return this;
    }

    public void setDiagnostico7(String diagnostico7) {
        this.diagnostico7 = diagnostico7;
    }

    public String getObservacion() {
        return this.observacion;
    }

    public Evento observacion(String observacion) {
        this.setObservacion(observacion);
        return this;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Set<Documento> getDocumentos() {
        return this.documentos;
    }

    public void setDocumentos(Set<Documento> documentos) {
        if (this.documentos != null) {
            this.documentos.forEach(i -> i.setEvento(null));
        }
        if (documentos != null) {
            documentos.forEach(i -> i.setEvento(this));
        }
        this.documentos = documentos;
    }

    public Evento documentos(Set<Documento> documentos) {
        this.setDocumentos(documentos);
        return this;
    }

    public Evento addDocumento(Documento documento) {
        this.documentos.add(documento);
        documento.setEvento(this);
        return this;
    }

    public Evento removeDocumento(Documento documento) {
        this.documentos.remove(documento);
        documento.setEvento(null);
        return this;
    }

    public TipoEvento getTipoEvento() {
        return this.tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public Evento tipoEvento(TipoEvento tipoEvento) {
        this.setTipoEvento(tipoEvento);
        return this;
    }

    public Centro getCentro() {
        return this.centro;
    }

    public void setCentro(Centro centro) {
        this.centro = centro;
    }

    public Evento centro(Centro centro) {
        this.setCentro(centro);
        return this;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Evento cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Evento)) {
            return false;
        }
        return getId() != null && getId().equals(((Evento) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Evento{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", descripcion='" + getDescripcion() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", estado='" + getEstado() + "'" +
            ", motivoConsulta='" + getMotivoConsulta() + "'" +
            ", tratamiento='" + getTratamiento() + "'" +
            ", indicaciones='" + getIndicaciones() + "'" +
            ", diagnostico1='" + getDiagnostico1() + "'" +
            ", diagnostico2='" + getDiagnostico2() + "'" +
            ", diagnostico3='" + getDiagnostico3() + "'" +
            ", diagnostico4='" + getDiagnostico4() + "'" +
            ", diagnostico5='" + getDiagnostico5() + "'" +
            ", diagnostico6='" + getDiagnostico6() + "'" +
            ", diagnostico7='" + getDiagnostico7() + "'" +
            ", observacion='" + getObservacion() + "'" +
            "}";
    }
}
