package com.devix.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TipoCatalogoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TipoCatalogo getTipoCatalogoSample1() {
        return new TipoCatalogo().id(1L).noCia(1L).descripcion("descripcion1").categoria("categoria1");
    }

    public static TipoCatalogo getTipoCatalogoSample2() {
        return new TipoCatalogo().id(2L).noCia(2L).descripcion("descripcion2").categoria("categoria2");
    }

    public static TipoCatalogo getTipoCatalogoRandomSampleGenerator() {
        return new TipoCatalogo()
            .id(longCount.incrementAndGet())
            .noCia(longCount.incrementAndGet())
            .descripcion(UUID.randomUUID().toString())
            .categoria(UUID.randomUUID().toString());
    }
}
