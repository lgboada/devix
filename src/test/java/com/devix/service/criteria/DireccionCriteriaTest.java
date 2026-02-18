package com.devix.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DireccionCriteriaTest {

    @Test
    void newDireccionCriteriaHasAllFiltersNullTest() {
        var direccionCriteria = new DireccionCriteria();
        assertThat(direccionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void direccionCriteriaFluentMethodsCreatesFiltersTest() {
        var direccionCriteria = new DireccionCriteria();

        setAllFilters(direccionCriteria);

        assertThat(direccionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void direccionCriteriaCopyCreatesNullFilterTest() {
        var direccionCriteria = new DireccionCriteria();
        var copy = direccionCriteria.copy();

        assertThat(direccionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(direccionCriteria)
        );
    }

    @Test
    void direccionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var direccionCriteria = new DireccionCriteria();
        setAllFilters(direccionCriteria);

        var copy = direccionCriteria.copy();

        assertThat(direccionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(direccionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var direccionCriteria = new DireccionCriteria();

        assertThat(direccionCriteria).hasToString("DireccionCriteria{}");
    }

    private static void setAllFilters(DireccionCriteria direccionCriteria) {
        direccionCriteria.id();
        direccionCriteria.noCia();
        direccionCriteria.descripcion();
        direccionCriteria.pais();
        direccionCriteria.provincia();
        direccionCriteria.tipoDireccionId();
        direccionCriteria.clienteId();
        direccionCriteria.distinct();
    }

    private static Condition<DireccionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNoCia()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getPais()) &&
                condition.apply(criteria.getProvincia()) &&
                condition.apply(criteria.getTipoDireccionId()) &&
                condition.apply(criteria.getClienteId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DireccionCriteria> copyFiltersAre(DireccionCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNoCia(), copy.getNoCia()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getPais(), copy.getPais()) &&
                condition.apply(criteria.getProvincia(), copy.getProvincia()) &&
                condition.apply(criteria.getTipoDireccionId(), copy.getTipoDireccionId()) &&
                condition.apply(criteria.getClienteId(), copy.getClienteId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
