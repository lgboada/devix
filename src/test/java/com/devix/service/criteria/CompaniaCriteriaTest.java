package com.devix.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CompaniaCriteriaTest {

    @Test
    void newCompaniaCriteriaHasAllFiltersNullTest() {
        var companiaCriteria = new CompaniaCriteria();
        assertThat(companiaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void companiaCriteriaFluentMethodsCreatesFiltersTest() {
        var companiaCriteria = new CompaniaCriteria();

        setAllFilters(companiaCriteria);

        assertThat(companiaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void companiaCriteriaCopyCreatesNullFilterTest() {
        var companiaCriteria = new CompaniaCriteria();
        var copy = companiaCriteria.copy();

        assertThat(companiaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(companiaCriteria)
        );
    }

    @Test
    void companiaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var companiaCriteria = new CompaniaCriteria();
        setAllFilters(companiaCriteria);

        var copy = companiaCriteria.copy();

        assertThat(companiaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(companiaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var companiaCriteria = new CompaniaCriteria();

        assertThat(companiaCriteria).hasToString("CompaniaCriteria{}");
    }

    private static void setAllFilters(CompaniaCriteria companiaCriteria) {
        companiaCriteria.id();
        companiaCriteria.noCia();
        companiaCriteria.dni();
        companiaCriteria.nombre();
        companiaCriteria.direccion();
        companiaCriteria.email();
        companiaCriteria.telefono();
        companiaCriteria.pathImage();
        companiaCriteria.activa();
        companiaCriteria.centrosId();
        companiaCriteria.distinct();
    }

    private static Condition<CompaniaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNoCia()) &&
                condition.apply(criteria.getDni()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getDireccion()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getTelefono()) &&
                condition.apply(criteria.getPathImage()) &&
                condition.apply(criteria.getActiva()) &&
                condition.apply(criteria.getCentrosId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CompaniaCriteria> copyFiltersAre(CompaniaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNoCia(), copy.getNoCia()) &&
                condition.apply(criteria.getDni(), copy.getDni()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getDireccion(), copy.getDireccion()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getTelefono(), copy.getTelefono()) &&
                condition.apply(criteria.getPathImage(), copy.getPathImage()) &&
                condition.apply(criteria.getActiva(), copy.getActiva()) &&
                condition.apply(criteria.getCentrosId(), copy.getCentrosId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
