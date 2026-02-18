package com.devix.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CentroCriteriaTest {

    @Test
    void newCentroCriteriaHasAllFiltersNullTest() {
        var centroCriteria = new CentroCriteria();
        assertThat(centroCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void centroCriteriaFluentMethodsCreatesFiltersTest() {
        var centroCriteria = new CentroCriteria();

        setAllFilters(centroCriteria);

        assertThat(centroCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void centroCriteriaCopyCreatesNullFilterTest() {
        var centroCriteria = new CentroCriteria();
        var copy = centroCriteria.copy();

        assertThat(centroCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(centroCriteria)
        );
    }

    @Test
    void centroCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var centroCriteria = new CentroCriteria();
        setAllFilters(centroCriteria);

        var copy = centroCriteria.copy();

        assertThat(centroCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(centroCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var centroCriteria = new CentroCriteria();

        assertThat(centroCriteria).hasToString("CentroCriteria{}");
    }

    private static void setAllFilters(CentroCriteria centroCriteria) {
        centroCriteria.id();
        centroCriteria.noCia();
        centroCriteria.descripcion();
        centroCriteria.facturaId();
        centroCriteria.eventoId();
        centroCriteria.companiaId();
        centroCriteria.distinct();
    }

    private static Condition<CentroCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNoCia()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getFacturaId()) &&
                condition.apply(criteria.getEventoId()) &&
                condition.apply(criteria.getCompaniaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CentroCriteria> copyFiltersAre(CentroCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNoCia(), copy.getNoCia()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getFacturaId(), copy.getFacturaId()) &&
                condition.apply(criteria.getEventoId(), copy.getEventoId()) &&
                condition.apply(criteria.getCompaniaId(), copy.getCompaniaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
