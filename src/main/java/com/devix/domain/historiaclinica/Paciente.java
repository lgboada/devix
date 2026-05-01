package com.devix.domain.historiaclinica;

import com.devix.domain.Ciudad;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "paciente")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Paciente implements Serializable {

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
    @Column(name = "dni", nullable = false, length = 50)
    private String dni;

    @Column(name = "tipo_dni", length = 1)
    private String tipoDni;

    @NotNull
    @Column(name = "nombres", nullable = false, length = 200)
    private String nombres;

    @NotNull
    @Column(name = "apellidos", nullable = false, length = 200)
    private String apellidos;

    @Column(name = "titulo", length = 250)
    private String titulo;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "sexo", length = 2)
    private String sexo;

    @Column(name = "orientacion_genero", length = 45)
    private String orientacionGenero;

    @Column(name = "identidad_sexual", length = 45)
    private String identidadSexual;

    @Column(name = "grupo_sanguineo", length = 5)
    private String grupoSanguineo;

    @Column(name = "estado_civil", length = 1)
    private String estadoCivil;

    @Column(name = "nivel_estudio", length = 250)
    private String nivelEstudio;

    @Column(name = "ocupacion", length = 120)
    private String ocupacion;

    @Column(name = "religion", length = 80)
    private String religion;

    @Column(name = "tipo_discapacidad", length = 45)
    private String tipoDiscapacidad;

    @Column(name = "porcentaje_discapacidad", length = 45)
    private String porcentajeDiscapacidad;

    @Column(name = "lateralidad", length = 45)
    private String lateralidad;

    @Column(name = "foto", length = 255)
    private String foto;

    @Column(name = "numero_historia", length = 15)
    private String numeroHistoria;

    @Column(name = "tipo_historia", length = 30)
    private String tipoHistoria;

    @Column(name = "prioritario", length = 45)
    private String prioritario;

    @Column(name = "comentario", length = 255)
    private String comentario;

    @Column(name = "actividades", length = 255)
    private String actividades;

    @Column(name = "agencia", length = 45)
    private String agencia;

    @Column(name = "area", length = 45)
    private String area;

    @Column(name = "ciiu", length = 45)
    private String ciiu;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;

    @Column(name = "fecha_egreso")
    private LocalDate fechaEgreso;

    @Column(name = "motivo_salida", length = 30)
    private String motivoSalida;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "estado", length = 3)
    private String estado;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "provincia" }, allowSetters = true)
    private Ciudad ciudad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private CategoriaPaciente categoriaPaciente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Paciente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public Paciente noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getDni() {
        return this.dni;
    }

    public Paciente dni(String dni) {
        this.setDni(dni);
        return this;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTipoDni() {
        return this.tipoDni;
    }

    public Paciente tipoDni(String tipoDni) {
        this.setTipoDni(tipoDni);
        return this;
    }

    public void setTipoDni(String tipoDni) {
        this.tipoDni = tipoDni;
    }

    public String getNombres() {
        return this.nombres;
    }

    public Paciente nombres(String nombres) {
        this.setNombres(nombres);
        return this;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public Paciente apellidos(String apellidos) {
        this.setApellidos(apellidos);
        return this;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Paciente titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public Paciente fechaNacimiento(LocalDate fechaNacimiento) {
        this.setFechaNacimiento(fechaNacimiento);
        return this;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return this.sexo;
    }

    public Paciente sexo(String sexo) {
        this.setSexo(sexo);
        return this;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getOrientacionGenero() {
        return this.orientacionGenero;
    }

    public Paciente orientacionGenero(String v) {
        this.setOrientacionGenero(v);
        return this;
    }

    public void setOrientacionGenero(String orientacionGenero) {
        this.orientacionGenero = orientacionGenero;
    }

    public String getIdentidadSexual() {
        return this.identidadSexual;
    }

    public Paciente identidadSexual(String v) {
        this.setIdentidadSexual(v);
        return this;
    }

    public void setIdentidadSexual(String identidadSexual) {
        this.identidadSexual = identidadSexual;
    }

    public String getGrupoSanguineo() {
        return this.grupoSanguineo;
    }

    public Paciente grupoSanguineo(String v) {
        this.setGrupoSanguineo(v);
        return this;
    }

    public void setGrupoSanguineo(String grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    public String getEstadoCivil() {
        return this.estadoCivil;
    }

    public Paciente estadoCivil(String v) {
        this.setEstadoCivil(v);
        return this;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getNivelEstudio() {
        return this.nivelEstudio;
    }

    public Paciente nivelEstudio(String v) {
        this.setNivelEstudio(v);
        return this;
    }

    public void setNivelEstudio(String nivelEstudio) {
        this.nivelEstudio = nivelEstudio;
    }

    public String getOcupacion() {
        return this.ocupacion;
    }

    public Paciente ocupacion(String v) {
        this.setOcupacion(v);
        return this;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getReligion() {
        return this.religion;
    }

    public Paciente religion(String v) {
        this.setReligion(v);
        return this;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getTipoDiscapacidad() {
        return this.tipoDiscapacidad;
    }

    public Paciente tipoDiscapacidad(String v) {
        this.setTipoDiscapacidad(v);
        return this;
    }

    public void setTipoDiscapacidad(String tipoDiscapacidad) {
        this.tipoDiscapacidad = tipoDiscapacidad;
    }

    public String getPorcentajeDiscapacidad() {
        return this.porcentajeDiscapacidad;
    }

    public Paciente porcentajeDiscapacidad(String v) {
        this.setPorcentajeDiscapacidad(v);
        return this;
    }

    public void setPorcentajeDiscapacidad(String porcentajeDiscapacidad) {
        this.porcentajeDiscapacidad = porcentajeDiscapacidad;
    }

    public String getLateralidad() {
        return this.lateralidad;
    }

    public Paciente lateralidad(String v) {
        this.setLateralidad(v);
        return this;
    }

    public void setLateralidad(String lateralidad) {
        this.lateralidad = lateralidad;
    }

    public String getFoto() {
        return this.foto;
    }

    public Paciente foto(String v) {
        this.setFoto(v);
        return this;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNumeroHistoria() {
        return this.numeroHistoria;
    }

    public Paciente numeroHistoria(String v) {
        this.setNumeroHistoria(v);
        return this;
    }

    public void setNumeroHistoria(String numeroHistoria) {
        this.numeroHistoria = numeroHistoria;
    }

    public String getTipoHistoria() {
        return this.tipoHistoria;
    }

    public Paciente tipoHistoria(String v) {
        this.setTipoHistoria(v);
        return this;
    }

    public void setTipoHistoria(String tipoHistoria) {
        this.tipoHistoria = tipoHistoria;
    }

    public String getPrioritario() {
        return this.prioritario;
    }

    public Paciente prioritario(String v) {
        this.setPrioritario(v);
        return this;
    }

    public void setPrioritario(String prioritario) {
        this.prioritario = prioritario;
    }

    public String getComentario() {
        return this.comentario;
    }

    public Paciente comentario(String v) {
        this.setComentario(v);
        return this;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getActividades() {
        return this.actividades;
    }

    public Paciente actividades(String v) {
        this.setActividades(v);
        return this;
    }

    public void setActividades(String actividades) {
        this.actividades = actividades;
    }

    public String getAgencia() {
        return this.agencia;
    }

    public Paciente agencia(String v) {
        this.setAgencia(v);
        return this;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getArea() {
        return this.area;
    }

    public Paciente area(String v) {
        this.setArea(v);
        return this;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCiiu() {
        return this.ciiu;
    }

    public Paciente ciiu(String v) {
        this.setCiiu(v);
        return this;
    }

    public void setCiiu(String ciiu) {
        this.ciiu = ciiu;
    }

    public LocalDate getFechaIngreso() {
        return this.fechaIngreso;
    }

    public Paciente fechaIngreso(LocalDate v) {
        this.setFechaIngreso(v);
        return this;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaEgreso() {
        return this.fechaEgreso;
    }

    public Paciente fechaEgreso(LocalDate v) {
        this.setFechaEgreso(v);
        return this;
    }

    public void setFechaEgreso(LocalDate fechaEgreso) {
        this.fechaEgreso = fechaEgreso;
    }

    public String getMotivoSalida() {
        return this.motivoSalida;
    }

    public Paciente motivoSalida(String v) {
        this.setMotivoSalida(v);
        return this;
    }

    public void setMotivoSalida(String motivoSalida) {
        this.motivoSalida = motivoSalida;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Paciente activo(Boolean v) {
        this.setActivo(v);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getEstado() {
        return this.estado;
    }

    public Paciente estado(String v) {
        this.setEstado(v);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Paciente fechaCreacion(LocalDate v) {
        this.setFechaCreacion(v);
        return this;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Ciudad getCiudad() {
        return this.ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public Paciente ciudad(Ciudad ciudad) {
        this.setCiudad(ciudad);
        return this;
    }

    public CategoriaPaciente getCategoriaPaciente() {
        return this.categoriaPaciente;
    }

    public void setCategoriaPaciente(CategoriaPaciente categoriaPaciente) {
        this.categoriaPaciente = categoriaPaciente;
    }

    public Paciente categoriaPaciente(CategoriaPaciente cp) {
        this.setCategoriaPaciente(cp);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paciente)) return false;
        return getId() != null && getId().equals(((Paciente) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paciente{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", dni='" + getDni() + "'" +
            ", nombres='" + getNombres() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", sexo='" + getSexo() + "'" +
            ", grupoSanguineo='" + getGrupoSanguineo() + "'" +
            ", numeroHistoria='" + getNumeroHistoria() + "'" +
            ", activo=" + getActivo() +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
