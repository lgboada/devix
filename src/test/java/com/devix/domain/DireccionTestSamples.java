package com.devix.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DireccionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Direccion getDireccionSample1() {
        return new Direccion().id(1L).noCia(1L).descripcion("descripcion1").telefono("telefono1");
    }

    public static Direccion getDireccionSample2() {
        return new Direccion().id(2L).noCia(2L).descripcion("descripcion2").telefono("telefono2");
    }

    public static Direccion getDireccionRandomSampleGenerator() {
        return new Direccion()
            .id(longCount.incrementAndGet())
            .noCia(longCount.incrementAndGet())
            .descripcion(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString());
    }
}
