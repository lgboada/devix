package com.devix.web.rest;

import static com.devix.domain.ProveedorAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.Proveedor;
import com.devix.repository.ProveedorRepository;
import com.devix.service.dto.ProveedorDTO;
import com.devix.service.mapper.ProveedorMapper;
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
 * Integration tests for the {@link ProveedorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProveedorResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_DNI = "AAAAAAAAAA";
    private static final String UPDATED_DNI = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACTO = "AAAAAAAAAA";
    private static final String UPDATED_CONTACTO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "f0gs@bmgs6.vly";
    private static final String UPDATED_EMAIL = "892rsk@syemrk.gs";

    private static final String DEFAULT_PATH_IMAGEN = "AAAAAAAAAA";
    private static final String UPDATED_PATH_IMAGEN = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/proveedors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ProveedorMapper proveedorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProveedorMockMvc;

    private Proveedor proveedor;

    private Proveedor insertedProveedor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proveedor createEntity() {
        return new Proveedor()
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
    public static Proveedor createUpdatedEntity() {
        return new Proveedor()
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
        proveedor = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProveedor != null) {
            proveedorRepository.delete(insertedProveedor);
            insertedProveedor = null;
        }
    }

    @Test
    @Transactional
    void createProveedor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);
        var returnedProveedorDTO = om.readValue(
            restProveedorMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProveedorDTO.class
        );

        // Validate the Proveedor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProveedor = proveedorMapper.toEntity(returnedProveedorDTO);
        assertProveedorUpdatableFieldsEquals(returnedProveedor, getPersistedProveedor(returnedProveedor));

        insertedProveedor = returnedProveedor;
    }

    @Test
    @Transactional
    void createProveedorWithExistingId() throws Exception {
        // Create the Proveedor with an existing ID
        proveedor.setId(1L);
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProveedorMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        proveedor.setNoCia(null);

        // Create the Proveedor, which fails.
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        restProveedorMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDniIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        proveedor.setDni(null);

        // Create the Proveedor, which fails.
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        restProveedorMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        proveedor.setNombre(null);

        // Create the Proveedor, which fails.
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        restProveedorMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        proveedor.setEmail(null);

        // Create the Proveedor, which fails.
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        restProveedorMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPathImagenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        proveedor.setPathImagen(null);

        // Create the Proveedor, which fails.
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        restProveedorMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefonoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        proveedor.setTelefono(null);

        // Create the Proveedor, which fails.
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        restProveedorMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProveedors() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList
        restProveedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proveedor.getId().intValue())))
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
    void getProveedor() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get the proveedor
        restProveedorMockMvc
            .perform(get(ENTITY_API_URL_ID, proveedor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proveedor.getId().intValue()))
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
    void getProveedorsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        Long id = proveedor.getId();

        defaultProveedorFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProveedorFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProveedorFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProveedorsByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where noCia equals to
        defaultProveedorFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllProveedorsByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where noCia in
        defaultProveedorFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllProveedorsByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where noCia is not null
        defaultProveedorFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllProveedorsByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where noCia is greater than or equal to
        defaultProveedorFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllProveedorsByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where noCia is less than or equal to
        defaultProveedorFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllProveedorsByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where noCia is less than
        defaultProveedorFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllProveedorsByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where noCia is greater than
        defaultProveedorFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllProveedorsByDniIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where dni equals to
        defaultProveedorFiltering("dni.equals=" + DEFAULT_DNI, "dni.equals=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllProveedorsByDniIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where dni in
        defaultProveedorFiltering("dni.in=" + DEFAULT_DNI + "," + UPDATED_DNI, "dni.in=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllProveedorsByDniIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where dni is not null
        defaultProveedorFiltering("dni.specified=true", "dni.specified=false");
    }

    @Test
    @Transactional
    void getAllProveedorsByDniContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where dni contains
        defaultProveedorFiltering("dni.contains=" + DEFAULT_DNI, "dni.contains=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllProveedorsByDniNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where dni does not contain
        defaultProveedorFiltering("dni.doesNotContain=" + UPDATED_DNI, "dni.doesNotContain=" + DEFAULT_DNI);
    }

    @Test
    @Transactional
    void getAllProveedorsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where nombre equals to
        defaultProveedorFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProveedorsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where nombre in
        defaultProveedorFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProveedorsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where nombre is not null
        defaultProveedorFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllProveedorsByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where nombre contains
        defaultProveedorFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProveedorsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where nombre does not contain
        defaultProveedorFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProveedorsByContactoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where contacto equals to
        defaultProveedorFiltering("contacto.equals=" + DEFAULT_CONTACTO, "contacto.equals=" + UPDATED_CONTACTO);
    }

    @Test
    @Transactional
    void getAllProveedorsByContactoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where contacto in
        defaultProveedorFiltering("contacto.in=" + DEFAULT_CONTACTO + "," + UPDATED_CONTACTO, "contacto.in=" + UPDATED_CONTACTO);
    }

    @Test
    @Transactional
    void getAllProveedorsByContactoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where contacto is not null
        defaultProveedorFiltering("contacto.specified=true", "contacto.specified=false");
    }

    @Test
    @Transactional
    void getAllProveedorsByContactoContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where contacto contains
        defaultProveedorFiltering("contacto.contains=" + DEFAULT_CONTACTO, "contacto.contains=" + UPDATED_CONTACTO);
    }

    @Test
    @Transactional
    void getAllProveedorsByContactoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where contacto does not contain
        defaultProveedorFiltering("contacto.doesNotContain=" + UPDATED_CONTACTO, "contacto.doesNotContain=" + DEFAULT_CONTACTO);
    }

    @Test
    @Transactional
    void getAllProveedorsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where email equals to
        defaultProveedorFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllProveedorsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where email in
        defaultProveedorFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllProveedorsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where email is not null
        defaultProveedorFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllProveedorsByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where email contains
        defaultProveedorFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllProveedorsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where email does not contain
        defaultProveedorFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllProveedorsByPathImagenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where pathImagen equals to
        defaultProveedorFiltering("pathImagen.equals=" + DEFAULT_PATH_IMAGEN, "pathImagen.equals=" + UPDATED_PATH_IMAGEN);
    }

    @Test
    @Transactional
    void getAllProveedorsByPathImagenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where pathImagen in
        defaultProveedorFiltering(
            "pathImagen.in=" + DEFAULT_PATH_IMAGEN + "," + UPDATED_PATH_IMAGEN,
            "pathImagen.in=" + UPDATED_PATH_IMAGEN
        );
    }

    @Test
    @Transactional
    void getAllProveedorsByPathImagenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where pathImagen is not null
        defaultProveedorFiltering("pathImagen.specified=true", "pathImagen.specified=false");
    }

    @Test
    @Transactional
    void getAllProveedorsByPathImagenContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where pathImagen contains
        defaultProveedorFiltering("pathImagen.contains=" + DEFAULT_PATH_IMAGEN, "pathImagen.contains=" + UPDATED_PATH_IMAGEN);
    }

    @Test
    @Transactional
    void getAllProveedorsByPathImagenNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where pathImagen does not contain
        defaultProveedorFiltering("pathImagen.doesNotContain=" + UPDATED_PATH_IMAGEN, "pathImagen.doesNotContain=" + DEFAULT_PATH_IMAGEN);
    }

    @Test
    @Transactional
    void getAllProveedorsByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where telefono equals to
        defaultProveedorFiltering("telefono.equals=" + DEFAULT_TELEFONO, "telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllProveedorsByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where telefono in
        defaultProveedorFiltering("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO, "telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllProveedorsByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where telefono is not null
        defaultProveedorFiltering("telefono.specified=true", "telefono.specified=false");
    }

    @Test
    @Transactional
    void getAllProveedorsByTelefonoContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where telefono contains
        defaultProveedorFiltering("telefono.contains=" + DEFAULT_TELEFONO, "telefono.contains=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllProveedorsByTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList where telefono does not contain
        defaultProveedorFiltering("telefono.doesNotContain=" + UPDATED_TELEFONO, "telefono.doesNotContain=" + DEFAULT_TELEFONO);
    }

    private void defaultProveedorFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProveedorShouldBeFound(shouldBeFound);
        defaultProveedorShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProveedorShouldBeFound(String filter) throws Exception {
        restProveedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proveedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].contacto").value(hasItem(DEFAULT_CONTACTO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].pathImagen").value(hasItem(DEFAULT_PATH_IMAGEN)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)));

        // Check, that the count call also returns 1
        restProveedorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProveedorShouldNotBeFound(String filter) throws Exception {
        restProveedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProveedorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProveedor() throws Exception {
        // Get the proveedor
        restProveedorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProveedor() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proveedor
        Proveedor updatedProveedor = proveedorRepository.findById(proveedor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProveedor are not directly saved in db
        em.detach(updatedProveedor);
        updatedProveedor
            .noCia(UPDATED_NO_CIA)
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .contacto(UPDATED_CONTACTO)
            .email(UPDATED_EMAIL)
            .pathImagen(UPDATED_PATH_IMAGEN)
            .telefono(UPDATED_TELEFONO);
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(updatedProveedor);

        restProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proveedorDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(proveedorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProveedorToMatchAllProperties(updatedProveedor);
    }

    @Test
    @Transactional
    void putNonExistingProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proveedor.setId(longCount.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proveedorDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(proveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proveedor.setId(longCount.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(proveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proveedor.setId(longCount.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProveedorWithPatch() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proveedor using partial update
        Proveedor partialUpdatedProveedor = new Proveedor();
        partialUpdatedProveedor.setId(proveedor.getId());

        partialUpdatedProveedor.dni(UPDATED_DNI).contacto(UPDATED_CONTACTO).email(UPDATED_EMAIL).pathImagen(UPDATED_PATH_IMAGEN);

        restProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProveedor.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProveedor))
            )
            .andExpect(status().isOk());

        // Validate the Proveedor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProveedorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProveedor, proveedor),
            getPersistedProveedor(proveedor)
        );
    }

    @Test
    @Transactional
    void fullUpdateProveedorWithPatch() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proveedor using partial update
        Proveedor partialUpdatedProveedor = new Proveedor();
        partialUpdatedProveedor.setId(proveedor.getId());

        partialUpdatedProveedor
            .noCia(UPDATED_NO_CIA)
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .contacto(UPDATED_CONTACTO)
            .email(UPDATED_EMAIL)
            .pathImagen(UPDATED_PATH_IMAGEN)
            .telefono(UPDATED_TELEFONO);

        restProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProveedor.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProveedor))
            )
            .andExpect(status().isOk());

        // Validate the Proveedor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProveedorUpdatableFieldsEquals(partialUpdatedProveedor, getPersistedProveedor(partialUpdatedProveedor));
    }

    @Test
    @Transactional
    void patchNonExistingProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proveedor.setId(longCount.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, proveedorDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(proveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proveedor.setId(longCount.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(proveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proveedor.setId(longCount.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(proveedorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProveedor() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the proveedor
        restProveedorMockMvc
            .perform(delete(ENTITY_API_URL_ID, proveedor.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return proveedorRepository.count();
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

    protected Proveedor getPersistedProveedor(Proveedor proveedor) {
        return proveedorRepository.findById(proveedor.getId()).orElseThrow();
    }

    protected void assertPersistedProveedorToMatchAllProperties(Proveedor expectedProveedor) {
        assertProveedorAllPropertiesEquals(expectedProveedor, getPersistedProveedor(expectedProveedor));
    }

    protected void assertPersistedProveedorToMatchUpdatableProperties(Proveedor expectedProveedor) {
        assertProveedorAllUpdatablePropertiesEquals(expectedProveedor, getPersistedProveedor(expectedProveedor));
    }
}
