package com.devix.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CatalogoCriteriaTest {

    @Test
    void newCatalogoCriteriaHasAllFiltersNullTest() {
        var catalogoCriteria = new CatalogoCriteria();
        assertThat(catalogoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void catalogoCriteriaFluentMethodsCreatesFiltersTest() {
        var catalogoCriteria = new CatalogoCriteria();

        setAllFilters(catalogoCriteria);

        assertThat(catalogoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void catalogoCriteriaCopyCreatesNullFilterTest() {
        var catalogoCriteria = new CatalogoCriteria();
        var copy = catalogoCriteria.copy();

        assertThat(catalogoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(catalogoCriteria)
        );
    }

    @Test
    void catalogoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var catalogoCriteria = new CatalogoCriteria();
        setAllFilters(catalogoCriteria);

        var copy = catalogoCriteria.copy();

        assertThat(catalogoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(catalogoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var catalogoCriteria = new CatalogoCriteria();

        assertThat(catalogoCriteria).hasToString("CatalogoCriteria{}");
    }

    private static void setAllFilters(CatalogoCriteria catalogoCriteria) {
        catalogoCriteria.id();
        catalogoCriteria.noCia();
        catalogoCriteria.descripcion1();
        catalogoCriteria.descripcion2();
        catalogoCriteria.estado();
        catalogoCriteria.orden();
        catalogoCriteria.texto1();
        catalogoCriteria.texto2();
        catalogoCriteria.tipoCatalogoId();
        catalogoCriteria.distinct();
    }

    private static Condition<CatalogoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNoCia()) &&
                condition.apply(criteria.getDescripcion1()) &&
                condition.apply(criteria.getDescripcion2()) &&
                condition.apply(criteria.getEstado()) &&
                condition.apply(criteria.getOrden()) &&
                condition.apply(criteria.getTexto1()) &&
                condition.apply(criteria.getTexto2()) &&
                condition.apply(criteria.getTipoCatalogoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CatalogoCriteria> copyFiltersAre(CatalogoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNoCia(), copy.getNoCia()) &&
                condition.apply(criteria.getDescripcion1(), copy.getDescripcion1()) &&
                condition.apply(criteria.getDescripcion2(), copy.getDescripcion2()) &&
                condition.apply(criteria.getEstado(), copy.getEstado()) &&
                condition.apply(criteria.getOrden(), copy.getOrden()) &&
                condition.apply(criteria.getTexto1(), copy.getTexto1()) &&
                condition.apply(criteria.getTexto2(), copy.getTexto2()) &&
                condition.apply(criteria.getTipoCatalogoId(), copy.getTipoCatalogoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
