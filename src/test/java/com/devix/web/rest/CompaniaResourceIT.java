package com.devix.web.rest;

import static com.devix.domain.CompaniaAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.Compania;
import com.devix.repository.CompaniaRepository;
import com.devix.service.dto.CompaniaDTO;
import com.devix.service.mapper.CompaniaMapper;
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
 * Integration tests for the {@link CompaniaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompaniaResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_DNI = "AAAAAAAAAA";
    private static final String UPDATED_DNI = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "d0.2@vx3pc.pho";
    private static final String UPDATED_EMAIL = "h@.y..tpi";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_PATH_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_PATH_IMAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVA = false;
    private static final Boolean UPDATED_ACTIVA = true;

    private static final String ENTITY_API_URL = "/api/companias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompaniaRepository companiaRepository;

    @Autowired
    private CompaniaMapper companiaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompaniaMockMvc;

    private Compania compania;

    private Compania insertedCompania;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compania createEntity() {
        return new Compania()
            .noCia(DEFAULT_NO_CIA)
            .dni(DEFAULT_DNI)
            .nombre(DEFAULT_NOMBRE)
            .direccion(DEFAULT_DIRECCION)
            .email(DEFAULT_EMAIL)
            .telefono(DEFAULT_TELEFONO)
            .pathImage(DEFAULT_PATH_IMAGE)
            .activa(DEFAULT_ACTIVA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compania createUpdatedEntity() {
        return new Compania()
            .noCia(UPDATED_NO_CIA)
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .pathImage(UPDATED_PATH_IMAGE)
            .activa(UPDATED_ACTIVA);
    }

    @BeforeEach
    void initTest() {
        compania = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCompania != null) {
            companiaRepository.delete(insertedCompania);
            insertedCompania = null;
        }
    }

    @Test
    @Transactional
    void createCompania() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Compania
        CompaniaDTO companiaDTO = companiaMapper.toDto(compania);
        var returnedCompaniaDTO = om.readValue(
            restCompaniaMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companiaDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CompaniaDTO.class
        );

        // Validate the Compania in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCompania = companiaMapper.toEntity(returnedCompaniaDTO);
        assertCompaniaUpdatableFieldsEquals(returnedCompania, getPersistedCompania(returnedCompania));

        insertedCompania = returnedCompania;
    }

    @Test
    @Transactional
    void createCompaniaWithExistingId() throws Exception {
        // Create the Compania with an existing ID
        compania.setId(1L);
        CompaniaDTO companiaDTO = companiaMapper.toDto(compania);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompaniaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companiaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Compania in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compania.setNoCia(null);

        // Create the Compania, which fails.
        CompaniaDTO companiaDTO = companiaMapper.toDto(compania);

        restCompaniaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companiaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDniIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compania.setDni(null);

        // Create the Compania, which fails.
        CompaniaDTO companiaDTO = companiaMapper.toDto(compania);

        restCompaniaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companiaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compania.setNombre(null);

        // Create the Compania, which fails.
        CompaniaDTO companiaDTO = companiaMapper.toDto(compania);

        restCompaniaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companiaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDireccionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compania.setDireccion(null);

        // Create the Compania, which fails.
        CompaniaDTO companiaDTO = companiaMapper.toDto(compania);

        restCompaniaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companiaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compania.setEmail(null);

        // Create the Compania, which fails.
        CompaniaDTO companiaDTO = companiaMapper.toDto(compania);

        restCompaniaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companiaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefonoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compania.setTelefono(null);

        // Create the Compania, which fails.
        CompaniaDTO companiaDTO = companiaMapper.toDto(compania);

        restCompaniaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companiaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPathImageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compania.setPathImage(null);

        // Create the Compania, which fails.
        CompaniaDTO companiaDTO = companiaMapper.toDto(compania);

        restCompaniaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companiaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compania.setActiva(null);

        // Create the Compania, which fails.
        CompaniaDTO companiaDTO = companiaMapper.toDto(compania);

        restCompaniaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companiaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompanias() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList
        restCompaniaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compania.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].pathImage").value(hasItem(DEFAULT_PATH_IMAGE)))
            .andExpect(jsonPath("$.[*].activa").value(hasItem(DEFAULT_ACTIVA)));
    }

    @Test
    @Transactional
    void getCompania() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get the compania
        restCompaniaMockMvc
            .perform(get(ENTITY_API_URL_ID, compania.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compania.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.pathImage").value(DEFAULT_PATH_IMAGE))
            .andExpect(jsonPath("$.activa").value(DEFAULT_ACTIVA));
    }

    @Test
    @Transactional
    void getCompaniasByIdFiltering() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        Long id = compania.getId();

        defaultCompaniaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCompaniaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCompaniaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCompaniasByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where noCia equals to
        defaultCompaniaFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCompaniasByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where noCia in
        defaultCompaniaFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCompaniasByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where noCia is not null
        defaultCompaniaFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniasByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where noCia is greater than or equal to
        defaultCompaniaFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCompaniasByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where noCia is less than or equal to
        defaultCompaniaFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCompaniasByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where noCia is less than
        defaultCompaniaFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCompaniasByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where noCia is greater than
        defaultCompaniaFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCompaniasByDniIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where dni equals to
        defaultCompaniaFiltering("dni.equals=" + DEFAULT_DNI, "dni.equals=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllCompaniasByDniIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where dni in
        defaultCompaniaFiltering("dni.in=" + DEFAULT_DNI + "," + UPDATED_DNI, "dni.in=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllCompaniasByDniIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where dni is not null
        defaultCompaniaFiltering("dni.specified=true", "dni.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniasByDniContainsSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where dni contains
        defaultCompaniaFiltering("dni.contains=" + DEFAULT_DNI, "dni.contains=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllCompaniasByDniNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where dni does not contain
        defaultCompaniaFiltering("dni.doesNotContain=" + UPDATED_DNI, "dni.doesNotContain=" + DEFAULT_DNI);
    }

    @Test
    @Transactional
    void getAllCompaniasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where nombre equals to
        defaultCompaniaFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCompaniasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where nombre in
        defaultCompaniaFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCompaniasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where nombre is not null
        defaultCompaniaFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniasByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where nombre contains
        defaultCompaniaFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCompaniasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where nombre does not contain
        defaultCompaniaFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCompaniasByDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where direccion equals to
        defaultCompaniaFiltering("direccion.equals=" + DEFAULT_DIRECCION, "direccion.equals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllCompaniasByDireccionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where direccion in
        defaultCompaniaFiltering("direccion.in=" + DEFAULT_DIRECCION + "," + UPDATED_DIRECCION, "direccion.in=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllCompaniasByDireccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where direccion is not null
        defaultCompaniaFiltering("direccion.specified=true", "direccion.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniasByDireccionContainsSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where direccion contains
        defaultCompaniaFiltering("direccion.contains=" + DEFAULT_DIRECCION, "direccion.contains=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllCompaniasByDireccionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where direccion does not contain
        defaultCompaniaFiltering("direccion.doesNotContain=" + UPDATED_DIRECCION, "direccion.doesNotContain=" + DEFAULT_DIRECCION);
    }

    @Test
    @Transactional
    void getAllCompaniasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where email equals to
        defaultCompaniaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCompaniasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where email in
        defaultCompaniaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCompaniasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where email is not null
        defaultCompaniaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where email contains
        defaultCompaniaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCompaniasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where email does not contain
        defaultCompaniaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllCompaniasByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where telefono equals to
        defaultCompaniaFiltering("telefono.equals=" + DEFAULT_TELEFONO, "telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllCompaniasByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where telefono in
        defaultCompaniaFiltering("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO, "telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllCompaniasByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where telefono is not null
        defaultCompaniaFiltering("telefono.specified=true", "telefono.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniasByTelefonoContainsSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where telefono contains
        defaultCompaniaFiltering("telefono.contains=" + DEFAULT_TELEFONO, "telefono.contains=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllCompaniasByTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where telefono does not contain
        defaultCompaniaFiltering("telefono.doesNotContain=" + UPDATED_TELEFONO, "telefono.doesNotContain=" + DEFAULT_TELEFONO);
    }

    @Test
    @Transactional
    void getAllCompaniasByPathImageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where pathImage equals to
        defaultCompaniaFiltering("pathImage.equals=" + DEFAULT_PATH_IMAGE, "pathImage.equals=" + UPDATED_PATH_IMAGE);
    }

    @Test
    @Transactional
    void getAllCompaniasByPathImageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where pathImage in
        defaultCompaniaFiltering("pathImage.in=" + DEFAULT_PATH_IMAGE + "," + UPDATED_PATH_IMAGE, "pathImage.in=" + UPDATED_PATH_IMAGE);
    }

    @Test
    @Transactional
    void getAllCompaniasByPathImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where pathImage is not null
        defaultCompaniaFiltering("pathImage.specified=true", "pathImage.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniasByPathImageContainsSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where pathImage contains
        defaultCompaniaFiltering("pathImage.contains=" + DEFAULT_PATH_IMAGE, "pathImage.contains=" + UPDATED_PATH_IMAGE);
    }

    @Test
    @Transactional
    void getAllCompaniasByPathImageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where pathImage does not contain
        defaultCompaniaFiltering("pathImage.doesNotContain=" + UPDATED_PATH_IMAGE, "pathImage.doesNotContain=" + DEFAULT_PATH_IMAGE);
    }

    @Test
    @Transactional
    void getAllCompaniasByActivaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where activa equals to
        defaultCompaniaFiltering("activa.equals=" + DEFAULT_ACTIVA, "activa.equals=" + UPDATED_ACTIVA);
    }

    @Test
    @Transactional
    void getAllCompaniasByActivaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where activa in
        defaultCompaniaFiltering("activa.in=" + DEFAULT_ACTIVA + "," + UPDATED_ACTIVA, "activa.in=" + UPDATED_ACTIVA);
    }

    @Test
    @Transactional
    void getAllCompaniasByActivaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        // Get all the companiaList where activa is not null
        defaultCompaniaFiltering("activa.specified=true", "activa.specified=false");
    }

    private void defaultCompaniaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCompaniaShouldBeFound(shouldBeFound);
        defaultCompaniaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompaniaShouldBeFound(String filter) throws Exception {
        restCompaniaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compania.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].pathImage").value(hasItem(DEFAULT_PATH_IMAGE)))
            .andExpect(jsonPath("$.[*].activa").value(hasItem(DEFAULT_ACTIVA)));

        // Check, that the count call also returns 1
        restCompaniaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompaniaShouldNotBeFound(String filter) throws Exception {
        restCompaniaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompaniaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCompania() throws Exception {
        // Get the compania
        restCompaniaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompania() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compania
        Compania updatedCompania = companiaRepository.findById(compania.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCompania are not directly saved in db
        em.detach(updatedCompania);
        updatedCompania
            .noCia(UPDATED_NO_CIA)
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .pathImage(UPDATED_PATH_IMAGE)
            .activa(UPDATED_ACTIVA);
        CompaniaDTO companiaDTO = companiaMapper.toDto(updatedCompania);

        restCompaniaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companiaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(companiaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Compania in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompaniaToMatchAllProperties(updatedCompania);
    }

    @Test
    @Transactional
    void putNonExistingCompania() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compania.setId(longCount.incrementAndGet());

        // Create the Compania
        CompaniaDTO companiaDTO = companiaMapper.toDto(compania);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompaniaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companiaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(companiaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compania in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompania() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compania.setId(longCount.incrementAndGet());

        // Create the Compania
        CompaniaDTO companiaDTO = companiaMapper.toDto(compania);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompaniaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(companiaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compania in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompania() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compania.setId(longCount.incrementAndGet());

        // Create the Compania
        CompaniaDTO companiaDTO = companiaMapper.toDto(compania);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompaniaMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companiaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compania in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompaniaWithPatch() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compania using partial update
        Compania partialUpdatedCompania = new Compania();
        partialUpdatedCompania.setId(compania.getId());

        partialUpdatedCompania
            .noCia(UPDATED_NO_CIA)
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .activa(UPDATED_ACTIVA);

        restCompaniaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompania.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompania))
            )
            .andExpect(status().isOk());

        // Validate the Compania in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompaniaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCompania, compania), getPersistedCompania(compania));
    }

    @Test
    @Transactional
    void fullUpdateCompaniaWithPatch() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compania using partial update
        Compania partialUpdatedCompania = new Compania();
        partialUpdatedCompania.setId(compania.getId());

        partialUpdatedCompania
            .noCia(UPDATED_NO_CIA)
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .pathImage(UPDATED_PATH_IMAGE)
            .activa(UPDATED_ACTIVA);

        restCompaniaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompania.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompania))
            )
            .andExpect(status().isOk());

        // Validate the Compania in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompaniaUpdatableFieldsEquals(partialUpdatedCompania, getPersistedCompania(partialUpdatedCompania));
    }

    @Test
    @Transactional
    void patchNonExistingCompania() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compania.setId(longCount.incrementAndGet());

        // Create the Compania
        CompaniaDTO companiaDTO = companiaMapper.toDto(compania);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompaniaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, companiaDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(companiaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compania in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompania() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compania.setId(longCount.incrementAndGet());

        // Create the Compania
        CompaniaDTO companiaDTO = companiaMapper.toDto(compania);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompaniaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(companiaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compania in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompania() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compania.setId(longCount.incrementAndGet());

        // Create the Compania
        CompaniaDTO companiaDTO = companiaMapper.toDto(compania);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompaniaMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(companiaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compania in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompania() throws Exception {
        // Initialize the database
        insertedCompania = companiaRepository.saveAndFlush(compania);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the compania
        restCompaniaMockMvc
            .perform(delete(ENTITY_API_URL_ID, compania.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return companiaRepository.count();
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

    protected Compania getPersistedCompania(Compania compania) {
        return companiaRepository.findById(compania.getId()).orElseThrow();
    }

    protected void assertPersistedCompaniaToMatchAllProperties(Compania expectedCompania) {
        assertCompaniaAllPropertiesEquals(expectedCompania, getPersistedCompania(expectedCompania));
    }

    protected void assertPersistedCompaniaToMatchUpdatableProperties(Compania expectedCompania) {
        assertCompaniaAllUpdatablePropertiesEquals(expectedCompania, getPersistedCompania(expectedCompania));
    }
}
