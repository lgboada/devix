package com.devix.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UsuarioCentroCriteriaTest {

    @Test
    void newUsuarioCentroCriteriaHasAllFiltersNullTest() {
        var usuarioCentroCriteria = new UsuarioCentroCriteria();
        assertThat(usuarioCentroCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void usuarioCentroCriteriaFluentMethodsCreatesFiltersTest() {
        var usuarioCentroCriteria = new UsuarioCentroCriteria();

        setAllFilters(usuarioCentroCriteria);

        assertThat(usuarioCentroCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void usuarioCentroCriteriaCopyCreatesNullFilterTest() {
        var usuarioCentroCriteria = new UsuarioCentroCriteria();
        var copy = usuarioCentroCriteria.copy();

        assertThat(usuarioCentroCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(usuarioCentroCriteria)
        );
    }

    @Test
    void usuarioCentroCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var usuarioCentroCriteria = new UsuarioCentroCriteria();
        setAllFilters(usuarioCentroCriteria);

        var copy = usuarioCentroCriteria.copy();

        assertThat(usuarioCentroCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(usuarioCentroCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var usuarioCentroCriteria = new UsuarioCentroCriteria();

        assertThat(usuarioCentroCriteria).hasToString("UsuarioCentroCriteria{}");
    }

    private static void setAllFilters(UsuarioCentroCriteria usuarioCentroCriteria) {
        usuarioCentroCriteria.id();
        usuarioCentroCriteria.noCia();
        usuarioCentroCriteria.principal();
        usuarioCentroCriteria.centroId();
        usuarioCentroCriteria.userId();
        usuarioCentroCriteria.distinct();
    }

    private static Condition<UsuarioCentroCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNoCia()) &&
                condition.apply(criteria.getPrincipal()) &&
                condition.apply(criteria.getCentroId()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UsuarioCentroCriteria> copyFiltersAre(
        UsuarioCentroCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNoCia(), copy.getNoCia()) &&
                condition.apply(criteria.getPrincipal(), copy.getPrincipal()) &&
                condition.apply(criteria.getCentroId(), copy.getCentroId()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
