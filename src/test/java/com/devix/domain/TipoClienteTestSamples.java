package com.devix.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TipoClienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TipoCliente getTipoClienteSample1() {
        return new TipoCliente().id(1L).noCia(1L).descripcion("descripcion1");
    }

    public static TipoCliente getTipoClienteSample2() {
        return new TipoCliente().id(2L).noCia(2L).descripcion("descripcion2");
    }

    public static TipoCliente getTipoClienteRandomSampleGenerator() {
        return new TipoCliente()
            .id(longCount.incrementAndGet())
            .noCia(longCount.incrementAndGet())
            .descripcion(UUID.randomUUID().toString());
    }
}
