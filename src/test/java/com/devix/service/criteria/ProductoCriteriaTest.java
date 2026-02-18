package com.devix.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductoCriteriaTest {

    @Test
    void newProductoCriteriaHasAllFiltersNullTest() {
        var productoCriteria = new ProductoCriteria();
        assertThat(productoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productoCriteriaFluentMethodsCreatesFiltersTest() {
        var productoCriteria = new ProductoCriteria();

        setAllFilters(productoCriteria);

        assertThat(productoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productoCriteriaCopyCreatesNullFilterTest() {
        var productoCriteria = new ProductoCriteria();
        var copy = productoCriteria.copy();

        assertThat(productoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productoCriteria)
        );
    }

    @Test
    void productoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productoCriteria = new ProductoCriteria();
        setAllFilters(productoCriteria);

        var copy = productoCriteria.copy();

        assertThat(productoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productoCriteria = new ProductoCriteria();

        assertThat(productoCriteria).hasToString("ProductoCriteria{}");
    }

    private static void setAllFilters(ProductoCriteria productoCriteria) {
        productoCriteria.id();
        productoCriteria.noCia();
        productoCriteria.nombre();
        productoCriteria.descripcion();
        productoCriteria.precio();
        productoCriteria.stock();
        productoCriteria.pathImagen();
        productoCriteria.codigo();
        productoCriteria.detalleFacturaId();
        productoCriteria.modeloId();
        productoCriteria.tipoProductoId();
        productoCriteria.proveedorId();
        productoCriteria.distinct();
    }

    private static Condition<ProductoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNoCia()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getPrecio()) &&
                condition.apply(criteria.getStock()) &&
                condition.apply(criteria.getPathImagen()) &&
                condition.apply(criteria.getCodigo()) &&
                condition.apply(criteria.getDetalleFacturaId()) &&
                condition.apply(criteria.getModeloId()) &&
                condition.apply(criteria.getTipoProductoId()) &&
                condition.apply(criteria.getProveedorId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductoCriteria> copyFiltersAre(ProductoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNoCia(), copy.getNoCia()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getPrecio(), copy.getPrecio()) &&
                condition.apply(criteria.getStock(), copy.getStock()) &&
                condition.apply(criteria.getPathImagen(), copy.getPathImagen()) &&
                condition.apply(criteria.getCodigo(), copy.getCodigo()) &&
                condition.apply(criteria.getDetalleFacturaId(), copy.getDetalleFacturaId()) &&
                condition.apply(criteria.getModeloId(), copy.getModeloId()) &&
                condition.apply(criteria.getTipoProductoId(), copy.getTipoProductoId()) &&
                condition.apply(criteria.getProveedorId(), copy.getProveedorId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
