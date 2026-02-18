package com.devix.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TipoClienteCriteriaTest {

    @Test
    void newTipoClienteCriteriaHasAllFiltersNullTest() {
        var tipoClienteCriteria = new TipoClienteCriteria();
        assertThat(tipoClienteCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void tipoClienteCriteriaFluentMethodsCreatesFiltersTest() {
        var tipoClienteCriteria = new TipoClienteCriteria();

        setAllFilters(tipoClienteCriteria);

        assertThat(tipoClienteCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void tipoClienteCriteriaCopyCreatesNullFilterTest() {
        var tipoClienteCriteria = new TipoClienteCriteria();
        var copy = tipoClienteCriteria.copy();

        assertThat(tipoClienteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(tipoClienteCriteria)
        );
    }

    @Test
    void tipoClienteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tipoClienteCriteria = new TipoClienteCriteria();
        setAllFilters(tipoClienteCriteria);

        var copy = tipoClienteCriteria.copy();

        assertThat(tipoClienteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(tipoClienteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tipoClienteCriteria = new TipoClienteCriteria();

        assertThat(tipoClienteCriteria).hasToString("TipoClienteCriteria{}");
    }

    private static void setAllFilters(TipoClienteCriteria tipoClienteCriteria) {
        tipoClienteCriteria.id();
        tipoClienteCriteria.noCia();
        tipoClienteCriteria.descripcion();
        tipoClienteCriteria.clienteId();
        tipoClienteCriteria.distinct();
    }

    private static Condition<TipoClienteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNoCia()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getClienteId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TipoClienteCriteria> copyFiltersAre(TipoClienteCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNoCia(), copy.getNoCia()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getClienteId(), copy.getClienteId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
