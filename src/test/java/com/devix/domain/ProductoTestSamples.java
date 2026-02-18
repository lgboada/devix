package com.devix.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Producto getProductoSample1() {
        return new Producto()
            .id(1L)
            .noCia(1L)
            .nombre("nombre1")
            .descripcion("descripcion1")
            .stock(1)
            .pathImagen("pathImagen1")
            .codigo("codigo1");
    }

    public static Producto getProductoSample2() {
        return new Producto()
            .id(2L)
            .noCia(2L)
            .nombre("nombre2")
            .descripcion("descripcion2")
            .stock(2)
            .pathImagen("pathImagen2")
            .codigo("codigo2");
    }

    public static Producto getProductoRandomSampleGenerator() {
        return new Producto()
            .id(longCount.incrementAndGet())
            .noCia(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .stock(intCount.incrementAndGet())
            .pathImagen(UUID.randomUUID().toString())
            .codigo(UUID.randomUUID().toString());
    }
}
