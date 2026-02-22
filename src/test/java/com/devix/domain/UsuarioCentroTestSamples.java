package com.devix.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class UsuarioCentroTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UsuarioCentro getUsuarioCentroSample1() {
        return new UsuarioCentro().id(1L).noCia(1L);
    }

    public static UsuarioCentro getUsuarioCentroSample2() {
        return new UsuarioCentro().id(2L).noCia(2L);
    }

    public static UsuarioCentro getUsuarioCentroRandomSampleGenerator() {
        return new UsuarioCentro().id(longCount.incrementAndGet()).noCia(longCount.incrementAndGet());
    }
}
