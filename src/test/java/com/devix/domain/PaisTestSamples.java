package com.devix.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaisTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Pais getPaisSample1() {
        return new Pais().id(1L).noCia(1L).descripcion("descripcion1");
    }

    public static Pais getPaisSample2() {
        return new Pais().id(2L).noCia(2L).descripcion("descripcion2");
    }

    public static Pais getPaisRandomSampleGenerator() {
        return new Pais().id(longCount.incrementAndGet()).noCia(longCount.incrementAndGet()).descripcion(UUID.randomUUID().toString());
    }
}
