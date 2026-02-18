package com.devix.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Cliente getClienteSample1() {
        return new Cliente()
            .id(1L)
            .noCia(1L)
            .dni("dni1")
            .nombres("nombres1")
            .apellidos("apellidos1")
            .nombreComercial("nombreComercial1")
            .email("email1")
            .telefono("telefono1")
            .sexo("sexo1")
            .estadoCivil("estadoCivil1")
            .tipoSangre("tipoSangre1")
            .pathImagen("pathImagen1");
    }

    public static Cliente getClienteSample2() {
        return new Cliente()
            .id(2L)
            .noCia(2L)
            .dni("dni2")
            .nombres("nombres2")
            .apellidos("apellidos2")
            .nombreComercial("nombreComercial2")
            .email("email2")
            .telefono("telefono2")
            .sexo("sexo2")
            .estadoCivil("estadoCivil2")
            .tipoSangre("tipoSangre2")
            .pathImagen("pathImagen2");
    }

    public static Cliente getClienteRandomSampleGenerator() {
        return new Cliente()
            .id(longCount.incrementAndGet())
            .noCia(longCount.incrementAndGet())
            .dni(UUID.randomUUID().toString())
            .nombres(UUID.randomUUID().toString())
            .apellidos(UUID.randomUUID().toString())
            .nombreComercial(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString())
            .sexo(UUID.randomUUID().toString())
            .estadoCivil(UUID.randomUUID().toString())
            .tipoSangre(UUID.randomUUID().toString())
            .pathImagen(UUID.randomUUID().toString());
    }
}
