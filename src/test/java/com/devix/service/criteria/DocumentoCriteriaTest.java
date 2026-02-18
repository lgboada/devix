package com.devix.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentoCriteriaTest {

    @Test
    void newDocumentoCriteriaHasAllFiltersNullTest() {
        var documentoCriteria = new DocumentoCriteria();
        assertThat(documentoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentoCriteriaFluentMethodsCreatesFiltersTest() {
        var documentoCriteria = new DocumentoCriteria();

        setAllFilters(documentoCriteria);

        assertThat(documentoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentoCriteriaCopyCreatesNullFilterTest() {
        var documentoCriteria = new DocumentoCriteria();
        var copy = documentoCriteria.copy();

        assertThat(documentoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentoCriteria)
        );
    }

    @Test
    void documentoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentoCriteria = new DocumentoCriteria();
        setAllFilters(documentoCriteria);

        var copy = documentoCriteria.copy();

        assertThat(documentoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentoCriteria = new DocumentoCriteria();

        assertThat(documentoCriteria).hasToString("DocumentoCriteria{}");
    }

    private static void setAllFilters(DocumentoCriteria documentoCriteria) {
        documentoCriteria.id();
        documentoCriteria.noCia();
        documentoCriteria.tipo();
        documentoCriteria.observacion();
        documentoCriteria.fechaCreacion();
        documentoCriteria.fechaVencimiento();
        documentoCriteria.path();
        documentoCriteria.clienteId();
        documentoCriteria.eventoId();
        documentoCriteria.distinct();
    }

    private static Condition<DocumentoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNoCia()) &&
                condition.apply(criteria.getTipo()) &&
                condition.apply(criteria.getObservacion()) &&
                condition.apply(criteria.getFechaCreacion()) &&
                condition.apply(criteria.getFechaVencimiento()) &&
                condition.apply(criteria.getPath()) &&
                condition.apply(criteria.getClienteId()) &&
                condition.apply(criteria.getEventoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentoCriteria> copyFiltersAre(DocumentoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNoCia(), copy.getNoCia()) &&
                condition.apply(criteria.getTipo(), copy.getTipo()) &&
                condition.apply(criteria.getObservacion(), copy.getObservacion()) &&
                condition.apply(criteria.getFechaCreacion(), copy.getFechaCreacion()) &&
                condition.apply(criteria.getFechaVencimiento(), copy.getFechaVencimiento()) &&
                condition.apply(criteria.getPath(), copy.getPath()) &&
                condition.apply(criteria.getClienteId(), copy.getClienteId()) &&
                condition.apply(criteria.getEventoId(), copy.getEventoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
