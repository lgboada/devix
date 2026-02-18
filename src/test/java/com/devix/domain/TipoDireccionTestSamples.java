package com.devix.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TipoDireccionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TipoDireccion getTipoDireccionSample1() {
        return new TipoDireccion().id(1L).noCia(1L).descripcion("descripcion1");
    }

    public static TipoDireccion getTipoDireccionSample2() {
        return new TipoDireccion().id(2L).noCia(2L).descripcion("descripcion2");
    }

    public static TipoDireccion getTipoDireccionRandomSampleGenerator() {
        return new TipoDireccion()
            .id(longCount.incrementAndGet())
            .noCia(longCount.incrementAndGet())
            .descripcion(UUID.randomUUID().toString());
    }
}
