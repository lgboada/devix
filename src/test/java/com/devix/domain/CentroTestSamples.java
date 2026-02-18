package com.devix.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CentroTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Centro getCentroSample1() {
        return new Centro().id(1L).noCia(1L).descripcion("descripcion1");
    }

    public static Centro getCentroSample2() {
        return new Centro().id(2L).noCia(2L).descripcion("descripcion2");
    }

    public static Centro getCentroRandomSampleGenerator() {
        return new Centro().id(longCount.incrementAndGet()).noCia(longCount.incrementAndGet()).descripcion(UUID.randomUUID().toString());
    }
}
