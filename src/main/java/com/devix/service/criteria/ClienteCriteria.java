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

    private StringFilter nombres;

    private StringFilter apellidos;

    private StringFilter nombreComercial;

    private StringFilter email;

    private StringFilter telefono;

    private LocalDateFilter fechaNacimiento;

    private StringFilter sexo;

    private StringFilter estadoCivil;

    private StringFilter tipoSangre;

    private StringFilter pathImagen;

    private LongFilter direccionesId;

    private LongFilter facturasId;

    private LongFilter eventoId;

    private LongFilter documentoId;

    private LongFilter tipoClienteId;

    private LongFilter ciudadId;

    private Boolean distinct;

    public ClienteCriteria() {}

    public ClienteCriteria(ClienteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.noCia = other.optionalNoCia().map(LongFilter::copy).orElse(null);
        this.dni = other.optionalDni().map(StringFilter::copy).orElse(null);
        this.nombres = other.optionalNombres().map(StringFilter::copy).orElse(null);
        this.apellidos = other.optionalApellidos().map(StringFilter::copy).orElse(null);
        this.nombreComercial = other.optionalNombreComercial().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.telefono = other.optionalTelefono().map(StringFilter::copy).orElse(null);
        this.fechaNacimiento = other.optionalFechaNacimiento().map(LocalDateFilter::copy).orElse(null);
        this.sexo = other.optionalSexo().map(StringFilter::copy).orElse(null);
        this.estadoCivil = other.optionalEstadoCivil().map(StringFilter::copy).orElse(null);
        this.tipoSangre = other.optionalTipoSangre().map(StringFilter::copy).orElse(null);
        this.pathImagen = other.optionalPathImagen().map(StringFilter::copy).orElse(null);
        this.direccionesId = other.optionalDireccionesId().map(LongFilter::copy).orElse(null);
        this.facturasId = other.optionalFacturasId().map(LongFilter::copy).orElse(null);
        this.eventoId = other.optionalEventoId().map(LongFilter::copy).orElse(null);
        this.documentoId = other.optionalDocumentoId().map(LongFilter::copy).orElse(null);
        this.tipoClienteId = other.optionalTipoClienteId().map(LongFilter::copy).orElse(null);
        this.ciudadId = other.optionalCiudadId().map(LongFilter::copy).orElse(null);
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

    public StringFilter getTelefono() {
        return telefono;
    }

    public Optional<StringFilter> optionalTelefono() {
        return Optional.ofNullable(telefono);
    }

    public StringFilter telefono() {
        if (telefono == null) {
            setTelefono(new StringFilter());
        }
        return telefono;
    }

    public void setTelefono(StringFilter telefono) {
        this.telefono = telefono;
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

    public StringFilter getTipoSangre() {
        return tipoSangre;
    }

    public Optional<StringFilter> optionalTipoSangre() {
        return Optional.ofNullable(tipoSangre);
    }

    public StringFilter tipoSangre() {
        if (tipoSangre == null) {
            setTipoSangre(new StringFilter());
        }
        return tipoSangre;
    }

    public void setTipoSangre(StringFilter tipoSangre) {
        this.tipoSangre = tipoSangre;
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

    public LongFilter getTipoClienteId() {
        return tipoClienteId;
    }

    public Optional<LongFilter> optionalTipoClienteId() {
        return Optional.ofNullable(tipoClienteId);
    }

    public LongFilter tipoClienteId() {
        if (tipoClienteId == null) {
            setTipoClienteId(new LongFilter());
        }
        return tipoClienteId;
    }

    public void setTipoClienteId(LongFilter tipoClienteId) {
        this.tipoClienteId = tipoClienteId;
    }

    public LongFilter getCiudadId() {
        return ciudadId;
    }

    public Optional<LongFilter> optionalCiudadId() {
        return Optional.ofNullable(ciudadId);
    }

    public LongFilter ciudadId() {
        if (ciudadId == null) {
            setCiudadId(new LongFilter());
        }
        return ciudadId;
    }

    public void setCiudadId(LongFilter ciudadId) {
        this.ciudadId = ciudadId;
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
            Objects.equals(nombres, that.nombres) &&
            Objects.equals(apellidos, that.apellidos) &&
            Objects.equals(nombreComercial, that.nombreComercial) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telefono, that.telefono) &&
            Objects.equals(fechaNacimiento, that.fechaNacimiento) &&
            Objects.equals(sexo, that.sexo) &&
            Objects.equals(estadoCivil, that.estadoCivil) &&
            Objects.equals(tipoSangre, that.tipoSangre) &&
            Objects.equals(pathImagen, that.pathImagen) &&
            Objects.equals(direccionesId, that.direccionesId) &&
            Objects.equals(facturasId, that.facturasId) &&
            Objects.equals(eventoId, that.eventoId) &&
            Objects.equals(documentoId, that.documentoId) &&
            Objects.equals(tipoClienteId, that.tipoClienteId) &&
            Objects.equals(ciudadId, that.ciudadId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            noCia,
            dni,
            nombres,
            apellidos,
            nombreComercial,
            email,
            telefono,
            fechaNacimiento,
            sexo,
            estadoCivil,
            tipoSangre,
            pathImagen,
            direccionesId,
            facturasId,
            eventoId,
            documentoId,
            tipoClienteId,
            ciudadId,
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
            optionalNombres().map(f -> "nombres=" + f + ", ").orElse("") +
            optionalApellidos().map(f -> "apellidos=" + f + ", ").orElse("") +
            optionalNombreComercial().map(f -> "nombreComercial=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalTelefono().map(f -> "telefono=" + f + ", ").orElse("") +
            optionalFechaNacimiento().map(f -> "fechaNacimiento=" + f + ", ").orElse("") +
            optionalSexo().map(f -> "sexo=" + f + ", ").orElse("") +
            optionalEstadoCivil().map(f -> "estadoCivil=" + f + ", ").orElse("") +
            optionalTipoSangre().map(f -> "tipoSangre=" + f + ", ").orElse("") +
            optionalPathImagen().map(f -> "pathImagen=" + f + ", ").orElse("") +
            optionalDireccionesId().map(f -> "direccionesId=" + f + ", ").orElse("") +
            optionalFacturasId().map(f -> "facturasId=" + f + ", ").orElse("") +
            optionalEventoId().map(f -> "eventoId=" + f + ", ").orElse("") +
            optionalDocumentoId().map(f -> "documentoId=" + f + ", ").orElse("") +
            optionalTipoClienteId().map(f -> "tipoClienteId=" + f + ", ").orElse("") +
            optionalCiudadId().map(f -> "ciudadId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
