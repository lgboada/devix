package com.devix.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Evento getEventoSample1() {
        return new Evento()
            .id(1L)
            .noCia(1L)
            .descripcion("descripcion1")
            .estado("estado1")
            .motivoConsulta("motivoConsulta1")
            .tratamiento("tratamiento1")
            .indicaciones("indicaciones1")
            .diagnostico1("diagnostico11")
            .diagnostico2("diagnostico21")
            .diagnostico3("diagnostico31")
            .diagnostico4("diagnostico41")
            .diagnostico5("diagnostico51")
            .diagnostico6("diagnostico61")
            .diagnostico7("diagnostico71")
            .observacion("observacion1");
    }

    public static Evento getEventoSample2() {
        return new Evento()
            .id(2L)
            .noCia(2L)
            .descripcion("descripcion2")
            .estado("estado2")
            .motivoConsulta("motivoConsulta2")
            .tratamiento("tratamiento2")
            .indicaciones("indicaciones2")
            .diagnostico1("diagnostico12")
            .diagnostico2("diagnostico22")
            .diagnostico3("diagnostico32")
            .diagnostico4("diagnostico42")
            .diagnostico5("diagnostico52")
            .diagnostico6("diagnostico62")
            .diagnostico7("diagnostico72")
            .observacion("observacion2");
    }

    public static Evento getEventoRandomSampleGenerator() {
        return new Evento()
            .id(longCount.incrementAndGet())
            .noCia(longCount.incrementAndGet())
            .descripcion(UUID.randomUUID().toString())
            .estado(UUID.randomUUID().toString())
            .motivoConsulta(UUID.randomUUID().toString())
            .tratamiento(UUID.randomUUID().toString())
            .indicaciones(UUID.randomUUID().toString())
            .diagnostico1(UUID.randomUUID().toString())
            .diagnostico2(UUID.randomUUID().toString())
            .diagnostico3(UUID.randomUUID().toString())
            .diagnostico4(UUID.randomUUID().toString())
            .diagnostico5(UUID.randomUUID().toString())
            .diagnostico6(UUID.randomUUID().toString())
            .diagnostico7(UUID.randomUUID().toString())
            .observacion(UUID.randomUUID().toString());
    }
}
