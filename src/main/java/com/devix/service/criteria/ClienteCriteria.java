package com.devix.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.devix.domain.Cliente} entity. This class is used
 * in {@link com.devix.web.rest.ClienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClienteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter noCia;

    private StringFilter dni;

    private StringFilter tipoDocumento;

    private StringFilter nombres;

    private StringFilter apellidos;

    private StringFilter search;

    private StringFilter nombreComercial;

    private StringFilter email;

    private StringFilter telefono1;

    private LocalDateFilter fechaNacimiento;

    private StringFilter sexo;

    private StringFilter estadoCivil;

    private LongFilter direccionesId;

    private LongFilter facturasId;

    private LongFilter eventoId;

    private LongFilter documentoId;

    private Boolean distinct;

    public ClienteCriteria() {}

    public ClienteCriteria(ClienteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.noCia = other.optionalNoCia().map(LongFilter::copy).orElse(null);
        this.dni = other.optionalDni().map(StringFilter::copy).orElse(null);
        this.tipoDocumento = other.optionalTipoDocumento().map(StringFilter::copy).orElse(null);
        this.nombres = other.optionalNombres().map(StringFilter::copy).orElse(null);
        this.apellidos = other.optionalApellidos().map(StringFilter::copy).orElse(null);
        this.search = other.optionalSearch().map(StringFilter::copy).orElse(null);
        this.nombreComercial = other.optionalNombreComercial().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.telefono1 = other.optionalTelefono1().map(StringFilter::copy).orElse(null);
        this.fechaNacimiento = other.optionalFechaNacimiento().map(LocalDateFilter::copy).orElse(null);
        this.sexo = other.optionalSexo().map(StringFilter::copy).orElse(null);
        this.estadoCivil = other.optionalEstadoCivil().map(StringFilter::copy).orElse(null);
        this.direccionesId = other.optionalDireccionesId().map(LongFilter::copy).orElse(null);
        this.facturasId = other.optionalFacturasId().map(LongFilter::copy).orElse(null);
        this.eventoId = other.optionalEventoId().map(LongFilter::copy).orElse(null);
        this.documentoId = other.optionalDocumentoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ClienteCriteria copy() {
        return new ClienteCriteria(this);
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

    public StringFilter getTipoDocumento() {
        return tipoDocumento;
    }

    public Optional<StringFilter> optionalTipoDocumento() {
        return Optional.ofNullable(tipoDocumento);
    }

    public StringFilter tipoDocumento() {
        if (tipoDocumento == null) {
            setTipoDocumento(new StringFilter());
        }
        return tipoDocumento;
    }

    public void setTipoDocumento(StringFilter tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public StringFilter getNombres() {
        return nombres;
    }

    public Optional<StringFilter> optionalNombres() {
        return Optional.ofNullable(nombres);
    }

    public StringFilter nombres() {
        if (nombres == null) {
            setNombres(new StringFilter());
        }
        return nombres;
    }

    public void setNombres(StringFilter nombres) {
        this.nombres = nombres;
    }

    public StringFilter getApellidos() {
        return apellidos;
    }

    public Optional<StringFilter> optionalApellidos() {
        return Optional.ofNullable(apellidos);
    }

    public StringFilter apellidos() {
        if (apellidos == null) {
            setApellidos(new StringFilter());
        }
        return apellidos;
    }

    public void setApellidos(StringFilter apellidos) {
        this.apellidos = apellidos;
    }

    public StringFilter getSearch() {
        return search;
    }

    public Optional<StringFilter> optionalSearch() {
        return Optional.ofNullable(search);
    }

    public StringFilter search() {
        if (search == null) {
            setSearch(new StringFilter());
        }
        return search;
    }

    public void setSearch(StringFilter search) {
        this.search = search;
    }

    public StringFilter getNombreComercial() {
        return nombreComercial;
    }

    public Optional<StringFilter> optionalNombreComercial() {
        return Optional.ofNullable(nombreComercial);
    }

    public StringFilter nombreComercial() {
        if (nombreComercial == null) {
            setNombreComercial(new StringFilter());
        }
        return nombreComercial;
    }

    public void setNombreComercial(StringFilter nombreComercial) {
        this.nombreComercial = nombreComercial;
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

    public StringFilter getTelefono1() {
        return telefono1;
    }

    public Optional<StringFilter> optionalTelefono1() {
        return Optional.ofNullable(telefono1);
    }

    public StringFilter telefono1() {
        if (telefono1 == null) {
            setTelefono1(new StringFilter());
        }
        return telefono1;
    }

    public void setTelefono1(StringFilter telefono1) {
        this.telefono1 = telefono1;
    }

    public LocalDateFilter getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Optional<LocalDateFilter> optionalFechaNacimiento() {
        return Optional.ofNullable(fechaNacimiento);
    }

    public LocalDateFilter fechaNacimiento() {
        if (fechaNacimiento == null) {
            setFechaNacimiento(new LocalDateFilter());
        }
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDateFilter fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public StringFilter getSexo() {
        return sexo;
    }

    public Optional<StringFilter> optionalSexo() {
        return Optional.ofNullable(sexo);
    }

    public StringFilter sexo() {
        if (sexo == null) {
            setSexo(new StringFilter());
        }
        return sexo;
    }

    public void setSexo(StringFilter sexo) {
        this.sexo = sexo;
    }

    public StringFilter getEstadoCivil() {
        return estadoCivil;
    }

    public Optional<StringFilter> optionalEstadoCivil() {
        return Optional.ofNullable(estadoCivil);
    }

    public StringFilter estadoCivil() {
        if (estadoCivil == null) {
            setEstadoCivil(new StringFilter());
        }
        return estadoCivil;
    }

    public void setEstadoCivil(StringFilter estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public LongFilter getDireccionesId() {
        return direccionesId;
    }

    public Optional<LongFilter> optionalDireccionesId() {
        return Optional.ofNullable(direccionesId);
    }

    public LongFilter direccionesId() {
        if (direccionesId == null) {
            setDireccionesId(new LongFilter());
        }
        return direccionesId;
    }

    public void setDireccionesId(LongFilter direccionesId) {
        this.direccionesId = direccionesId;
    }

    public LongFilter getFacturasId() {
        return facturasId;
    }

    public Optional<LongFilter> optionalFacturasId() {
        return Optional.ofNullable(facturasId);
    }

    public LongFilter facturasId() {
        if (facturasId == null) {
            setFacturasId(new LongFilter());
        }
        return facturasId;
    }

    public void setFacturasId(LongFilter facturasId) {
        this.facturasId = facturasId;
    }

    public LongFilter getEventoId() {
        return eventoId;
    }

    public Optional<LongFilter> optionalEventoId() {
        return Optional.ofNullable(eventoId);
    }

    public LongFilter eventoId() {
        if (eventoId == null) {
            setEventoId(new LongFilter());
        }
        return eventoId;
    }

    public void setEventoId(LongFilter eventoId) {
        this.eventoId = eventoId;
    }

    public LongFilter getDocumentoId() {
        return documentoId;
    }

    public Optional<LongFilter> optionalDocumentoId() {
        return Optional.ofNullable(documentoId);
    }

    public LongFilter documentoId() {
        if (documentoId == null) {
            setDocumentoId(new LongFilter());
        }
        return documentoId;
    }

    public void setDocumentoId(LongFilter documentoId) {
        this.documentoId = documentoId;
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
        final ClienteCriteria that = (ClienteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(noCia, that.noCia) &&
            Objects.equals(dni, that.dni) &&
            Objects.equals(tipoDocumento, that.tipoDocumento) &&
            Objects.equals(nombres, that.nombres) &&
            Objects.equals(apellidos, that.apellidos) &&
            Objects.equals(search, that.search) &&
            Objects.equals(nombreComercial, that.nombreComercial) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telefono1, that.telefono1) &&
            Objects.equals(fechaNacimiento, that.fechaNacimiento) &&
            Objects.equals(sexo, that.sexo) &&
            Objects.equals(estadoCivil, that.estadoCivil) &&
            Objects.equals(direccionesId, that.direccionesId) &&
            Objects.equals(facturasId, that.facturasId) &&
            Objects.equals(eventoId, that.eventoId) &&
            Objects.equals(documentoId, that.documentoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            noCia,
            dni,
            tipoDocumento,
            nombres,
            apellidos,
            search,
            nombreComercial,
            email,
            telefono1,
            fechaNacimiento,
            sexo,
            estadoCivil,
            direccionesId,
            facturasId,
            eventoId,
            documentoId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNoCia().map(f -> "noCia=" + f + ", ").orElse("") +
            optionalDni().map(f -> "dni=" + f + ", ").orElse("") +
            optionalTipoDocumento().map(f -> "tipoDocumento=" + f + ", ").orElse("") +
            optionalNombres().map(f -> "nombres=" + f + ", ").orElse("") +
            optionalApellidos().map(f -> "apellidos=" + f + ", ").orElse("") +
            optionalSearch().map(f -> "search=" + f + ", ").orElse("") +
            optionalNombreComercial().map(f -> "nombreComercial=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalTelefono1().map(f -> "telefono1=" + f + ", ").orElse("") +
            optionalFechaNacimiento().map(f -> "fechaNacimiento=" + f + ", ").orElse("") +
            optionalSexo().map(f -> "sexo=" + f + ", ").orElse("") +
            optionalEstadoCivil().map(f -> "estadoCivil=" + f + ", ").orElse("") +
            optionalDireccionesId().map(f -> "direccionesId=" + f + ", ").orElse("") +
            optionalFacturasId().map(f -> "facturasId=" + f + ", ").orElse("") +
            optionalEventoId().map(f -> "eventoId=" + f + ", ").orElse("") +
            optionalDocumentoId().map(f -> "documentoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
