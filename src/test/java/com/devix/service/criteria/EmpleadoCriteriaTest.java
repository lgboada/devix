package com.devix.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EmpleadoCriteriaTest {

    @Test
    void newEmpleadoCriteriaHasAllFiltersNullTest() {
        var empleadoCriteria = new EmpleadoCriteria();
        assertThat(empleadoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void empleadoCriteriaFluentMethodsCreatesFiltersTest() {
        var empleadoCriteria = new EmpleadoCriteria();

        setAllFilters(empleadoCriteria);

        assertThat(empleadoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void empleadoCriteriaCopyCreatesNullFilterTest() {
        var empleadoCriteria = new EmpleadoCriteria();
        var copy = empleadoCriteria.copy();

        assertThat(empleadoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(empleadoCriteria)
        );
    }

    @Test
    void empleadoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var empleadoCriteria = new EmpleadoCriteria();
        setAllFilters(empleadoCriteria);

        var copy = empleadoCriteria.copy();

        assertThat(empleadoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(empleadoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var empleadoCriteria = new EmpleadoCriteria();

        assertThat(empleadoCriteria).hasToString("EmpleadoCriteria{}");
    }

    private static void setAllFilters(EmpleadoCriteria empleadoCriteria) {
        empleadoCriteria.id();
        empleadoCriteria.noCia();
        empleadoCriteria.dni();
        empleadoCriteria.nombre();
        empleadoCriteria.contacto();
        empleadoCriteria.email();
        empleadoCriteria.pathImagen();
        empleadoCriteria.telefono();
        empleadoCriteria.distinct();
    }

    private static Condition<EmpleadoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNoCia()) &&
                condition.apply(criteria.getDni()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getContacto()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getPathImagen()) &&
                condition.apply(criteria.getTelefono()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EmpleadoCriteria> copyFiltersAre(EmpleadoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNoCia(), copy.getNoCia()) &&
                condition.apply(criteria.getDni(), copy.getDni()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getContacto(), copy.getContacto()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getPathImagen(), copy.getPathImagen()) &&
                condition.apply(criteria.getTelefono(), copy.getTelefono()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
