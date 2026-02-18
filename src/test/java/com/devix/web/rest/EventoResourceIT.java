package com.devix.web.rest;

import static com.devix.domain.EventoAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.Centro;
import com.devix.domain.Cliente;
import com.devix.domain.Evento;
import com.devix.domain.TipoEvento;
import com.devix.repository.EventoRepository;
import com.devix.service.dto.EventoDTO;
import com.devix.service.mapper.EventoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EventoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventoResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String DEFAULT_MOTIVO_CONSULTA = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO_CONSULTA = "BBBBBBBBBB";

    private static final String DEFAULT_TRATAMIENTO = "AAAAAAAAAA";
    private static final String UPDATED_TRATAMIENTO = "BBBBBBBBBB";

    private static final String DEFAULT_INDICACIONES = "AAAAAAAAAA";
    private static final String UPDATED_INDICACIONES = "BBBBBBBBBB";

    private static final String DEFAULT_DIAGNOSTICO_1 = "AAAAAAAAAA";
    private static final String UPDATED_DIAGNOSTICO_1 = "BBBBBBBBBB";

    private static final String DEFAULT_DIAGNOSTICO_2 = "AAAAAAAAAA";
    private static final String UPDATED_DIAGNOSTICO_2 = "BBBBBBBBBB";

    private static final String DEFAULT_DIAGNOSTICO_3 = "AAAAAAAAAA";
    private static final String UPDATED_DIAGNOSTICO_3 = "BBBBBBBBBB";

    private static final String DEFAULT_DIAGNOSTICO_4 = "AAAAAAAAAA";
    private static final String UPDATED_DIAGNOSTICO_4 = "BBBBBBBBBB";

    private static final String DEFAULT_DIAGNOSTICO_5 = "AAAAAAAAAA";
    private static final String UPDATED_DIAGNOSTICO_5 = "BBBBBBBBBB";

    private static final String DEFAULT_DIAGNOSTICO_6 = "AAAAAAAAAA";
    private static final String UPDATED_DIAGNOSTICO_6 = "BBBBBBBBBB";

    private static final String DEFAULT_DIAGNOSTICO_7 = "AAAAAAAAAA";
    private static final String UPDATED_DIAGNOSTICO_7 = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/eventos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private EventoMapper eventoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventoMockMvc;

    private Evento evento;

    private Evento insertedEvento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evento createEntity() {
        return new Evento()
            .noCia(DEFAULT_NO_CIA)
            .descripcion(DEFAULT_DESCRIPCION)
            .fecha(DEFAULT_FECHA)
            .estado(DEFAULT_ESTADO)
            .motivoConsulta(DEFAULT_MOTIVO_CONSULTA)
            .tratamiento(DEFAULT_TRATAMIENTO)
            .indicaciones(DEFAULT_INDICACIONES)
            .diagnostico1(DEFAULT_DIAGNOSTICO_1)
            .diagnostico2(DEFAULT_DIAGNOSTICO_2)
            .diagnostico3(DEFAULT_DIAGNOSTICO_3)
            .diagnostico4(DEFAULT_DIAGNOSTICO_4)
            .diagnostico5(DEFAULT_DIAGNOSTICO_5)
            .diagnostico6(DEFAULT_DIAGNOSTICO_6)
            .diagnostico7(DEFAULT_DIAGNOSTICO_7)
            .observacion(DEFAULT_OBSERVACION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evento createUpdatedEntity() {
        return new Evento()
            .noCia(UPDATED_NO_CIA)
            .descripcion(UPDATED_DESCRIPCION)
            .fecha(UPDATED_FECHA)
            .estado(UPDATED_ESTADO)
            .motivoConsulta(UPDATED_MOTIVO_CONSULTA)
            .tratamiento(UPDATED_TRATAMIENTO)
            .indicaciones(UPDATED_INDICACIONES)
            .diagnostico1(UPDATED_DIAGNOSTICO_1)
            .diagnostico2(UPDATED_DIAGNOSTICO_2)
            .diagnostico3(UPDATED_DIAGNOSTICO_3)
            .diagnostico4(UPDATED_DIAGNOSTICO_4)
            .diagnostico5(UPDATED_DIAGNOSTICO_5)
            .diagnostico6(UPDATED_DIAGNOSTICO_6)
            .diagnostico7(UPDATED_DIAGNOSTICO_7)
            .observacion(UPDATED_OBSERVACION);
    }

    @BeforeEach
    void initTest() {
        evento = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEvento != null) {
            eventoRepository.delete(insertedEvento);
            insertedEvento = null;
        }
    }

    @Test
    @Transactional
    void createEvento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Evento
        EventoDTO eventoDTO = eventoMapper.toDto(evento);
        var returnedEventoDTO = om.readValue(
            restEventoMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EventoDTO.class
        );

        // Validate the Evento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEvento = eventoMapper.toEntity(returnedEventoDTO);
        assertEventoUpdatableFieldsEquals(returnedEvento, getPersistedEvento(returnedEvento));

        insertedEvento = returnedEvento;
    }

    @Test
    @Transactional
    void createEventoWithExistingId() throws Exception {
        // Create the Evento with an existing ID
        evento.setId(1L);
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Evento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evento.setNoCia(null);

        // Create the Evento, which fails.
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        restEventoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evento.setDescripcion(null);

        // Create the Evento, which fails.
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        restEventoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evento.setFecha(null);

        // Create the Evento, which fails.
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        restEventoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventos() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList
        restEventoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evento.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].motivoConsulta").value(hasItem(DEFAULT_MOTIVO_CONSULTA)))
            .andExpect(jsonPath("$.[*].tratamiento").value(hasItem(DEFAULT_TRATAMIENTO)))
            .andExpect(jsonPath("$.[*].indicaciones").value(hasItem(DEFAULT_INDICACIONES)))
            .andExpect(jsonPath("$.[*].diagnostico1").value(hasItem(DEFAULT_DIAGNOSTICO_1)))
            .andExpect(jsonPath("$.[*].diagnostico2").value(hasItem(DEFAULT_DIAGNOSTICO_2)))
            .andExpect(jsonPath("$.[*].diagnostico3").value(hasItem(DEFAULT_DIAGNOSTICO_3)))
            .andExpect(jsonPath("$.[*].diagnostico4").value(hasItem(DEFAULT_DIAGNOSTICO_4)))
            .andExpect(jsonPath("$.[*].diagnostico5").value(hasItem(DEFAULT_DIAGNOSTICO_5)))
            .andExpect(jsonPath("$.[*].diagnostico6").value(hasItem(DEFAULT_DIAGNOSTICO_6)))
            .andExpect(jsonPath("$.[*].diagnostico7").value(hasItem(DEFAULT_DIAGNOSTICO_7)))
            .andExpect(jsonPath("$.[*].observacion").value(hasItem(DEFAULT_OBSERVACION)));
    }

    @Test
    @Transactional
    void getEvento() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get the evento
        restEventoMockMvc
            .perform(get(ENTITY_API_URL_ID, evento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evento.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.motivoConsulta").value(DEFAULT_MOTIVO_CONSULTA))
            .andExpect(jsonPath("$.tratamiento").value(DEFAULT_TRATAMIENTO))
            .andExpect(jsonPath("$.indicaciones").value(DEFAULT_INDICACIONES))
            .andExpect(jsonPath("$.diagnostico1").value(DEFAULT_DIAGNOSTICO_1))
            .andExpect(jsonPath("$.diagnostico2").value(DEFAULT_DIAGNOSTICO_2))
            .andExpect(jsonPath("$.diagnostico3").value(DEFAULT_DIAGNOSTICO_3))
            .andExpect(jsonPath("$.diagnostico4").value(DEFAULT_DIAGNOSTICO_4))
            .andExpect(jsonPath("$.diagnostico5").value(DEFAULT_DIAGNOSTICO_5))
            .andExpect(jsonPath("$.diagnostico6").value(DEFAULT_DIAGNOSTICO_6))
            .andExpect(jsonPath("$.diagnostico7").value(DEFAULT_DIAGNOSTICO_7))
            .andExpect(jsonPath("$.observacion").value(DEFAULT_OBSERVACION));
    }

    @Test
    @Transactional
    void getEventosByIdFiltering() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        Long id = evento.getId();

        defaultEventoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEventoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEventoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventosByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where noCia equals to
        defaultEventoFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllEventosByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where noCia in
        defaultEventoFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllEventosByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where noCia is not null
        defaultEventoFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllEventosByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where noCia is greater than or equal to
        defaultEventoFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllEventosByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where noCia is less than or equal to
        defaultEventoFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllEventosByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where noCia is less than
        defaultEventoFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllEventosByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where noCia is greater than
        defaultEventoFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllEventosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where descripcion equals to
        defaultEventoFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllEventosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where descripcion in
        defaultEventoFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllEventosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where descripcion is not null
        defaultEventoFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllEventosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where descripcion contains
        defaultEventoFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllEventosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where descripcion does not contain
        defaultEventoFiltering("descripcion.doesNotContain=" + UPDATED_DESCRIPCION, "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllEventosByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where fecha equals to
        defaultEventoFiltering("fecha.equals=" + DEFAULT_FECHA, "fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllEventosByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where fecha in
        defaultEventoFiltering("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA, "fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllEventosByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where fecha is not null
        defaultEventoFiltering("fecha.specified=true", "fecha.specified=false");
    }

    @Test
    @Transactional
    void getAllEventosByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where estado equals to
        defaultEventoFiltering("estado.equals=" + DEFAULT_ESTADO, "estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllEventosByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where estado in
        defaultEventoFiltering("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO, "estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllEventosByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where estado is not null
        defaultEventoFiltering("estado.specified=true", "estado.specified=false");
    }

    @Test
    @Transactional
    void getAllEventosByEstadoContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where estado contains
        defaultEventoFiltering("estado.contains=" + DEFAULT_ESTADO, "estado.contains=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllEventosByEstadoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where estado does not contain
        defaultEventoFiltering("estado.doesNotContain=" + UPDATED_ESTADO, "estado.doesNotContain=" + DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void getAllEventosByMotivoConsultaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where motivoConsulta equals to
        defaultEventoFiltering("motivoConsulta.equals=" + DEFAULT_MOTIVO_CONSULTA, "motivoConsulta.equals=" + UPDATED_MOTIVO_CONSULTA);
    }

    @Test
    @Transactional
    void getAllEventosByMotivoConsultaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where motivoConsulta in
        defaultEventoFiltering(
            "motivoConsulta.in=" + DEFAULT_MOTIVO_CONSULTA + "," + UPDATED_MOTIVO_CONSULTA,
            "motivoConsulta.in=" + UPDATED_MOTIVO_CONSULTA
        );
    }

    @Test
    @Transactional
    void getAllEventosByMotivoConsultaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where motivoConsulta is not null
        defaultEventoFiltering("motivoConsulta.specified=true", "motivoConsulta.specified=false");
    }

    @Test
    @Transactional
    void getAllEventosByMotivoConsultaContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where motivoConsulta contains
        defaultEventoFiltering("motivoConsulta.contains=" + DEFAULT_MOTIVO_CONSULTA, "motivoConsulta.contains=" + UPDATED_MOTIVO_CONSULTA);
    }

    @Test
    @Transactional
    void getAllEventosByMotivoConsultaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where motivoConsulta does not contain
        defaultEventoFiltering(
            "motivoConsulta.doesNotContain=" + UPDATED_MOTIVO_CONSULTA,
            "motivoConsulta.doesNotContain=" + DEFAULT_MOTIVO_CONSULTA
        );
    }

    @Test
    @Transactional
    void getAllEventosByTratamientoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where tratamiento equals to
        defaultEventoFiltering("tratamiento.equals=" + DEFAULT_TRATAMIENTO, "tratamiento.equals=" + UPDATED_TRATAMIENTO);
    }

    @Test
    @Transactional
    void getAllEventosByTratamientoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where tratamiento in
        defaultEventoFiltering(
            "tratamiento.in=" + DEFAULT_TRATAMIENTO + "," + UPDATED_TRATAMIENTO,
            "tratamiento.in=" + UPDATED_TRATAMIENTO
        );
    }

    @Test
    @Transactional
    void getAllEventosByTratamientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where tratamiento is not null
        defaultEventoFiltering("tratamiento.specified=true", "tratamiento.specified=false");
    }

    @Test
    @Transactional
    void getAllEventosByTratamientoContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where tratamiento contains
        defaultEventoFiltering("tratamiento.contains=" + DEFAULT_TRATAMIENTO, "tratamiento.contains=" + UPDATED_TRATAMIENTO);
    }

    @Test
    @Transactional
    void getAllEventosByTratamientoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where tratamiento does not contain
        defaultEventoFiltering("tratamiento.doesNotContain=" + UPDATED_TRATAMIENTO, "tratamiento.doesNotContain=" + DEFAULT_TRATAMIENTO);
    }

    @Test
    @Transactional
    void getAllEventosByIndicacionesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where indicaciones equals to
        defaultEventoFiltering("indicaciones.equals=" + DEFAULT_INDICACIONES, "indicaciones.equals=" + UPDATED_INDICACIONES);
    }

    @Test
    @Transactional
    void getAllEventosByIndicacionesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where indicaciones in
        defaultEventoFiltering(
            "indicaciones.in=" + DEFAULT_INDICACIONES + "," + UPDATED_INDICACIONES,
            "indicaciones.in=" + UPDATED_INDICACIONES
        );
    }

    @Test
    @Transactional
    void getAllEventosByIndicacionesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where indicaciones is not null
        defaultEventoFiltering("indicaciones.specified=true", "indicaciones.specified=false");
    }

    @Test
    @Transactional
    void getAllEventosByIndicacionesContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where indicaciones contains
        defaultEventoFiltering("indicaciones.contains=" + DEFAULT_INDICACIONES, "indicaciones.contains=" + UPDATED_INDICACIONES);
    }

    @Test
    @Transactional
    void getAllEventosByIndicacionesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where indicaciones does not contain
        defaultEventoFiltering(
            "indicaciones.doesNotContain=" + UPDATED_INDICACIONES,
            "indicaciones.doesNotContain=" + DEFAULT_INDICACIONES
        );
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico1IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico1 equals to
        defaultEventoFiltering("diagnostico1.equals=" + DEFAULT_DIAGNOSTICO_1, "diagnostico1.equals=" + UPDATED_DIAGNOSTICO_1);
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico1IsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico1 in
        defaultEventoFiltering(
            "diagnostico1.in=" + DEFAULT_DIAGNOSTICO_1 + "," + UPDATED_DIAGNOSTICO_1,
            "diagnostico1.in=" + UPDATED_DIAGNOSTICO_1
        );
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico1IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico1 is not null
        defaultEventoFiltering("diagnostico1.specified=true", "diagnostico1.specified=false");
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico1ContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico1 contains
        defaultEventoFiltering("diagnostico1.contains=" + DEFAULT_DIAGNOSTICO_1, "diagnostico1.contains=" + UPDATED_DIAGNOSTICO_1);
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico1NotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico1 does not contain
        defaultEventoFiltering(
            "diagnostico1.doesNotContain=" + UPDATED_DIAGNOSTICO_1,
            "diagnostico1.doesNotContain=" + DEFAULT_DIAGNOSTICO_1
        );
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico2IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico2 equals to
        defaultEventoFiltering("diagnostico2.equals=" + DEFAULT_DIAGNOSTICO_2, "diagnostico2.equals=" + UPDATED_DIAGNOSTICO_2);
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico2IsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico2 in
        defaultEventoFiltering(
            "diagnostico2.in=" + DEFAULT_DIAGNOSTICO_2 + "," + UPDATED_DIAGNOSTICO_2,
            "diagnostico2.in=" + UPDATED_DIAGNOSTICO_2
        );
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico2IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico2 is not null
        defaultEventoFiltering("diagnostico2.specified=true", "diagnostico2.specified=false");
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico2ContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico2 contains
        defaultEventoFiltering("diagnostico2.contains=" + DEFAULT_DIAGNOSTICO_2, "diagnostico2.contains=" + UPDATED_DIAGNOSTICO_2);
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico2NotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico2 does not contain
        defaultEventoFiltering(
            "diagnostico2.doesNotContain=" + UPDATED_DIAGNOSTICO_2,
            "diagnostico2.doesNotContain=" + DEFAULT_DIAGNOSTICO_2
        );
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico3IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico3 equals to
        defaultEventoFiltering("diagnostico3.equals=" + DEFAULT_DIAGNOSTICO_3, "diagnostico3.equals=" + UPDATED_DIAGNOSTICO_3);
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico3IsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico3 in
        defaultEventoFiltering(
            "diagnostico3.in=" + DEFAULT_DIAGNOSTICO_3 + "," + UPDATED_DIAGNOSTICO_3,
            "diagnostico3.in=" + UPDATED_DIAGNOSTICO_3
        );
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico3IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico3 is not null
        defaultEventoFiltering("diagnostico3.specified=true", "diagnostico3.specified=false");
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico3ContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico3 contains
        defaultEventoFiltering("diagnostico3.contains=" + DEFAULT_DIAGNOSTICO_3, "diagnostico3.contains=" + UPDATED_DIAGNOSTICO_3);
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico3NotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico3 does not contain
        defaultEventoFiltering(
            "diagnostico3.doesNotContain=" + UPDATED_DIAGNOSTICO_3,
            "diagnostico3.doesNotContain=" + DEFAULT_DIAGNOSTICO_3
        );
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico4IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico4 equals to
        defaultEventoFiltering("diagnostico4.equals=" + DEFAULT_DIAGNOSTICO_4, "diagnostico4.equals=" + UPDATED_DIAGNOSTICO_4);
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico4IsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico4 in
        defaultEventoFiltering(
            "diagnostico4.in=" + DEFAULT_DIAGNOSTICO_4 + "," + UPDATED_DIAGNOSTICO_4,
            "diagnostico4.in=" + UPDATED_DIAGNOSTICO_4
        );
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico4IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico4 is not null
        defaultEventoFiltering("diagnostico4.specified=true", "diagnostico4.specified=false");
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico4ContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico4 contains
        defaultEventoFiltering("diagnostico4.contains=" + DEFAULT_DIAGNOSTICO_4, "diagnostico4.contains=" + UPDATED_DIAGNOSTICO_4);
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico4NotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico4 does not contain
        defaultEventoFiltering(
            "diagnostico4.doesNotContain=" + UPDATED_DIAGNOSTICO_4,
            "diagnostico4.doesNotContain=" + DEFAULT_DIAGNOSTICO_4
        );
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico5IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico5 equals to
        defaultEventoFiltering("diagnostico5.equals=" + DEFAULT_DIAGNOSTICO_5, "diagnostico5.equals=" + UPDATED_DIAGNOSTICO_5);
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico5IsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico5 in
        defaultEventoFiltering(
            "diagnostico5.in=" + DEFAULT_DIAGNOSTICO_5 + "," + UPDATED_DIAGNOSTICO_5,
            "diagnostico5.in=" + UPDATED_DIAGNOSTICO_5
        );
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico5IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico5 is not null
        defaultEventoFiltering("diagnostico5.specified=true", "diagnostico5.specified=false");
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico5ContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico5 contains
        defaultEventoFiltering("diagnostico5.contains=" + DEFAULT_DIAGNOSTICO_5, "diagnostico5.contains=" + UPDATED_DIAGNOSTICO_5);
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico5NotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico5 does not contain
        defaultEventoFiltering(
            "diagnostico5.doesNotContain=" + UPDATED_DIAGNOSTICO_5,
            "diagnostico5.doesNotContain=" + DEFAULT_DIAGNOSTICO_5
        );
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico6IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico6 equals to
        defaultEventoFiltering("diagnostico6.equals=" + DEFAULT_DIAGNOSTICO_6, "diagnostico6.equals=" + UPDATED_DIAGNOSTICO_6);
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico6IsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico6 in
        defaultEventoFiltering(
            "diagnostico6.in=" + DEFAULT_DIAGNOSTICO_6 + "," + UPDATED_DIAGNOSTICO_6,
            "diagnostico6.in=" + UPDATED_DIAGNOSTICO_6
        );
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico6IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico6 is not null
        defaultEventoFiltering("diagnostico6.specified=true", "diagnostico6.specified=false");
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico6ContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico6 contains
        defaultEventoFiltering("diagnostico6.contains=" + DEFAULT_DIAGNOSTICO_6, "diagnostico6.contains=" + UPDATED_DIAGNOSTICO_6);
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico6NotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico6 does not contain
        defaultEventoFiltering(
            "diagnostico6.doesNotContain=" + UPDATED_DIAGNOSTICO_6,
            "diagnostico6.doesNotContain=" + DEFAULT_DIAGNOSTICO_6
        );
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico7IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico7 equals to
        defaultEventoFiltering("diagnostico7.equals=" + DEFAULT_DIAGNOSTICO_7, "diagnostico7.equals=" + UPDATED_DIAGNOSTICO_7);
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico7IsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico7 in
        defaultEventoFiltering(
            "diagnostico7.in=" + DEFAULT_DIAGNOSTICO_7 + "," + UPDATED_DIAGNOSTICO_7,
            "diagnostico7.in=" + UPDATED_DIAGNOSTICO_7
        );
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico7IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico7 is not null
        defaultEventoFiltering("diagnostico7.specified=true", "diagnostico7.specified=false");
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico7ContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico7 contains
        defaultEventoFiltering("diagnostico7.contains=" + DEFAULT_DIAGNOSTICO_7, "diagnostico7.contains=" + UPDATED_DIAGNOSTICO_7);
    }

    @Test
    @Transactional
    void getAllEventosByDiagnostico7NotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where diagnostico7 does not contain
        defaultEventoFiltering(
            "diagnostico7.doesNotContain=" + UPDATED_DIAGNOSTICO_7,
            "diagnostico7.doesNotContain=" + DEFAULT_DIAGNOSTICO_7
        );
    }

    @Test
    @Transactional
    void getAllEventosByObservacionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where observacion equals to
        defaultEventoFiltering("observacion.equals=" + DEFAULT_OBSERVACION, "observacion.equals=" + UPDATED_OBSERVACION);
    }

    @Test
    @Transactional
    void getAllEventosByObservacionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where observacion in
        defaultEventoFiltering(
            "observacion.in=" + DEFAULT_OBSERVACION + "," + UPDATED_OBSERVACION,
            "observacion.in=" + UPDATED_OBSERVACION
        );
    }

    @Test
    @Transactional
    void getAllEventosByObservacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where observacion is not null
        defaultEventoFiltering("observacion.specified=true", "observacion.specified=false");
    }

    @Test
    @Transactional
    void getAllEventosByObservacionContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where observacion contains
        defaultEventoFiltering("observacion.contains=" + DEFAULT_OBSERVACION, "observacion.contains=" + UPDATED_OBSERVACION);
    }

    @Test
    @Transactional
    void getAllEventosByObservacionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where observacion does not contain
        defaultEventoFiltering("observacion.doesNotContain=" + UPDATED_OBSERVACION, "observacion.doesNotContain=" + DEFAULT_OBSERVACION);
    }

    @Test
    @Transactional
    void getAllEventosByTipoEventoIsEqualToSomething() throws Exception {
        TipoEvento tipoEvento;
        if (TestUtil.findAll(em, TipoEvento.class).isEmpty()) {
            eventoRepository.saveAndFlush(evento);
            tipoEvento = TipoEventoResourceIT.createEntity();
        } else {
            tipoEvento = TestUtil.findAll(em, TipoEvento.class).get(0);
        }
        em.persist(tipoEvento);
        em.flush();
        evento.setTipoEvento(tipoEvento);
        eventoRepository.saveAndFlush(evento);
        Long tipoEventoId = tipoEvento.getId();
        // Get all the eventoList where tipoEvento equals to tipoEventoId
        defaultEventoShouldBeFound("tipoEventoId.equals=" + tipoEventoId);

        // Get all the eventoList where tipoEvento equals to (tipoEventoId + 1)
        defaultEventoShouldNotBeFound("tipoEventoId.equals=" + (tipoEventoId + 1));
    }

    @Test
    @Transactional
    void getAllEventosByCentroIsEqualToSomething() throws Exception {
        Centro centro;
        if (TestUtil.findAll(em, Centro.class).isEmpty()) {
            eventoRepository.saveAndFlush(evento);
            centro = CentroResourceIT.createEntity();
        } else {
            centro = TestUtil.findAll(em, Centro.class).get(0);
        }
        em.persist(centro);
        em.flush();
        evento.setCentro(centro);
        eventoRepository.saveAndFlush(evento);
        Long centroId = centro.getId();
        // Get all the eventoList where centro equals to centroId
        defaultEventoShouldBeFound("centroId.equals=" + centroId);

        // Get all the eventoList where centro equals to (centroId + 1)
        defaultEventoShouldNotBeFound("centroId.equals=" + (centroId + 1));
    }

    @Test
    @Transactional
    void getAllEventosByClienteIsEqualToSomething() throws Exception {
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            eventoRepository.saveAndFlush(evento);
            cliente = ClienteResourceIT.createEntity();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        em.persist(cliente);
        em.flush();
        evento.setCliente(cliente);
        eventoRepository.saveAndFlush(evento);
        Long clienteId = cliente.getId();
        // Get all the eventoList where cliente equals to clienteId
        defaultEventoShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the eventoList where cliente equals to (clienteId + 1)
        defaultEventoShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    private void defaultEventoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEventoShouldBeFound(shouldBeFound);
        defaultEventoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventoShouldBeFound(String filter) throws Exception {
        restEventoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evento.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].motivoConsulta").value(hasItem(DEFAULT_MOTIVO_CONSULTA)))
            .andExpect(jsonPath("$.[*].tratamiento").value(hasItem(DEFAULT_TRATAMIENTO)))
            .andExpect(jsonPath("$.[*].indicaciones").value(hasItem(DEFAULT_INDICACIONES)))
            .andExpect(jsonPath("$.[*].diagnostico1").value(hasItem(DEFAULT_DIAGNOSTICO_1)))
            .andExpect(jsonPath("$.[*].diagnostico2").value(hasItem(DEFAULT_DIAGNOSTICO_2)))
            .andExpect(jsonPath("$.[*].diagnostico3").value(hasItem(DEFAULT_DIAGNOSTICO_3)))
            .andExpect(jsonPath("$.[*].diagnostico4").value(hasItem(DEFAULT_DIAGNOSTICO_4)))
            .andExpect(jsonPath("$.[*].diagnostico5").value(hasItem(DEFAULT_DIAGNOSTICO_5)))
            .andExpect(jsonPath("$.[*].diagnostico6").value(hasItem(DEFAULT_DIAGNOSTICO_6)))
            .andExpect(jsonPath("$.[*].diagnostico7").value(hasItem(DEFAULT_DIAGNOSTICO_7)))
            .andExpect(jsonPath("$.[*].observacion").value(hasItem(DEFAULT_OBSERVACION)));

        // Check, that the count call also returns 1
        restEventoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventoShouldNotBeFound(String filter) throws Exception {
        restEventoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEvento() throws Exception {
        // Get the evento
        restEventoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEvento() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evento
        Evento updatedEvento = eventoRepository.findById(evento.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEvento are not directly saved in db
        em.detach(updatedEvento);
        updatedEvento
            .noCia(UPDATED_NO_CIA)
            .descripcion(UPDATED_DESCRIPCION)
            .fecha(UPDATED_FECHA)
            .estado(UPDATED_ESTADO)
            .motivoConsulta(UPDATED_MOTIVO_CONSULTA)
            .tratamiento(UPDATED_TRATAMIENTO)
            .indicaciones(UPDATED_INDICACIONES)
            .diagnostico1(UPDATED_DIAGNOSTICO_1)
            .diagnostico2(UPDATED_DIAGNOSTICO_2)
            .diagnostico3(UPDATED_DIAGNOSTICO_3)
            .diagnostico4(UPDATED_DIAGNOSTICO_4)
            .diagnostico5(UPDATED_DIAGNOSTICO_5)
            .diagnostico6(UPDATED_DIAGNOSTICO_6)
            .diagnostico7(UPDATED_DIAGNOSTICO_7)
            .observacion(UPDATED_OBSERVACION);
        EventoDTO eventoDTO = eventoMapper.toDto(updatedEvento);

        restEventoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Evento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEventoToMatchAllProperties(updatedEvento);
    }

    @Test
    @Transactional
    void putNonExistingEvento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evento.setId(longCount.incrementAndGet());

        // Create the Evento
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evento.setId(longCount.incrementAndGet());

        // Create the Evento
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evento.setId(longCount.incrementAndGet());

        // Create the Evento
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventoMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventoWithPatch() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evento using partial update
        Evento partialUpdatedEvento = new Evento();
        partialUpdatedEvento.setId(evento.getId());

        partialUpdatedEvento
            .descripcion(UPDATED_DESCRIPCION)
            .estado(UPDATED_ESTADO)
            .motivoConsulta(UPDATED_MOTIVO_CONSULTA)
            .tratamiento(UPDATED_TRATAMIENTO)
            .indicaciones(UPDATED_INDICACIONES)
            .diagnostico2(UPDATED_DIAGNOSTICO_2)
            .diagnostico5(UPDATED_DIAGNOSTICO_5)
            .diagnostico7(UPDATED_DIAGNOSTICO_7);

        restEventoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvento.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEvento))
            )
            .andExpect(status().isOk());

        // Validate the Evento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEventoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEvento, evento), getPersistedEvento(evento));
    }

    @Test
    @Transactional
    void fullUpdateEventoWithPatch() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evento using partial update
        Evento partialUpdatedEvento = new Evento();
        partialUpdatedEvento.setId(evento.getId());

        partialUpdatedEvento
            .noCia(UPDATED_NO_CIA)
            .descripcion(UPDATED_DESCRIPCION)
            .fecha(UPDATED_FECHA)
            .estado(UPDATED_ESTADO)
            .motivoConsulta(UPDATED_MOTIVO_CONSULTA)
            .tratamiento(UPDATED_TRATAMIENTO)
            .indicaciones(UPDATED_INDICACIONES)
            .diagnostico1(UPDATED_DIAGNOSTICO_1)
            .diagnostico2(UPDATED_DIAGNOSTICO_2)
            .diagnostico3(UPDATED_DIAGNOSTICO_3)
            .diagnostico4(UPDATED_DIAGNOSTICO_4)
            .diagnostico5(UPDATED_DIAGNOSTICO_5)
            .diagnostico6(UPDATED_DIAGNOSTICO_6)
            .diagnostico7(UPDATED_DIAGNOSTICO_7)
            .observacion(UPDATED_OBSERVACION);

        restEventoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvento.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEvento))
            )
            .andExpect(status().isOk());

        // Validate the Evento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEventoUpdatableFieldsEquals(partialUpdatedEvento, getPersistedEvento(partialUpdatedEvento));
    }

    @Test
    @Transactional
    void patchNonExistingEvento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evento.setId(longCount.incrementAndGet());

        // Create the Evento
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(eventoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evento.setId(longCount.incrementAndGet());

        // Create the Evento
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(eventoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evento.setId(longCount.incrementAndGet());

        // Create the Evento
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventoMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(eventoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvento() throws Exception {
        // Initialize the database
        insertedEvento = eventoRepository.saveAndFlush(evento);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the evento
        restEventoMockMvc
            .perform(delete(ENTITY_API_URL_ID, evento.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return eventoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Evento getPersistedEvento(Evento evento) {
        return eventoRepository.findById(evento.getId()).orElseThrow();
    }

    protected void assertPersistedEventoToMatchAllProperties(Evento expectedEvento) {
        assertEventoAllPropertiesEquals(expectedEvento, getPersistedEvento(expectedEvento));
    }

    protected void assertPersistedEventoToMatchUpdatableProperties(Evento expectedEvento) {
        assertEventoAllUpdatablePropertiesEquals(expectedEvento, getPersistedEvento(expectedEvento));
    }
}
