package com.devix.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Documento getDocumentoSample1() {
        return new Documento().id(1L).noCia(1L).tipo("tipo1").observacion("observacion1").path("path1");
    }

    public static Documento getDocumentoSample2() {
        return new Documento().id(2L).noCia(2L).tipo("tipo2").observacion("observacion2").path("path2");
    }

    public static Documento getDocumentoRandomSampleGenerator() {
        return new Documento()
            .id(longCount.incrementAndGet())
            .noCia(longCount.incrementAndGet())
            .tipo(UUID.randomUUID().toString())
            .observacion(UUID.randomUUID().toString())
            .path(UUID.randomUUID().toString());
    }
}
