package com.devix.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TipoCatalogoCriteriaTest {

    @Test
    void newTipoCatalogoCriteriaHasAllFiltersNullTest() {
        var tipoCatalogoCriteria = new TipoCatalogoCriteria();
        assertThat(tipoCatalogoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void tipoCatalogoCriteriaFluentMethodsCreatesFiltersTest() {
        var tipoCatalogoCriteria = new TipoCatalogoCriteria();

        setAllFilters(tipoCatalogoCriteria);

        assertThat(tipoCatalogoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void tipoCatalogoCriteriaCopyCreatesNullFilterTest() {
        var tipoCatalogoCriteria = new TipoCatalogoCriteria();
        var copy = tipoCatalogoCriteria.copy();

        assertThat(tipoCatalogoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(tipoCatalogoCriteria)
        );
    }

    @Test
    void tipoCatalogoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tipoCatalogoCriteria = new TipoCatalogoCriteria();
        setAllFilters(tipoCatalogoCriteria);

        var copy = tipoCatalogoCriteria.copy();

        assertThat(tipoCatalogoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(tipoCatalogoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tipoCatalogoCriteria = new TipoCatalogoCriteria();

        assertThat(tipoCatalogoCriteria).hasToString("TipoCatalogoCriteria{}");
    }

    private static void setAllFilters(TipoCatalogoCriteria tipoCatalogoCriteria) {
        tipoCatalogoCriteria.id();
        tipoCatalogoCriteria.noCia();
        tipoCatalogoCriteria.descripcion();
        tipoCatalogoCriteria.categoria();
        tipoCatalogoCriteria.catalogoId();
        tipoCatalogoCriteria.distinct();
    }

    private static Condition<TipoCatalogoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNoCia()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getCategoria()) &&
                condition.apply(criteria.getCatalogoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TipoCatalogoCriteria> copyFiltersAre(
        TipoCatalogoCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNoCia(), copy.getNoCia()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getCategoria(), copy.getCategoria()) &&
                condition.apply(criteria.getCatalogoId(), copy.getCatalogoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
