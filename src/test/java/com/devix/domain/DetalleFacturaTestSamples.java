package com.devix.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DetalleFacturaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DetalleFactura getDetalleFacturaSample1() {
        return new DetalleFactura().id(1L).noCia(1L).cantidad(1);
    }

    public static DetalleFactura getDetalleFacturaSample2() {
        return new DetalleFactura().id(2L).noCia(2L).cantidad(2);
    }

    public static DetalleFactura getDetalleFacturaRandomSampleGenerator() {
        return new DetalleFactura().id(longCount.incrementAndGet()).noCia(longCount.incrementAndGet()).cantidad(intCount.incrementAndGet());
    }
}
