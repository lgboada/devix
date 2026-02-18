package com.devix.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TipoEventoCriteriaTest {

    @Test
    void newTipoEventoCriteriaHasAllFiltersNullTest() {
        var tipoEventoCriteria = new TipoEventoCriteria();
        assertThat(tipoEventoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void tipoEventoCriteriaFluentMethodsCreatesFiltersTest() {
        var tipoEventoCriteria = new TipoEventoCriteria();

        setAllFilters(tipoEventoCriteria);

        assertThat(tipoEventoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void tipoEventoCriteriaCopyCreatesNullFilterTest() {
        var tipoEventoCriteria = new TipoEventoCriteria();
        var copy = tipoEventoCriteria.copy();

        assertThat(tipoEventoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(tipoEventoCriteria)
        );
    }

    @Test
    void tipoEventoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tipoEventoCriteria = new TipoEventoCriteria();
        setAllFilters(tipoEventoCriteria);

        var copy = tipoEventoCriteria.copy();

        assertThat(tipoEventoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(tipoEventoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tipoEventoCriteria = new TipoEventoCriteria();

        assertThat(tipoEventoCriteria).hasToString("TipoEventoCriteria{}");
    }

    private static void setAllFilters(TipoEventoCriteria tipoEventoCriteria) {
        tipoEventoCriteria.id();
        tipoEventoCriteria.noCia();
        tipoEventoCriteria.nombre();
        tipoEventoCriteria.eventoId();
        tipoEventoCriteria.distinct();
    }

    private static Condition<TipoEventoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNoCia()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getEventoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TipoEventoCriteria> copyFiltersAre(TipoEventoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNoCia(), copy.getNoCia()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getEventoId(), copy.getEventoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
