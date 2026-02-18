package com.devix.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FacturaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Factura getFacturaSample1() {
        return new Factura()
            .id(1L)
            .noCia(1L)
            .serie("serie1")
            .noFisico("noFisico1")
            .cedula("cedula1")
            .direccion("direccion1")
            .email("email1")
            .estado("estado1");
    }

    public static Factura getFacturaSample2() {
        return new Factura()
            .id(2L)
            .noCia(2L)
            .serie("serie2")
            .noFisico("noFisico2")
            .cedula("cedula2")
            .direccion("direccion2")
            .email("email2")
            .estado("estado2");
    }

    public static Factura getFacturaRandomSampleGenerator() {
        return new Factura()
            .id(longCount.incrementAndGet())
            .noCia(longCount.incrementAndGet())
            .serie(UUID.randomUUID().toString())
            .noFisico(UUID.randomUUID().toString())
            .cedula(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .estado(UUID.randomUUID().toString());
    }
}
