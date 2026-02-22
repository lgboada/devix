package com.devix.web.rest;

import static com.devix.domain.DireccionAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.Cliente;
import com.devix.domain.Direccion;
import com.devix.domain.TipoDireccion;
import com.devix.repository.DireccionRepository;
import com.devix.service.dto.DireccionDTO;
import com.devix.service.mapper.DireccionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link DireccionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DireccionResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUD = 1D;
    private static final Double UPDATED_LATITUD = 2D;
    private static final Double SMALLER_LATITUD = 1D - 1D;

    private static final Double DEFAULT_LONGITUD = 1D;
    private static final Double UPDATED_LONGITUD = 2D;
    private static final Double SMALLER_LONGITUD = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/direccions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private DireccionMapper direccionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDireccionMockMvc;

    private Direccion direccion;

    private Direccion insertedDireccion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direccion createEntity() {
        return new Direccion()
            .noCia(DEFAULT_NO_CIA)
            .descripcion(DEFAULT_DESCRIPCION)
            .telefono(DEFAULT_TELEFONO)
            .latitud(DEFAULT_LATITUD)
            .longitud(DEFAULT_LONGITUD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direccion createUpdatedEntity() {
        return new Direccion()
            .noCia(UPDATED_NO_CIA)
            .descripcion(UPDATED_DESCRIPCION)
            .telefono(UPDATED_TELEFONO)
            .latitud(UPDATED_LATITUD)
            .longitud(UPDATED_LONGITUD);
    }

    @BeforeEach
    void initTest() {
        direccion = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDireccion != null) {
            direccionRepository.delete(insertedDireccion);
            insertedDireccion = null;
        }
    }

    @Test
    @Transactional
    void createDireccion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);
        var returnedDireccionDTO = om.readValue(
            restDireccionMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(direccionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DireccionDTO.class
        );

        // Validate the Direccion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDireccion = direccionMapper.toEntity(returnedDireccionDTO);
        assertDireccionUpdatableFieldsEquals(returnedDireccion, getPersistedDireccion(returnedDireccion));

        insertedDireccion = returnedDireccion;
    }

    @Test
    @Transactional
    void createDireccionWithExistingId() throws Exception {
        // Create the Direccion with an existing ID
        direccion.setId(1L);
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDireccionMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(direccionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        direccion.setNoCia(null);

        // Create the Direccion, which fails.
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        restDireccionMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(direccionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        direccion.setDescripcion(null);

        // Create the Direccion, which fails.
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        restDireccionMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(direccionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDireccions() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList
        restDireccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(direccion.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].latitud").value(hasItem(DEFAULT_LATITUD)))
            .andExpect(jsonPath("$.[*].longitud").value(hasItem(DEFAULT_LONGITUD)));
    }

    @Test
    @Transactional
    void getDireccion() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get the direccion
        restDireccionMockMvc
            .perform(get(ENTITY_API_URL_ID, direccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(direccion.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.latitud").value(DEFAULT_LATITUD))
            .andExpect(jsonPath("$.longitud").value(DEFAULT_LONGITUD));
    }

    @Test
    @Transactional
    void getDireccionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        Long id = direccion.getId();

        defaultDireccionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDireccionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDireccionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDireccionsByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where noCia equals to
        defaultDireccionFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllDireccionsByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where noCia in
        defaultDireccionFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllDireccionsByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where noCia is not null
        defaultDireccionFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllDireccionsByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where noCia is greater than or equal to
        defaultDireccionFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllDireccionsByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where noCia is less than or equal to
        defaultDireccionFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllDireccionsByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where noCia is less than
        defaultDireccionFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllDireccionsByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where noCia is greater than
        defaultDireccionFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllDireccionsByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where descripcion equals to
        defaultDireccionFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllDireccionsByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where descripcion in
        defaultDireccionFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllDireccionsByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where descripcion is not null
        defaultDireccionFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllDireccionsByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where descripcion contains
        defaultDireccionFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllDireccionsByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where descripcion does not contain
        defaultDireccionFiltering("descripcion.doesNotContain=" + UPDATED_DESCRIPCION, "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllDireccionsByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where telefono equals to
        defaultDireccionFiltering("telefono.equals=" + DEFAULT_TELEFONO, "telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllDireccionsByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where telefono in
        defaultDireccionFiltering("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO, "telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllDireccionsByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where telefono is not null
        defaultDireccionFiltering("telefono.specified=true", "telefono.specified=false");
    }

    @Test
    @Transactional
    void getAllDireccionsByTelefonoContainsSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where telefono contains
        defaultDireccionFiltering("telefono.contains=" + DEFAULT_TELEFONO, "telefono.contains=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllDireccionsByTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where telefono does not contain
        defaultDireccionFiltering("telefono.doesNotContain=" + UPDATED_TELEFONO, "telefono.doesNotContain=" + DEFAULT_TELEFONO);
    }

    @Test
    @Transactional
    void getAllDireccionsByLatitudIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where latitud equals to
        defaultDireccionFiltering("latitud.equals=" + DEFAULT_LATITUD, "latitud.equals=" + UPDATED_LATITUD);
    }

    @Test
    @Transactional
    void getAllDireccionsByLatitudIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where latitud in
        defaultDireccionFiltering("latitud.in=" + DEFAULT_LATITUD + "," + UPDATED_LATITUD, "latitud.in=" + UPDATED_LATITUD);
    }

    @Test
    @Transactional
    void getAllDireccionsByLatitudIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where latitud is not null
        defaultDireccionFiltering("latitud.specified=true", "latitud.specified=false");
    }

    @Test
    @Transactional
    void getAllDireccionsByLatitudIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where latitud is greater than or equal to
        defaultDireccionFiltering("latitud.greaterThanOrEqual=" + DEFAULT_LATITUD, "latitud.greaterThanOrEqual=" + UPDATED_LATITUD);
    }

    @Test
    @Transactional
    void getAllDireccionsByLatitudIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where latitud is less than or equal to
        defaultDireccionFiltering("latitud.lessThanOrEqual=" + DEFAULT_LATITUD, "latitud.lessThanOrEqual=" + SMALLER_LATITUD);
    }

    @Test
    @Transactional
    void getAllDireccionsByLatitudIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where latitud is less than
        defaultDireccionFiltering("latitud.lessThan=" + UPDATED_LATITUD, "latitud.lessThan=" + DEFAULT_LATITUD);
    }

    @Test
    @Transactional
    void getAllDireccionsByLatitudIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where latitud is greater than
        defaultDireccionFiltering("latitud.greaterThan=" + SMALLER_LATITUD, "latitud.greaterThan=" + DEFAULT_LATITUD);
    }

    @Test
    @Transactional
    void getAllDireccionsByLongitudIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where longitud equals to
        defaultDireccionFiltering("longitud.equals=" + DEFAULT_LONGITUD, "longitud.equals=" + UPDATED_LONGITUD);
    }

    @Test
    @Transactional
    void getAllDireccionsByLongitudIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where longitud in
        defaultDireccionFiltering("longitud.in=" + DEFAULT_LONGITUD + "," + UPDATED_LONGITUD, "longitud.in=" + UPDATED_LONGITUD);
    }

    @Test
    @Transactional
    void getAllDireccionsByLongitudIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where longitud is not null
        defaultDireccionFiltering("longitud.specified=true", "longitud.specified=false");
    }

    @Test
    @Transactional
    void getAllDireccionsByLongitudIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where longitud is greater than or equal to
        defaultDireccionFiltering("longitud.greaterThanOrEqual=" + DEFAULT_LONGITUD, "longitud.greaterThanOrEqual=" + UPDATED_LONGITUD);
    }

    @Test
    @Transactional
    void getAllDireccionsByLongitudIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where longitud is less than or equal to
        defaultDireccionFiltering("longitud.lessThanOrEqual=" + DEFAULT_LONGITUD, "longitud.lessThanOrEqual=" + SMALLER_LONGITUD);
    }

    @Test
    @Transactional
    void getAllDireccionsByLongitudIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where longitud is less than
        defaultDireccionFiltering("longitud.lessThan=" + UPDATED_LONGITUD, "longitud.lessThan=" + DEFAULT_LONGITUD);
    }

    @Test
    @Transactional
    void getAllDireccionsByLongitudIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where longitud is greater than
        defaultDireccionFiltering("longitud.greaterThan=" + SMALLER_LONGITUD, "longitud.greaterThan=" + DEFAULT_LONGITUD);
    }

    @Test
    @Transactional
    void getAllDireccionsByTipoDireccionIsEqualToSomething() throws Exception {
        TipoDireccion tipoDireccion;
        if (TestUtil.findAll(em, TipoDireccion.class).isEmpty()) {
            direccionRepository.saveAndFlush(direccion);
            tipoDireccion = TipoDireccionResourceIT.createEntity();
        } else {
            tipoDireccion = TestUtil.findAll(em, TipoDireccion.class).get(0);
        }
        em.persist(tipoDireccion);
        em.flush();
        direccion.setTipoDireccion(tipoDireccion);
        direccionRepository.saveAndFlush(direccion);
        Long tipoDireccionId = tipoDireccion.getId();
        // Get all the direccionList where tipoDireccion equals to tipoDireccionId
        defaultDireccionShouldBeFound("tipoDireccionId.equals=" + tipoDireccionId);

        // Get all the direccionList where tipoDireccion equals to (tipoDireccionId + 1)
        defaultDireccionShouldNotBeFound("tipoDireccionId.equals=" + (tipoDireccionId + 1));
    }

    @Test
    @Transactional
    void getAllDireccionsByClienteIsEqualToSomething() throws Exception {
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            direccionRepository.saveAndFlush(direccion);
            cliente = ClienteResourceIT.createEntity();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        em.persist(cliente);
        em.flush();
        direccion.setCliente(cliente);
        direccionRepository.saveAndFlush(direccion);
        Long clienteId = cliente.getId();
        // Get all the direccionList where cliente equals to clienteId
        defaultDireccionShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the direccionList where cliente equals to (clienteId + 1)
        defaultDireccionShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    private void defaultDireccionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDireccionShouldBeFound(shouldBeFound);
        defaultDireccionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDireccionShouldBeFound(String filter) throws Exception {
        restDireccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(direccion.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].latitud").value(hasItem(DEFAULT_LATITUD)))
            .andExpect(jsonPath("$.[*].longitud").value(hasItem(DEFAULT_LONGITUD)));

        // Check, that the count call also returns 1
        restDireccionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDireccionShouldNotBeFound(String filter) throws Exception {
        restDireccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDireccionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDireccion() throws Exception {
        // Get the direccion
        restDireccionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDireccion() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the direccion
        Direccion updatedDireccion = direccionRepository.findById(direccion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDireccion are not directly saved in db
        em.detach(updatedDireccion);
        updatedDireccion
            .noCia(UPDATED_NO_CIA)
            .descripcion(UPDATED_DESCRIPCION)
            .telefono(UPDATED_TELEFONO)
            .latitud(UPDATED_LATITUD)
            .longitud(UPDATED_LONGITUD);
        DireccionDTO direccionDTO = direccionMapper.toDto(updatedDireccion);

        restDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, direccionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(direccionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDireccionToMatchAllProperties(updatedDireccion);
    }

    @Test
    @Transactional
    void putNonExistingDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, direccionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(direccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(direccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(direccionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDireccionWithPatch() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the direccion using partial update
        Direccion partialUpdatedDireccion = new Direccion();
        partialUpdatedDireccion.setId(direccion.getId());

        partialUpdatedDireccion.noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION).longitud(UPDATED_LONGITUD);

        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDireccion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDireccion))
            )
            .andExpect(status().isOk());

        // Validate the Direccion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDireccionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDireccion, direccion),
            getPersistedDireccion(direccion)
        );
    }

    @Test
    @Transactional
    void fullUpdateDireccionWithPatch() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the direccion using partial update
        Direccion partialUpdatedDireccion = new Direccion();
        partialUpdatedDireccion.setId(direccion.getId());

        partialUpdatedDireccion
            .noCia(UPDATED_NO_CIA)
            .descripcion(UPDATED_DESCRIPCION)
            .telefono(UPDATED_TELEFONO)
            .latitud(UPDATED_LATITUD)
            .longitud(UPDATED_LONGITUD);

        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDireccion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDireccion))
            )
            .andExpect(status().isOk());

        // Validate the Direccion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDireccionUpdatableFieldsEquals(partialUpdatedDireccion, getPersistedDireccion(partialUpdatedDireccion));
    }

    @Test
    @Transactional
    void patchNonExistingDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, direccionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(direccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(direccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(direccionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDireccion() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the direccion
        restDireccionMockMvc
            .perform(delete(ENTITY_API_URL_ID, direccion.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return direccionRepository.count();
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

    protected Direccion getPersistedDireccion(Direccion direccion) {
        return direccionRepository.findById(direccion.getId()).orElseThrow();
    }

    protected void assertPersistedDireccionToMatchAllProperties(Direccion expectedDireccion) {
        assertDireccionAllPropertiesEquals(expectedDireccion, getPersistedDireccion(expectedDireccion));
    }

    protected void assertPersistedDireccionToMatchUpdatableProperties(Direccion expectedDireccion) {
        assertDireccionAllUpdatablePropertiesEquals(expectedDireccion, getPersistedDireccion(expectedDireccion));
    }
}
