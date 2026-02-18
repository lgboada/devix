package com.devix.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmpleadoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Empleado getEmpleadoSample1() {
        return new Empleado()
            .id(1L)
            .noCia(1L)
            .dni("dni1")
            .nombre("nombre1")
            .contacto("contacto1")
            .email("email1")
            .pathImagen("pathImagen1")
            .telefono("telefono1");
    }

    public static Empleado getEmpleadoSample2() {
        return new Empleado()
            .id(2L)
            .noCia(2L)
            .dni("dni2")
            .nombre("nombre2")
            .contacto("contacto2")
            .email("email2")
            .pathImagen("pathImagen2")
            .telefono("telefono2");
    }

    public static Empleado getEmpleadoRandomSampleGenerator() {
        return new Empleado()
            .id(longCount.incrementAndGet())
            .noCia(longCount.incrementAndGet())
            .dni(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .contacto(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .pathImagen(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString());
    }
}
