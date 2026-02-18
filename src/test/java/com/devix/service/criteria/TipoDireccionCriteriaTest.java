package com.devix.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TipoDireccionCriteriaTest {

    @Test
    void newTipoDireccionCriteriaHasAllFiltersNullTest() {
        var tipoDireccionCriteria = new TipoDireccionCriteria();
        assertThat(tipoDireccionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void tipoDireccionCriteriaFluentMethodsCreatesFiltersTest() {
        var tipoDireccionCriteria = new TipoDireccionCriteria();

        setAllFilters(tipoDireccionCriteria);

        assertThat(tipoDireccionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void tipoDireccionCriteriaCopyCreatesNullFilterTest() {
        var tipoDireccionCriteria = new TipoDireccionCriteria();
        var copy = tipoDireccionCriteria.copy();

        assertThat(tipoDireccionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(tipoDireccionCriteria)
        );
    }

    @Test
    void tipoDireccionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tipoDireccionCriteria = new TipoDireccionCriteria();
        setAllFilters(tipoDireccionCriteria);

        var copy = tipoDireccionCriteria.copy();

        assertThat(tipoDireccionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(tipoDireccionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tipoDireccionCriteria = new TipoDireccionCriteria();

        assertThat(tipoDireccionCriteria).hasToString("TipoDireccionCriteria{}");
    }

    private static void setAllFilters(TipoDireccionCriteria tipoDireccionCriteria) {
        tipoDireccionCriteria.id();
        tipoDireccionCriteria.noCia();
        tipoDireccionCriteria.descripcion();
        tipoDireccionCriteria.direccionId();
        tipoDireccionCriteria.distinct();
    }

    private static Condition<TipoDireccionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNoCia()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getDireccionId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TipoDireccionCriteria> copyFiltersAre(
        TipoDireccionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNoCia(), copy.getNoCia()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getDireccionId(), copy.getDireccionId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
