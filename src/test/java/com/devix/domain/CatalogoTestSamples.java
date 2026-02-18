package com.devix.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CatalogoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Catalogo getCatalogoSample1() {
        return new Catalogo()
            .id(1L)
            .noCia(1L)
            .descripcion1("descripcion11")
            .descripcion2("descripcion21")
            .estado("estado1")
            .orden(1)
            .texto1("texto11")
            .texto2("texto21");
    }

    public static Catalogo getCatalogoSample2() {
        return new Catalogo()
            .id(2L)
            .noCia(2L)
            .descripcion1("descripcion12")
            .descripcion2("descripcion22")
            .estado("estado2")
            .orden(2)
            .texto1("texto12")
            .texto2("texto22");
    }

    public static Catalogo getCatalogoRandomSampleGenerator() {
        return new Catalogo()
            .id(longCount.incrementAndGet())
            .noCia(longCount.incrementAndGet())
            .descripcion1(UUID.randomUUID().toString())
            .descripcion2(UUID.randomUUID().toString())
            .estado(UUID.randomUUID().toString())
            .orden(intCount.incrementAndGet())
            .texto1(UUID.randomUUID().toString())
            .texto2(UUID.randomUUID().toString());
    }
}
