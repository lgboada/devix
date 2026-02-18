package com.devix.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DetalleFacturaCriteriaTest {

    @Test
    void newDetalleFacturaCriteriaHasAllFiltersNullTest() {
        var detalleFacturaCriteria = new DetalleFacturaCriteria();
        assertThat(detalleFacturaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void detalleFacturaCriteriaFluentMethodsCreatesFiltersTest() {
        var detalleFacturaCriteria = new DetalleFacturaCriteria();

        setAllFilters(detalleFacturaCriteria);

        assertThat(detalleFacturaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void detalleFacturaCriteriaCopyCreatesNullFilterTest() {
        var detalleFacturaCriteria = new DetalleFacturaCriteria();
        var copy = detalleFacturaCriteria.copy();

        assertThat(detalleFacturaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(detalleFacturaCriteria)
        );
    }

    @Test
    void detalleFacturaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var detalleFacturaCriteria = new DetalleFacturaCriteria();
        setAllFilters(detalleFacturaCriteria);

        var copy = detalleFacturaCriteria.copy();

        assertThat(detalleFacturaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(detalleFacturaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var detalleFacturaCriteria = new DetalleFacturaCriteria();

        assertThat(detalleFacturaCriteria).hasToString("DetalleFacturaCriteria{}");
    }

    private static void setAllFilters(DetalleFacturaCriteria detalleFacturaCriteria) {
        detalleFacturaCriteria.id();
        detalleFacturaCriteria.noCia();
        detalleFacturaCriteria.cantidad();
        detalleFacturaCriteria.precioUnitario();
        detalleFacturaCriteria.subtotal();
        detalleFacturaCriteria.descuento();
        detalleFacturaCriteria.impuesto();
        detalleFacturaCriteria.total();
        detalleFacturaCriteria.facturaId();
        detalleFacturaCriteria.productoId();
        detalleFacturaCriteria.distinct();
    }

    private static Condition<DetalleFacturaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNoCia()) &&
                condition.apply(criteria.getCantidad()) &&
                condition.apply(criteria.getPrecioUnitario()) &&
                condition.apply(criteria.getSubtotal()) &&
                condition.apply(criteria.getDescuento()) &&
                condition.apply(criteria.getImpuesto()) &&
                condition.apply(criteria.getTotal()) &&
                condition.apply(criteria.getFacturaId()) &&
                condition.apply(criteria.getProductoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DetalleFacturaCriteria> copyFiltersAre(
        DetalleFacturaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNoCia(), copy.getNoCia()) &&
                condition.apply(criteria.getCantidad(), copy.getCantidad()) &&
                condition.apply(criteria.getPrecioUnitario(), copy.getPrecioUnitario()) &&
                condition.apply(criteria.getSubtotal(), copy.getSubtotal()) &&
                condition.apply(criteria.getDescuento(), copy.getDescuento()) &&
                condition.apply(criteria.getImpuesto(), copy.getImpuesto()) &&
                condition.apply(criteria.getTotal(), copy.getTotal()) &&
                condition.apply(criteria.getFacturaId(), copy.getFacturaId()) &&
                condition.apply(criteria.getProductoId(), copy.getProductoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
