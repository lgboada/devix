package com.devix.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TipoProductoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TipoProducto getTipoProductoSample1() {
        return new TipoProducto().id(1L).noCia(1L).nombre("nombre1");
    }

    public static TipoProducto getTipoProductoSample2() {
        return new TipoProducto().id(2L).noCia(2L).nombre("nombre2");
    }

    public static TipoProducto getTipoProductoRandomSampleGenerator() {
        return new TipoProducto().id(longCount.incrementAndGet()).noCia(longCount.incrementAndGet()).nombre(UUID.randomUUID().toString());
    }
}
