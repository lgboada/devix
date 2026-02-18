package com.devix.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MarcaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Marca getMarcaSample1() {
        return new Marca().id(1L).noCia(1L).nombre("nombre1").pathImagen("pathImagen1");
    }

    public static Marca getMarcaSample2() {
        return new Marca().id(2L).noCia(2L).nombre("nombre2").pathImagen("pathImagen2");
    }

    public static Marca getMarcaRandomSampleGenerator() {
        return new Marca()
            .id(longCount.incrementAndGet())
            .noCia(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .pathImagen(UUID.randomUUID().toString());
    }
}
