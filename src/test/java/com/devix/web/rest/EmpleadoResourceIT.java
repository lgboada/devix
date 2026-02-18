package com.devix.web.rest;

import static com.devix.domain.EmpleadoAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.Empleado;
import com.devix.repository.EmpleadoRepository;
import com.devix.service.dto.EmpleadoDTO;
import com.devix.service.mapper.EmpleadoMapper;
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
 * Integration tests for the {@link EmpleadoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmpleadoResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_DNI = "AAAAAAAAAA";
    private static final String UPDATED_DNI = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACTO = "AAAAAAAAAA";
    private static final String UPDATED_CONTACTO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "fr@esx7b.lrsy";
    private static final String UPDATED_EMAIL = "lyadbq@rrdm.wokw";

    private static final String DEFAULT_PATH_IMAGEN = "AAAAAAAAAA";
    private static final String UPDATED_PATH_IMAGEN = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/empleados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private EmpleadoMapper empleadoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmpleadoMockMvc;

    private Empleado empleado;

    private Empleado insertedEmpleado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empleado createEntity() {
        return new Empleado()
            .noCia(DEFAULT_NO_CIA)
            .dni(DEFAULT_DNI)
            .nombre(DEFAULT_NOMBRE)
            .contacto(DEFAULT_CONTACTO)
            .email(DEFAULT_EMAIL)
            .pathImagen(DEFAULT_PATH_IMAGEN)
            .telefono(DEFAULT_TELEFONO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empleado createUpdatedEntity() {
        return new Empleado()
            .noCia(UPDATED_NO_CIA)
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .contacto(UPDATED_CONTACTO)
            .email(UPDATED_EMAIL)
            .pathImagen(UPDATED_PATH_IMAGEN)
            .telefono(UPDATED_TELEFONO);
    }

    @BeforeEach
    void initTest() {
        empleado = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEmpleado != null) {
            empleadoRepository.delete(insertedEmpleado);
            insertedEmpleado = null;
        }
    }

    @Test
    @Transactional
    void createEmpleado() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);
        var returnedEmpleadoDTO = om.readValue(
            restEmpleadoMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmpleadoDTO.class
        );

        // Validate the Empleado in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEmpleado = empleadoMapper.toEntity(returnedEmpleadoDTO);
        assertEmpleadoUpdatableFieldsEquals(returnedEmpleado, getPersistedEmpleado(returnedEmpleado));

        insertedEmpleado = returnedEmpleado;
    }

    @Test
    @Transactional
    void createEmpleadoWithExistingId() throws Exception {
        // Create the Empleado with an existing ID
        empleado.setId(1L);
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        empleado.setNoCia(null);

        // Create the Empleado, which fails.
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        restEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDniIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        empleado.setDni(null);

        // Create the Empleado, which fails.
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        restEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        empleado.setNombre(null);

        // Create the Empleado, which fails.
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        restEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        empleado.setEmail(null);

        // Create the Empleado, which fails.
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        restEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPathImagenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        empleado.setPathImagen(null);

        // Create the Empleado, which fails.
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        restEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefonoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        empleado.setTelefono(null);

        // Create the Empleado, which fails.
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        restEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmpleados() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList
        restEmpleadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empleado.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].contacto").value(hasItem(DEFAULT_CONTACTO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].pathImagen").value(hasItem(DEFAULT_PATH_IMAGEN)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)));
    }

    @Test
    @Transactional
    void getEmpleado() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get the empleado
        restEmpleadoMockMvc
            .perform(get(ENTITY_API_URL_ID, empleado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(empleado.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.contacto").value(DEFAULT_CONTACTO))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.pathImagen").value(DEFAULT_PATH_IMAGEN))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO));
    }

    @Test
    @Transactional
    void getEmpleadosByIdFiltering() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        Long id = empleado.getId();

        defaultEmpleadoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEmpleadoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEmpleadoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmpleadosByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where noCia equals to
        defaultEmpleadoFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllEmpleadosByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where noCia in
        defaultEmpleadoFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllEmpleadosByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where noCia is not null
        defaultEmpleadoFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpleadosByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where noCia is greater than or equal to
        defaultEmpleadoFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllEmpleadosByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where noCia is less than or equal to
        defaultEmpleadoFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllEmpleadosByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where noCia is less than
        defaultEmpleadoFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllEmpleadosByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where noCia is greater than
        defaultEmpleadoFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllEmpleadosByDniIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where dni equals to
        defaultEmpleadoFiltering("dni.equals=" + DEFAULT_DNI, "dni.equals=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllEmpleadosByDniIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where dni in
        defaultEmpleadoFiltering("dni.in=" + DEFAULT_DNI + "," + UPDATED_DNI, "dni.in=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllEmpleadosByDniIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where dni is not null
        defaultEmpleadoFiltering("dni.specified=true", "dni.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpleadosByDniContainsSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where dni contains
        defaultEmpleadoFiltering("dni.contains=" + DEFAULT_DNI, "dni.contains=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllEmpleadosByDniNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where dni does not contain
        defaultEmpleadoFiltering("dni.doesNotContain=" + UPDATED_DNI, "dni.doesNotContain=" + DEFAULT_DNI);
    }

    @Test
    @Transactional
    void getAllEmpleadosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where nombre equals to
        defaultEmpleadoFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEmpleadosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where nombre in
        defaultEmpleadoFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEmpleadosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where nombre is not null
        defaultEmpleadoFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpleadosByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where nombre contains
        defaultEmpleadoFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEmpleadosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where nombre does not contain
        defaultEmpleadoFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEmpleadosByContactoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where contacto equals to
        defaultEmpleadoFiltering("contacto.equals=" + DEFAULT_CONTACTO, "contacto.equals=" + UPDATED_CONTACTO);
    }

    @Test
    @Transactional
    void getAllEmpleadosByContactoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where contacto in
        defaultEmpleadoFiltering("contacto.in=" + DEFAULT_CONTACTO + "," + UPDATED_CONTACTO, "contacto.in=" + UPDATED_CONTACTO);
    }

    @Test
    @Transactional
    void getAllEmpleadosByContactoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where contacto is not null
        defaultEmpleadoFiltering("contacto.specified=true", "contacto.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpleadosByContactoContainsSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where contacto contains
        defaultEmpleadoFiltering("contacto.contains=" + DEFAULT_CONTACTO, "contacto.contains=" + UPDATED_CONTACTO);
    }

    @Test
    @Transactional
    void getAllEmpleadosByContactoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where contacto does not contain
        defaultEmpleadoFiltering("contacto.doesNotContain=" + UPDATED_CONTACTO, "contacto.doesNotContain=" + DEFAULT_CONTACTO);
    }

    @Test
    @Transactional
    void getAllEmpleadosByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where email equals to
        defaultEmpleadoFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmpleadosByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where email in
        defaultEmpleadoFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmpleadosByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where email is not null
        defaultEmpleadoFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpleadosByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where email contains
        defaultEmpleadoFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmpleadosByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where email does not contain
        defaultEmpleadoFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmpleadosByPathImagenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where pathImagen equals to
        defaultEmpleadoFiltering("pathImagen.equals=" + DEFAULT_PATH_IMAGEN, "pathImagen.equals=" + UPDATED_PATH_IMAGEN);
    }

    @Test
    @Transactional
    void getAllEmpleadosByPathImagenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where pathImagen in
        defaultEmpleadoFiltering(
            "pathImagen.in=" + DEFAULT_PATH_IMAGEN + "," + UPDATED_PATH_IMAGEN,
            "pathImagen.in=" + UPDATED_PATH_IMAGEN
        );
    }

    @Test
    @Transactional
    void getAllEmpleadosByPathImagenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where pathImagen is not null
        defaultEmpleadoFiltering("pathImagen.specified=true", "pathImagen.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpleadosByPathImagenContainsSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where pathImagen contains
        defaultEmpleadoFiltering("pathImagen.contains=" + DEFAULT_PATH_IMAGEN, "pathImagen.contains=" + UPDATED_PATH_IMAGEN);
    }

    @Test
    @Transactional
    void getAllEmpleadosByPathImagenNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where pathImagen does not contain
        defaultEmpleadoFiltering("pathImagen.doesNotContain=" + UPDATED_PATH_IMAGEN, "pathImagen.doesNotContain=" + DEFAULT_PATH_IMAGEN);
    }

    @Test
    @Transactional
    void getAllEmpleadosByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where telefono equals to
        defaultEmpleadoFiltering("telefono.equals=" + DEFAULT_TELEFONO, "telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllEmpleadosByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where telefono in
        defaultEmpleadoFiltering("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO, "telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllEmpleadosByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where telefono is not null
        defaultEmpleadoFiltering("telefono.specified=true", "telefono.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpleadosByTelefonoContainsSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where telefono contains
        defaultEmpleadoFiltering("telefono.contains=" + DEFAULT_TELEFONO, "telefono.contains=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllEmpleadosByTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where telefono does not contain
        defaultEmpleadoFiltering("telefono.doesNotContain=" + UPDATED_TELEFONO, "telefono.doesNotContain=" + DEFAULT_TELEFONO);
    }

    private void defaultEmpleadoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEmpleadoShouldBeFound(shouldBeFound);
        defaultEmpleadoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmpleadoShouldBeFound(String filter) throws Exception {
        restEmpleadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empleado.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].contacto").value(hasItem(DEFAULT_CONTACTO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].pathImagen").value(hasItem(DEFAULT_PATH_IMAGEN)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)));

        // Check, that the count call also returns 1
        restEmpleadoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmpleadoShouldNotBeFound(String filter) throws Exception {
        restEmpleadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmpleadoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmpleado() throws Exception {
        // Get the empleado
        restEmpleadoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmpleado() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleado
        Empleado updatedEmpleado = empleadoRepository.findById(empleado.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmpleado are not directly saved in db
        em.detach(updatedEmpleado);
        updatedEmpleado
            .noCia(UPDATED_NO_CIA)
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .contacto(UPDATED_CONTACTO)
            .email(UPDATED_EMAIL)
            .pathImagen(UPDATED_PATH_IMAGEN)
            .telefono(UPDATED_TELEFONO);
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(updatedEmpleado);

        restEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empleadoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(empleadoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmpleadoToMatchAllProperties(updatedEmpleado);
    }

    @Test
    @Transactional
    void putNonExistingEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleado.setId(longCount.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empleadoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(empleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleado.setId(longCount.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(empleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleado.setId(longCount.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmpleadoWithPatch() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleado using partial update
        Empleado partialUpdatedEmpleado = new Empleado();
        partialUpdatedEmpleado.setId(empleado.getId());

        partialUpdatedEmpleado.dni(UPDATED_DNI);

        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpleado.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmpleado))
            )
            .andExpect(status().isOk());

        // Validate the Empleado in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmpleadoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEmpleado, empleado), getPersistedEmpleado(empleado));
    }

    @Test
    @Transactional
    void fullUpdateEmpleadoWithPatch() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleado using partial update
        Empleado partialUpdatedEmpleado = new Empleado();
        partialUpdatedEmpleado.setId(empleado.getId());

        partialUpdatedEmpleado
            .noCia(UPDATED_NO_CIA)
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .contacto(UPDATED_CONTACTO)
            .email(UPDATED_EMAIL)
            .pathImagen(UPDATED_PATH_IMAGEN)
            .telefono(UPDATED_TELEFONO);

        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpleado.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmpleado))
            )
            .andExpect(status().isOk());

        // Validate the Empleado in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmpleadoUpdatableFieldsEquals(partialUpdatedEmpleado, getPersistedEmpleado(partialUpdatedEmpleado));
    }

    @Test
    @Transactional
    void patchNonExistingEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleado.setId(longCount.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, empleadoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(empleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleado.setId(longCount.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(empleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleado.setId(longCount.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(empleadoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmpleado() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the empleado
        restEmpleadoMockMvc
            .perform(delete(ENTITY_API_URL_ID, empleado.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return empleadoRepository.count();
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

    protected Empleado getPersistedEmpleado(Empleado empleado) {
        return empleadoRepository.findById(empleado.getId()).orElseThrow();
    }

    protected void assertPersistedEmpleadoToMatchAllProperties(Empleado expectedEmpleado) {
        assertEmpleadoAllPropertiesEquals(expectedEmpleado, getPersistedEmpleado(expectedEmpleado));
    }

    protected void assertPersistedEmpleadoToMatchUpdatableProperties(Empleado expectedEmpleado) {
        assertEmpleadoAllUpdatablePropertiesEquals(expectedEmpleado, getPersistedEmpleado(expectedEmpleado));
    }
}
