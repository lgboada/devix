package com.devix.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ClienteCriteriaTest {

    @Test
    void newClienteCriteriaHasAllFiltersNullTest() {
        var clienteCriteria = new ClienteCriteria();
        assertThat(clienteCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void clienteCriteriaFluentMethodsCreatesFiltersTest() {
        var clienteCriteria = new ClienteCriteria();

        setAllFilters(clienteCriteria);

        assertThat(clienteCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void clienteCriteriaCopyCreatesNullFilterTest() {
        var clienteCriteria = new ClienteCriteria();
        var copy = clienteCriteria.copy();

        assertThat(clienteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(clienteCriteria)
        );
    }

    @Test
    void clienteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var clienteCriteria = new ClienteCriteria();
        setAllFilters(clienteCriteria);

        var copy = clienteCriteria.copy();

        assertThat(clienteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(clienteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var clienteCriteria = new ClienteCriteria();

        assertThat(clienteCriteria).hasToString("ClienteCriteria{}");
    }

    private static void setAllFilters(ClienteCriteria clienteCriteria) {
        clienteCriteria.id();
        clienteCriteria.noCia();
        clienteCriteria.dni();
        clienteCriteria.nombres();
        clienteCriteria.apellidos();
        clienteCriteria.nombreComercial();
        clienteCriteria.email();
        clienteCriteria.telefono1();
        clienteCriteria.telefono2();
        clienteCriteria.fechaNacimiento();
        clienteCriteria.sexo();
        clienteCriteria.estadoCivil();
        clienteCriteria.tipoSangre();
        clienteCriteria.pathImagen();
        clienteCriteria.direccionesId();
        clienteCriteria.facturasId();
        clienteCriteria.eventoId();
        clienteCriteria.documentoId();
        clienteCriteria.tipoClienteId();
        clienteCriteria.ciudadId();
        clienteCriteria.distinct();
    }

    private static Condition<ClienteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNoCia()) &&
                condition.apply(criteria.getDni()) &&
                condition.apply(criteria.getNombres()) &&
                condition.apply(criteria.getApellidos()) &&
                condition.apply(criteria.getNombreComercial()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getTelefono1()) &&
                condition.apply(criteria.getTelefono2()) &&
                condition.apply(criteria.getFechaNacimiento()) &&
                condition.apply(criteria.getSexo()) &&
                condition.apply(criteria.getEstadoCivil()) &&
                condition.apply(criteria.getTipoSangre()) &&
                condition.apply(criteria.getPathImagen()) &&
                condition.apply(criteria.getDireccionesId()) &&
                condition.apply(criteria.getFacturasId()) &&
                condition.apply(criteria.getEventoId()) &&
                condition.apply(criteria.getDocumentoId()) &&
                condition.apply(criteria.getTipoClienteId()) &&
                condition.apply(criteria.getCiudadId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ClienteCriteria> copyFiltersAre(ClienteCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNoCia(), copy.getNoCia()) &&
                condition.apply(criteria.getDni(), copy.getDni()) &&
                condition.apply(criteria.getNombres(), copy.getNombres()) &&
                condition.apply(criteria.getApellidos(), copy.getApellidos()) &&
                condition.apply(criteria.getNombreComercial(), copy.getNombreComercial()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getTelefono1(), copy.getTelefono1()) &&
                condition.apply(criteria.getTelefono2(), copy.getTelefono2()) &&
                condition.apply(criteria.getFechaNacimiento(), copy.getFechaNacimiento()) &&
                condition.apply(criteria.getSexo(), copy.getSexo()) &&
                condition.apply(criteria.getEstadoCivil(), copy.getEstadoCivil()) &&
                condition.apply(criteria.getTipoSangre(), copy.getTipoSangre()) &&
                condition.apply(criteria.getPathImagen(), copy.getPathImagen()) &&
                condition.apply(criteria.getDireccionesId(), copy.getDireccionesId()) &&
                condition.apply(criteria.getFacturasId(), copy.getFacturasId()) &&
                condition.apply(criteria.getEventoId(), copy.getEventoId()) &&
                condition.apply(criteria.getDocumentoId(), copy.getDocumentoId()) &&
                condition.apply(criteria.getTipoClienteId(), copy.getTipoClienteId()) &&
                condition.apply(criteria.getCiudadId(), copy.getCiudadId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
