package com.devix.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TipoProductoCriteriaTest {

    @Test
    void newTipoProductoCriteriaHasAllFiltersNullTest() {
        var tipoProductoCriteria = new TipoProductoCriteria();
        assertThat(tipoProductoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void tipoProductoCriteriaFluentMethodsCreatesFiltersTest() {
        var tipoProductoCriteria = new TipoProductoCriteria();

        setAllFilters(tipoProductoCriteria);

        assertThat(tipoProductoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void tipoProductoCriteriaCopyCreatesNullFilterTest() {
        var tipoProductoCriteria = new TipoProductoCriteria();
        var copy = tipoProductoCriteria.copy();

        assertThat(tipoProductoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(tipoProductoCriteria)
        );
    }

    @Test
    void tipoProductoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tipoProductoCriteria = new TipoProductoCriteria();
        setAllFilters(tipoProductoCriteria);

        var copy = tipoProductoCriteria.copy();

        assertThat(tipoProductoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(tipoProductoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tipoProductoCriteria = new TipoProductoCriteria();

        assertThat(tipoProductoCriteria).hasToString("TipoProductoCriteria{}");
    }

    private static void setAllFilters(TipoProductoCriteria tipoProductoCriteria) {
        tipoProductoCriteria.id();
        tipoProductoCriteria.noCia();
        tipoProductoCriteria.nombre();
        tipoProductoCriteria.productoId();
        tipoProductoCriteria.distinct();
    }

    private static Condition<TipoProductoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNoCia()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getProductoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TipoProductoCriteria> copyFiltersAre(
        TipoProductoCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNoCia(), copy.getNoCia()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getProductoId(), copy.getProductoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
