package com.devix.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MarcaCriteriaTest {

    @Test
    void newMarcaCriteriaHasAllFiltersNullTest() {
        var marcaCriteria = new MarcaCriteria();
        assertThat(marcaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void marcaCriteriaFluentMethodsCreatesFiltersTest() {
        var marcaCriteria = new MarcaCriteria();

        setAllFilters(marcaCriteria);

        assertThat(marcaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void marcaCriteriaCopyCreatesNullFilterTest() {
        var marcaCriteria = new MarcaCriteria();
        var copy = marcaCriteria.copy();

        assertThat(marcaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(marcaCriteria)
        );
    }

    @Test
    void marcaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var marcaCriteria = new MarcaCriteria();
        setAllFilters(marcaCriteria);

        var copy = marcaCriteria.copy();

        assertThat(marcaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(marcaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var marcaCriteria = new MarcaCriteria();

        assertThat(marcaCriteria).hasToString("MarcaCriteria{}");
    }

    private static void setAllFilters(MarcaCriteria marcaCriteria) {
        marcaCriteria.id();
        marcaCriteria.noCia();
        marcaCriteria.nombre();
        marcaCriteria.pathImagen();
        marcaCriteria.modelosId();
        marcaCriteria.distinct();
    }

    private static Condition<MarcaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNoCia()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getPathImagen()) &&
                condition.apply(criteria.getModelosId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MarcaCriteria> copyFiltersAre(MarcaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNoCia(), copy.getNoCia()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getPathImagen(), copy.getPathImagen()) &&
                condition.apply(criteria.getModelosId(), copy.getModelosId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
