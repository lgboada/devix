package com.devix.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProvinciaCriteriaTest {

    @Test
    void newProvinciaCriteriaHasAllFiltersNullTest() {
        var provinciaCriteria = new ProvinciaCriteria();
        assertThat(provinciaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void provinciaCriteriaFluentMethodsCreatesFiltersTest() {
        var provinciaCriteria = new ProvinciaCriteria();

        setAllFilters(provinciaCriteria);

        assertThat(provinciaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void provinciaCriteriaCopyCreatesNullFilterTest() {
        var provinciaCriteria = new ProvinciaCriteria();
        var copy = provinciaCriteria.copy();

        assertThat(provinciaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(provinciaCriteria)
        );
    }

    @Test
    void provinciaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var provinciaCriteria = new ProvinciaCriteria();
        setAllFilters(provinciaCriteria);

        var copy = provinciaCriteria.copy();

        assertThat(provinciaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(provinciaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var provinciaCriteria = new ProvinciaCriteria();

        assertThat(provinciaCriteria).hasToString("ProvinciaCriteria{}");
    }

    private static void setAllFilters(ProvinciaCriteria provinciaCriteria) {
        provinciaCriteria.id();
        provinciaCriteria.noCia();
        provinciaCriteria.descripcion();
        provinciaCriteria.ciudadId();
        provinciaCriteria.paisId();
        provinciaCriteria.distinct();
    }

    private static Condition<ProvinciaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNoCia()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getCiudadId()) &&
                condition.apply(criteria.getPaisId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProvinciaCriteria> copyFiltersAre(ProvinciaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNoCia(), copy.getNoCia()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getCiudadId(), copy.getCiudadId()) &&
                condition.apply(criteria.getPaisId(), copy.getPaisId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
