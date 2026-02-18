package com.devix.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EventoCriteriaTest {

    @Test
    void newEventoCriteriaHasAllFiltersNullTest() {
        var eventoCriteria = new EventoCriteria();
        assertThat(eventoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void eventoCriteriaFluentMethodsCreatesFiltersTest() {
        var eventoCriteria = new EventoCriteria();

        setAllFilters(eventoCriteria);

        assertThat(eventoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void eventoCriteriaCopyCreatesNullFilterTest() {
        var eventoCriteria = new EventoCriteria();
        var copy = eventoCriteria.copy();

        assertThat(eventoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(eventoCriteria)
        );
    }

    @Test
    void eventoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var eventoCriteria = new EventoCriteria();
        setAllFilters(eventoCriteria);

        var copy = eventoCriteria.copy();

        assertThat(eventoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(eventoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var eventoCriteria = new EventoCriteria();

        assertThat(eventoCriteria).hasToString("EventoCriteria{}");
    }

    private static void setAllFilters(EventoCriteria eventoCriteria) {
        eventoCriteria.id();
        eventoCriteria.noCia();
        eventoCriteria.descripcion();
        eventoCriteria.fecha();
        eventoCriteria.estado();
        eventoCriteria.motivoConsulta();
        eventoCriteria.tratamiento();
        eventoCriteria.indicaciones();
        eventoCriteria.diagnostico1();
        eventoCriteria.diagnostico2();
        eventoCriteria.diagnostico3();
        eventoCriteria.diagnostico4();
        eventoCriteria.diagnostico5();
        eventoCriteria.diagnostico6();
        eventoCriteria.diagnostico7();
        eventoCriteria.observacion();
        eventoCriteria.documentoId();
        eventoCriteria.tipoEventoId();
        eventoCriteria.centroId();
        eventoCriteria.clienteId();
        eventoCriteria.distinct();
    }

    private static Condition<EventoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNoCia()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getFecha()) &&
                condition.apply(criteria.getEstado()) &&
                condition.apply(criteria.getMotivoConsulta()) &&
                condition.apply(criteria.getTratamiento()) &&
                condition.apply(criteria.getIndicaciones()) &&
                condition.apply(criteria.getDiagnostico1()) &&
                condition.apply(criteria.getDiagnostico2()) &&
                condition.apply(criteria.getDiagnostico3()) &&
                condition.apply(criteria.getDiagnostico4()) &&
                condition.apply(criteria.getDiagnostico5()) &&
                condition.apply(criteria.getDiagnostico6()) &&
                condition.apply(criteria.getDiagnostico7()) &&
                condition.apply(criteria.getObservacion()) &&
                condition.apply(criteria.getDocumentoId()) &&
                condition.apply(criteria.getTipoEventoId()) &&
                condition.apply(criteria.getCentroId()) &&
                condition.apply(criteria.getClienteId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EventoCriteria> copyFiltersAre(EventoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNoCia(), copy.getNoCia()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getFecha(), copy.getFecha()) &&
                condition.apply(criteria.getEstado(), copy.getEstado()) &&
                condition.apply(criteria.getMotivoConsulta(), copy.getMotivoConsulta()) &&
                condition.apply(criteria.getTratamiento(), copy.getTratamiento()) &&
                condition.apply(criteria.getIndicaciones(), copy.getIndicaciones()) &&
                condition.apply(criteria.getDiagnostico1(), copy.getDiagnostico1()) &&
                condition.apply(criteria.getDiagnostico2(), copy.getDiagnostico2()) &&
                condition.apply(criteria.getDiagnostico3(), copy.getDiagnostico3()) &&
                condition.apply(criteria.getDiagnostico4(), copy.getDiagnostico4()) &&
                condition.apply(criteria.getDiagnostico5(), copy.getDiagnostico5()) &&
                condition.apply(criteria.getDiagnostico6(), copy.getDiagnostico6()) &&
                condition.apply(criteria.getDiagnostico7(), copy.getDiagnostico7()) &&
                condition.apply(criteria.getObservacion(), copy.getObservacion()) &&
                condition.apply(criteria.getDocumentoId(), copy.getDocumentoId()) &&
                condition.apply(criteria.getTipoEventoId(), copy.getTipoEventoId()) &&
                condition.apply(criteria.getCentroId(), copy.getCentroId()) &&
                condition.apply(criteria.getClienteId(), copy.getClienteId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
