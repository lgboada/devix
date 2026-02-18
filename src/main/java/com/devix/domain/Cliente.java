package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cliente implements Serializable {

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
    @Column(name = "dni", nullable = false, unique = true)
    private String dni;

    @NotNull
    @Column(name = "nombres", nullable = false)
    private String nombres;

    @NotNull
    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @Column(name = "nombre_comercial")
    private String nombreComercial;

    @NotNull
    @Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "telefono")
    private String telefono;

    @NotNull
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @NotNull
    @Column(name = "sexo", nullable = false)
    private String sexo;

    @NotNull
    @Column(name = "estado_civil", nullable = false)
    private String estadoCivil;

    @NotNull
    @Column(name = "tipo_sangre", nullable = false)
    private String tipoSangre;

    @NotNull
    @Column(name = "path_imagen", nullable = false)
    private String pathImagen;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
    @JsonIgnoreProperties(value = { "tipoDireccion", "cliente" }, allowSetters = true)
    private Set<Direccion> direcciones = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
    @JsonIgnoreProperties(value = { "detalles", "centro", "cliente" }, allowSetters = true)
    private Set<Factura> facturas = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
    @JsonIgnoreProperties(value = { "documentos", "tipoEvento", "centro", "cliente" }, allowSetters = true)
    private Set<Evento> eventos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
    @JsonIgnoreProperties(value = { "cliente", "evento" }, allowSetters = true)
    private Set<Documento> documentos = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "clientes" }, allowSetters = true)
    private TipoCliente tipoCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "clientes", "provincia" }, allowSetters = true)
    private Ciudad ciudad;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cliente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public Cliente noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getDni() {
        return this.dni;
    }

    public Cliente dni(String dni) {
        this.setDni(dni);
        return this;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombres() {
        return this.nombres;
    }

    public Cliente nombres(String nombres) {
        this.setNombres(nombres);
        return this;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public Cliente apellidos(String apellidos) {
        this.setApellidos(apellidos);
        return this;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombreComercial() {
        return this.nombreComercial;
    }

    public Cliente nombreComercial(String nombreComercial) {
        this.setNombreComercial(nombreComercial);
        return this;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getEmail() {
        return this.email;
    }

    public Cliente email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Cliente telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public Cliente fechaNacimiento(LocalDate fechaNacimiento) {
        this.setFechaNacimiento(fechaNacimiento);
        return this;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return this.sexo;
    }

    public Cliente sexo(String sexo) {
        this.setSexo(sexo);
        return this;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEstadoCivil() {
        return this.estadoCivil;
    }

    public Cliente estadoCivil(String estadoCivil) {
        this.setEstadoCivil(estadoCivil);
        return this;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getTipoSangre() {
        return this.tipoSangre;
    }

    public Cliente tipoSangre(String tipoSangre) {
        this.setTipoSangre(tipoSangre);
        return this;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public String getPathImagen() {
        return this.pathImagen;
    }

    public Cliente pathImagen(String pathImagen) {
        this.setPathImagen(pathImagen);
        return this;
    }

    public void setPathImagen(String pathImagen) {
        this.pathImagen = pathImagen;
    }

    public Set<Direccion> getDirecciones() {
        return this.direcciones;
    }

    public void setDirecciones(Set<Direccion> direccions) {
        if (this.direcciones != null) {
            this.direcciones.forEach(i -> i.setCliente(null));
        }
        if (direccions != null) {
            direccions.forEach(i -> i.setCliente(this));
        }
        this.direcciones = direccions;
    }

    public Cliente direcciones(Set<Direccion> direccions) {
        this.setDirecciones(direccions);
        return this;
    }

    public Cliente addDirecciones(Direccion direccion) {
        this.direcciones.add(direccion);
        direccion.setCliente(this);
        return this;
    }

    public Cliente removeDirecciones(Direccion direccion) {
        this.direcciones.remove(direccion);
        direccion.setCliente(null);
        return this;
    }

    public Set<Factura> getFacturas() {
        return this.facturas;
    }

    public void setFacturas(Set<Factura> facturas) {
        if (this.facturas != null) {
            this.facturas.forEach(i -> i.setCliente(null));
        }
        if (facturas != null) {
            facturas.forEach(i -> i.setCliente(this));
        }
        this.facturas = facturas;
    }

    public Cliente facturas(Set<Factura> facturas) {
        this.setFacturas(facturas);
        return this;
    }

    public Cliente addFacturas(Factura factura) {
        this.facturas.add(factura);
        factura.setCliente(this);
        return this;
    }

    public Cliente removeFacturas(Factura factura) {
        this.facturas.remove(factura);
        factura.setCliente(null);
        return this;
    }

    public Set<Evento> getEventos() {
        return this.eventos;
    }

    public void setEventos(Set<Evento> eventos) {
        if (this.eventos != null) {
            this.eventos.forEach(i -> i.setCliente(null));
        }
        if (eventos != null) {
            eventos.forEach(i -> i.setCliente(this));
        }
        this.eventos = eventos;
    }

    public Cliente eventos(Set<Evento> eventos) {
        this.setEventos(eventos);
        return this;
    }

    public Cliente addEvento(Evento evento) {
        this.eventos.add(evento);
        evento.setCliente(this);
        return this;
    }

    public Cliente removeEvento(Evento evento) {
        this.eventos.remove(evento);
        evento.setCliente(null);
        return this;
    }

    public Set<Documento> getDocumentos() {
        return this.documentos;
    }

    public void setDocumentos(Set<Documento> documentos) {
        if (this.documentos != null) {
            this.documentos.forEach(i -> i.setCliente(null));
        }
        if (documentos != null) {
            documentos.forEach(i -> i.setCliente(this));
        }
        this.documentos = documentos;
    }

    public Cliente documentos(Set<Documento> documentos) {
        this.setDocumentos(documentos);
        return this;
    }

    public Cliente addDocumento(Documento documento) {
        this.documentos.add(documento);
        documento.setCliente(this);
        return this;
    }

    public Cliente removeDocumento(Documento documento) {
        this.documentos.remove(documento);
        documento.setCliente(null);
        return this;
    }

    public TipoCliente getTipoCliente() {
        return this.tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public Cliente tipoCliente(TipoCliente tipoCliente) {
        this.setTipoCliente(tipoCliente);
        return this;
    }

    public Ciudad getCiudad() {
        return this.ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public Cliente ciudad(Ciudad ciudad) {
        this.setCiudad(ciudad);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cliente)) {
            return false;
        }
        return getId() != null && getId().equals(((Cliente) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", dni='" + getDni() + "'" +
            ", nombres='" + getNombres() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", nombreComercial='" + getNombreComercial() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", sexo='" + getSexo() + "'" +
            ", estadoCivil='" + getEstadoCivil() + "'" +
            ", tipoSangre='" + getTipoSangre() + "'" +
            ", pathImagen='" + getPathImagen() + "'" +
            "}";
    }
}
