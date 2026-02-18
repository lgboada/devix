package com.devix.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FacturaCriteriaTest {

    @Test
    void newFacturaCriteriaHasAllFiltersNullTest() {
        var facturaCriteria = new FacturaCriteria();
        assertThat(facturaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void facturaCriteriaFluentMethodsCreatesFiltersTest() {
        var facturaCriteria = new FacturaCriteria();

        setAllFilters(facturaCriteria);

        assertThat(facturaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void facturaCriteriaCopyCreatesNullFilterTest() {
        var facturaCriteria = new FacturaCriteria();
        var copy = facturaCriteria.copy();

        assertThat(facturaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(facturaCriteria)
        );
    }

    @Test
    void facturaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var facturaCriteria = new FacturaCriteria();
        setAllFilters(facturaCriteria);

        var copy = facturaCriteria.copy();

        assertThat(facturaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(facturaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var facturaCriteria = new FacturaCriteria();

        assertThat(facturaCriteria).hasToString("FacturaCriteria{}");
    }

    private static void setAllFilters(FacturaCriteria facturaCriteria) {
        facturaCriteria.id();
        facturaCriteria.noCia();
        facturaCriteria.serie();
        facturaCriteria.noFisico();
        facturaCriteria.fecha();
        facturaCriteria.subtotal();
        facturaCriteria.impuesto();
        facturaCriteria.impuestoCero();
        facturaCriteria.descuento();
        facturaCriteria.total();
        facturaCriteria.porcentajeImpuesto();
        facturaCriteria.cedula();
        facturaCriteria.direccion();
        facturaCriteria.email();
        facturaCriteria.estado();
        facturaCriteria.detallesId();
        facturaCriteria.centroId();
        facturaCriteria.clienteId();
        facturaCriteria.distinct();
    }

    private static Condition<FacturaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNoCia()) &&
                condition.apply(criteria.getSerie()) &&
                condition.apply(criteria.getNoFisico()) &&
                condition.apply(criteria.getFecha()) &&
                condition.apply(criteria.getSubtotal()) &&
                condition.apply(criteria.getImpuesto()) &&
                condition.apply(criteria.getImpuestoCero()) &&
                condition.apply(criteria.getDescuento()) &&
                condition.apply(criteria.getTotal()) &&
                condition.apply(criteria.getPorcentajeImpuesto()) &&
                condition.apply(criteria.getCedula()) &&
                condition.apply(criteria.getDireccion()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getEstado()) &&
                condition.apply(criteria.getDetallesId()) &&
                condition.apply(criteria.getCentroId()) &&
                condition.apply(criteria.getClienteId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FacturaCriteria> copyFiltersAre(FacturaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNoCia(), copy.getNoCia()) &&
                condition.apply(criteria.getSerie(), copy.getSerie()) &&
                condition.apply(criteria.getNoFisico(), copy.getNoFisico()) &&
                condition.apply(criteria.getFecha(), copy.getFecha()) &&
                condition.apply(criteria.getSubtotal(), copy.getSubtotal()) &&
                condition.apply(criteria.getImpuesto(), copy.getImpuesto()) &&
                condition.apply(criteria.getImpuestoCero(), copy.getImpuestoCero()) &&
                condition.apply(criteria.getDescuento(), copy.getDescuento()) &&
                condition.apply(criteria.getTotal(), copy.getTotal()) &&
                condition.apply(criteria.getPorcentajeImpuesto(), copy.getPorcentajeImpuesto()) &&
                condition.apply(criteria.getCedula(), copy.getCedula()) &&
                condition.apply(criteria.getDireccion(), copy.getDireccion()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getEstado(), copy.getEstado()) &&
                condition.apply(criteria.getDetallesId(), copy.getDetallesId()) &&
                condition.apply(criteria.getCentroId(), copy.getCentroId()) &&
                condition.apply(criteria.getClienteId(), copy.getClienteId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
