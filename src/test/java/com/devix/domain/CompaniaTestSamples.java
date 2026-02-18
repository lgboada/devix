package com.devix.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CompaniaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Compania getCompaniaSample1() {
        return new Compania()
            .id(1L)
            .noCia(1L)
            .dni("dni1")
            .nombre("nombre1")
            .direccion("direccion1")
            .email("email1")
            .telefono("telefono1")
            .pathImage("pathImage1");
    }

    public static Compania getCompaniaSample2() {
        return new Compania()
            .id(2L)
            .noCia(2L)
            .dni("dni2")
            .nombre("nombre2")
            .direccion("direccion2")
            .email("email2")
            .telefono("telefono2")
            .pathImage("pathImage2");
    }

    public static Compania getCompaniaRandomSampleGenerator() {
        return new Compania()
            .id(longCount.incrementAndGet())
            .noCia(longCount.incrementAndGet())
            .dni(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString())
            .pathImage(UUID.randomUUID().toString());
    }
}
