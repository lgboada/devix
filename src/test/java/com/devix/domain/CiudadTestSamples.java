package com.devix.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CiudadTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Ciudad getCiudadSample1() {
        return new Ciudad().id(1L).noCia(1L).descripcion("descripcion1");
    }

    public static Ciudad getCiudadSample2() {
        return new Ciudad().id(2L).noCia(2L).descripcion("descripcion2");
    }

    public static Ciudad getCiudadRandomSampleGenerator() {
        return new Ciudad().id(longCount.incrementAndGet()).noCia(longCount.incrementAndGet()).descripcion(UUID.randomUUID().toString());
    }
}
