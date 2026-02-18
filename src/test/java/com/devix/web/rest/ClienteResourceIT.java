package com.devix.web.rest;

import static com.devix.domain.ClienteAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.Ciudad;
import com.devix.domain.Cliente;
import com.devix.domain.TipoCliente;
import com.devix.repository.ClienteRepository;
import com.devix.service.dto.ClienteDTO;
import com.devix.service.mapper.ClienteMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ClienteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClienteResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_DNI = "AAAAAAAAAA";
    private static final String UPDATED_DNI = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRES = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRES = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_COMERCIAL = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_COMERCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "c@3d.opip";
    private static final String UPDATED_EMAIL = "f@bwt1g.hdxq";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_NACIMIENTO = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_SEXO = "AAAAAAAAAA";
    private static final String UPDATED_SEXO = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO_CIVIL = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO_CIVIL = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO_SANGRE = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_SANGRE = "BBBBBBBBBB";

    private static final String DEFAULT_PATH_IMAGEN = "AAAAAAAAAA";
    private static final String UPDATED_PATH_IMAGEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/clientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteMapper clienteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClienteMockMvc;

    private Cliente cliente;

    private Cliente insertedCliente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cliente createEntity() {
        return new Cliente()
            .noCia(DEFAULT_NO_CIA)
            .dni(DEFAULT_DNI)
            .nombres(DEFAULT_NOMBRES)
            .apellidos(DEFAULT_APELLIDOS)
            .nombreComercial(DEFAULT_NOMBRE_COMERCIAL)
            .email(DEFAULT_EMAIL)
            .telefono(DEFAULT_TELEFONO)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .sexo(DEFAULT_SEXO)
            .estadoCivil(DEFAULT_ESTADO_CIVIL)
            .tipoSangre(DEFAULT_TIPO_SANGRE)
            .pathImagen(DEFAULT_PATH_IMAGEN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cliente createUpdatedEntity() {
        return new Cliente()
            .noCia(UPDATED_NO_CIA)
            .dni(UPDATED_DNI)
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .nombreComercial(UPDATED_NOMBRE_COMERCIAL)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .sexo(UPDATED_SEXO)
            .estadoCivil(UPDATED_ESTADO_CIVIL)
            .tipoSangre(UPDATED_TIPO_SANGRE)
            .pathImagen(UPDATED_PATH_IMAGEN);
    }

    @BeforeEach
    void initTest() {
        cliente = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCliente != null) {
            clienteRepository.delete(insertedCliente);
            insertedCliente = null;
        }
    }

    @Test
    @Transactional
    void createCliente() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);
        var returnedClienteDTO = om.readValue(
            restClienteMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClienteDTO.class
        );

        // Validate the Cliente in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCliente = clienteMapper.toEntity(returnedClienteDTO);
        assertClienteUpdatableFieldsEquals(returnedCliente, getPersistedCliente(returnedCliente));

        insertedCliente = returnedCliente;
    }

    @Test
    @Transactional
    void createClienteWithExistingId() throws Exception {
        // Create the Cliente with an existing ID
        cliente.setId(1L);
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClienteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cliente.setNoCia(null);

        // Create the Cliente, which fails.
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        restClienteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDniIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cliente.setDni(null);

        // Create the Cliente, which fails.
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        restClienteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombresIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cliente.setNombres(null);

        // Create the Cliente, which fails.
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        restClienteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApellidosIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cliente.setApellidos(null);

        // Create the Cliente, which fails.
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        restClienteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cliente.setEmail(null);

        // Create the Cliente, which fails.
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        restClienteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaNacimientoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cliente.setFechaNacimiento(null);

        // Create the Cliente, which fails.
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        restClienteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cliente.setSexo(null);

        // Create the Cliente, which fails.
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        restClienteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoCivilIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cliente.setEstadoCivil(null);

        // Create the Cliente, which fails.
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        restClienteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoSangreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cliente.setTipoSangre(null);

        // Create the Cliente, which fails.
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        restClienteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPathImagenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cliente.setPathImagen(null);

        // Create the Cliente, which fails.
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        restClienteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClientes() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].nombres").value(hasItem(DEFAULT_NOMBRES)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].nombreComercial").value(hasItem(DEFAULT_NOMBRE_COMERCIAL)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO)))
            .andExpect(jsonPath("$.[*].estadoCivil").value(hasItem(DEFAULT_ESTADO_CIVIL)))
            .andExpect(jsonPath("$.[*].tipoSangre").value(hasItem(DEFAULT_TIPO_SANGRE)))
            .andExpect(jsonPath("$.[*].pathImagen").value(hasItem(DEFAULT_PATH_IMAGEN)));
    }

    @Test
    @Transactional
    void getCliente() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get the cliente
        restClienteMockMvc
            .perform(get(ENTITY_API_URL_ID, cliente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cliente.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI))
            .andExpect(jsonPath("$.nombres").value(DEFAULT_NOMBRES))
            .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS))
            .andExpect(jsonPath("$.nombreComercial").value(DEFAULT_NOMBRE_COMERCIAL))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.sexo").value(DEFAULT_SEXO))
            .andExpect(jsonPath("$.estadoCivil").value(DEFAULT_ESTADO_CIVIL))
            .andExpect(jsonPath("$.tipoSangre").value(DEFAULT_TIPO_SANGRE))
            .andExpect(jsonPath("$.pathImagen").value(DEFAULT_PATH_IMAGEN));
    }

    @Test
    @Transactional
    void getClientesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        Long id = cliente.getId();

        defaultClienteFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultClienteFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultClienteFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClientesByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where noCia equals to
        defaultClienteFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllClientesByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where noCia in
        defaultClienteFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllClientesByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where noCia is not null
        defaultClienteFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where noCia is greater than or equal to
        defaultClienteFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllClientesByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where noCia is less than or equal to
        defaultClienteFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllClientesByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where noCia is less than
        defaultClienteFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllClientesByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where noCia is greater than
        defaultClienteFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllClientesByDniIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where dni equals to
        defaultClienteFiltering("dni.equals=" + DEFAULT_DNI, "dni.equals=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllClientesByDniIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where dni in
        defaultClienteFiltering("dni.in=" + DEFAULT_DNI + "," + UPDATED_DNI, "dni.in=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllClientesByDniIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where dni is not null
        defaultClienteFiltering("dni.specified=true", "dni.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByDniContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where dni contains
        defaultClienteFiltering("dni.contains=" + DEFAULT_DNI, "dni.contains=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllClientesByDniNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where dni does not contain
        defaultClienteFiltering("dni.doesNotContain=" + UPDATED_DNI, "dni.doesNotContain=" + DEFAULT_DNI);
    }

    @Test
    @Transactional
    void getAllClientesByNombresIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nombres equals to
        defaultClienteFiltering("nombres.equals=" + DEFAULT_NOMBRES, "nombres.equals=" + UPDATED_NOMBRES);
    }

    @Test
    @Transactional
    void getAllClientesByNombresIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nombres in
        defaultClienteFiltering("nombres.in=" + DEFAULT_NOMBRES + "," + UPDATED_NOMBRES, "nombres.in=" + UPDATED_NOMBRES);
    }

    @Test
    @Transactional
    void getAllClientesByNombresIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nombres is not null
        defaultClienteFiltering("nombres.specified=true", "nombres.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByNombresContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nombres contains
        defaultClienteFiltering("nombres.contains=" + DEFAULT_NOMBRES, "nombres.contains=" + UPDATED_NOMBRES);
    }

    @Test
    @Transactional
    void getAllClientesByNombresNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nombres does not contain
        defaultClienteFiltering("nombres.doesNotContain=" + UPDATED_NOMBRES, "nombres.doesNotContain=" + DEFAULT_NOMBRES);
    }

    @Test
    @Transactional
    void getAllClientesByApellidosIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where apellidos equals to
        defaultClienteFiltering("apellidos.equals=" + DEFAULT_APELLIDOS, "apellidos.equals=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllClientesByApellidosIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where apellidos in
        defaultClienteFiltering("apellidos.in=" + DEFAULT_APELLIDOS + "," + UPDATED_APELLIDOS, "apellidos.in=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllClientesByApellidosIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where apellidos is not null
        defaultClienteFiltering("apellidos.specified=true", "apellidos.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByApellidosContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where apellidos contains
        defaultClienteFiltering("apellidos.contains=" + DEFAULT_APELLIDOS, "apellidos.contains=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllClientesByApellidosNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where apellidos does not contain
        defaultClienteFiltering("apellidos.doesNotContain=" + UPDATED_APELLIDOS, "apellidos.doesNotContain=" + DEFAULT_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllClientesByNombreComercialIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nombreComercial equals to
        defaultClienteFiltering("nombreComercial.equals=" + DEFAULT_NOMBRE_COMERCIAL, "nombreComercial.equals=" + UPDATED_NOMBRE_COMERCIAL);
    }

    @Test
    @Transactional
    void getAllClientesByNombreComercialIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nombreComercial in
        defaultClienteFiltering(
            "nombreComercial.in=" + DEFAULT_NOMBRE_COMERCIAL + "," + UPDATED_NOMBRE_COMERCIAL,
            "nombreComercial.in=" + UPDATED_NOMBRE_COMERCIAL
        );
    }

    @Test
    @Transactional
    void getAllClientesByNombreComercialIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nombreComercial is not null
        defaultClienteFiltering("nombreComercial.specified=true", "nombreComercial.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByNombreComercialContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nombreComercial contains
        defaultClienteFiltering(
            "nombreComercial.contains=" + DEFAULT_NOMBRE_COMERCIAL,
            "nombreComercial.contains=" + UPDATED_NOMBRE_COMERCIAL
        );
    }

    @Test
    @Transactional
    void getAllClientesByNombreComercialNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nombreComercial does not contain
        defaultClienteFiltering(
            "nombreComercial.doesNotContain=" + UPDATED_NOMBRE_COMERCIAL,
            "nombreComercial.doesNotContain=" + DEFAULT_NOMBRE_COMERCIAL
        );
    }

    @Test
    @Transactional
    void getAllClientesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where email equals to
        defaultClienteFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where email in
        defaultClienteFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where email is not null
        defaultClienteFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where email contains
        defaultClienteFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where email does not contain
        defaultClienteFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientesByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where telefono equals to
        defaultClienteFiltering("telefono.equals=" + DEFAULT_TELEFONO, "telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllClientesByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where telefono in
        defaultClienteFiltering("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO, "telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllClientesByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where telefono is not null
        defaultClienteFiltering("telefono.specified=true", "telefono.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByTelefonoContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where telefono contains
        defaultClienteFiltering("telefono.contains=" + DEFAULT_TELEFONO, "telefono.contains=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllClientesByTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where telefono does not contain
        defaultClienteFiltering("telefono.doesNotContain=" + UPDATED_TELEFONO, "telefono.doesNotContain=" + DEFAULT_TELEFONO);
    }

    @Test
    @Transactional
    void getAllClientesByFechaNacimientoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where fechaNacimiento equals to
        defaultClienteFiltering("fechaNacimiento.equals=" + DEFAULT_FECHA_NACIMIENTO, "fechaNacimiento.equals=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllClientesByFechaNacimientoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where fechaNacimiento in
        defaultClienteFiltering(
            "fechaNacimiento.in=" + DEFAULT_FECHA_NACIMIENTO + "," + UPDATED_FECHA_NACIMIENTO,
            "fechaNacimiento.in=" + UPDATED_FECHA_NACIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllClientesByFechaNacimientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where fechaNacimiento is not null
        defaultClienteFiltering("fechaNacimiento.specified=true", "fechaNacimiento.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByFechaNacimientoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where fechaNacimiento is greater than or equal to
        defaultClienteFiltering(
            "fechaNacimiento.greaterThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO,
            "fechaNacimiento.greaterThanOrEqual=" + UPDATED_FECHA_NACIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllClientesByFechaNacimientoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where fechaNacimiento is less than or equal to
        defaultClienteFiltering(
            "fechaNacimiento.lessThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO,
            "fechaNacimiento.lessThanOrEqual=" + SMALLER_FECHA_NACIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllClientesByFechaNacimientoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where fechaNacimiento is less than
        defaultClienteFiltering(
            "fechaNacimiento.lessThan=" + UPDATED_FECHA_NACIMIENTO,
            "fechaNacimiento.lessThan=" + DEFAULT_FECHA_NACIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllClientesByFechaNacimientoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where fechaNacimiento is greater than
        defaultClienteFiltering(
            "fechaNacimiento.greaterThan=" + SMALLER_FECHA_NACIMIENTO,
            "fechaNacimiento.greaterThan=" + DEFAULT_FECHA_NACIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllClientesBySexoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where sexo equals to
        defaultClienteFiltering("sexo.equals=" + DEFAULT_SEXO, "sexo.equals=" + UPDATED_SEXO);
    }

    @Test
    @Transactional
    void getAllClientesBySexoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where sexo in
        defaultClienteFiltering("sexo.in=" + DEFAULT_SEXO + "," + UPDATED_SEXO, "sexo.in=" + UPDATED_SEXO);
    }

    @Test
    @Transactional
    void getAllClientesBySexoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where sexo is not null
        defaultClienteFiltering("sexo.specified=true", "sexo.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesBySexoContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where sexo contains
        defaultClienteFiltering("sexo.contains=" + DEFAULT_SEXO, "sexo.contains=" + UPDATED_SEXO);
    }

    @Test
    @Transactional
    void getAllClientesBySexoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where sexo does not contain
        defaultClienteFiltering("sexo.doesNotContain=" + UPDATED_SEXO, "sexo.doesNotContain=" + DEFAULT_SEXO);
    }

    @Test
    @Transactional
    void getAllClientesByEstadoCivilIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where estadoCivil equals to
        defaultClienteFiltering("estadoCivil.equals=" + DEFAULT_ESTADO_CIVIL, "estadoCivil.equals=" + UPDATED_ESTADO_CIVIL);
    }

    @Test
    @Transactional
    void getAllClientesByEstadoCivilIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where estadoCivil in
        defaultClienteFiltering(
            "estadoCivil.in=" + DEFAULT_ESTADO_CIVIL + "," + UPDATED_ESTADO_CIVIL,
            "estadoCivil.in=" + UPDATED_ESTADO_CIVIL
        );
    }

    @Test
    @Transactional
    void getAllClientesByEstadoCivilIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where estadoCivil is not null
        defaultClienteFiltering("estadoCivil.specified=true", "estadoCivil.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByEstadoCivilContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where estadoCivil contains
        defaultClienteFiltering("estadoCivil.contains=" + DEFAULT_ESTADO_CIVIL, "estadoCivil.contains=" + UPDATED_ESTADO_CIVIL);
    }

    @Test
    @Transactional
    void getAllClientesByEstadoCivilNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where estadoCivil does not contain
        defaultClienteFiltering("estadoCivil.doesNotContain=" + UPDATED_ESTADO_CIVIL, "estadoCivil.doesNotContain=" + DEFAULT_ESTADO_CIVIL);
    }

    @Test
    @Transactional
    void getAllClientesByTipoSangreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where tipoSangre equals to
        defaultClienteFiltering("tipoSangre.equals=" + DEFAULT_TIPO_SANGRE, "tipoSangre.equals=" + UPDATED_TIPO_SANGRE);
    }

    @Test
    @Transactional
    void getAllClientesByTipoSangreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where tipoSangre in
        defaultClienteFiltering("tipoSangre.in=" + DEFAULT_TIPO_SANGRE + "," + UPDATED_TIPO_SANGRE, "tipoSangre.in=" + UPDATED_TIPO_SANGRE);
    }

    @Test
    @Transactional
    void getAllClientesByTipoSangreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where tipoSangre is not null
        defaultClienteFiltering("tipoSangre.specified=true", "tipoSangre.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByTipoSangreContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where tipoSangre contains
        defaultClienteFiltering("tipoSangre.contains=" + DEFAULT_TIPO_SANGRE, "tipoSangre.contains=" + UPDATED_TIPO_SANGRE);
    }

    @Test
    @Transactional
    void getAllClientesByTipoSangreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where tipoSangre does not contain
        defaultClienteFiltering("tipoSangre.doesNotContain=" + UPDATED_TIPO_SANGRE, "tipoSangre.doesNotContain=" + DEFAULT_TIPO_SANGRE);
    }

    @Test
    @Transactional
    void getAllClientesByPathImagenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where pathImagen equals to
        defaultClienteFiltering("pathImagen.equals=" + DEFAULT_PATH_IMAGEN, "pathImagen.equals=" + UPDATED_PATH_IMAGEN);
    }

    @Test
    @Transactional
    void getAllClientesByPathImagenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where pathImagen in
        defaultClienteFiltering("pathImagen.in=" + DEFAULT_PATH_IMAGEN + "," + UPDATED_PATH_IMAGEN, "pathImagen.in=" + UPDATED_PATH_IMAGEN);
    }

    @Test
    @Transactional
    void getAllClientesByPathImagenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where pathImagen is not null
        defaultClienteFiltering("pathImagen.specified=true", "pathImagen.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByPathImagenContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where pathImagen contains
        defaultClienteFiltering("pathImagen.contains=" + DEFAULT_PATH_IMAGEN, "pathImagen.contains=" + UPDATED_PATH_IMAGEN);
    }

    @Test
    @Transactional
    void getAllClientesByPathImagenNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where pathImagen does not contain
        defaultClienteFiltering("pathImagen.doesNotContain=" + UPDATED_PATH_IMAGEN, "pathImagen.doesNotContain=" + DEFAULT_PATH_IMAGEN);
    }

    @Test
    @Transactional
    void getAllClientesByTipoClienteIsEqualToSomething() throws Exception {
        TipoCliente tipoCliente;
        if (TestUtil.findAll(em, TipoCliente.class).isEmpty()) {
            clienteRepository.saveAndFlush(cliente);
            tipoCliente = TipoClienteResourceIT.createEntity();
        } else {
            tipoCliente = TestUtil.findAll(em, TipoCliente.class).get(0);
        }
        em.persist(tipoCliente);
        em.flush();
        cliente.setTipoCliente(tipoCliente);
        clienteRepository.saveAndFlush(cliente);
        Long tipoClienteId = tipoCliente.getId();
        // Get all the clienteList where tipoCliente equals to tipoClienteId
        defaultClienteShouldBeFound("tipoClienteId.equals=" + tipoClienteId);

        // Get all the clienteList where tipoCliente equals to (tipoClienteId + 1)
        defaultClienteShouldNotBeFound("tipoClienteId.equals=" + (tipoClienteId + 1));
    }

    @Test
    @Transactional
    void getAllClientesByCiudadIsEqualToSomething() throws Exception {
        Ciudad ciudad;
        if (TestUtil.findAll(em, Ciudad.class).isEmpty()) {
            clienteRepository.saveAndFlush(cliente);
            ciudad = CiudadResourceIT.createEntity();
        } else {
            ciudad = TestUtil.findAll(em, Ciudad.class).get(0);
        }
        em.persist(ciudad);
        em.flush();
        cliente.setCiudad(ciudad);
        clienteRepository.saveAndFlush(cliente);
        Long ciudadId = ciudad.getId();
        // Get all the clienteList where ciudad equals to ciudadId
        defaultClienteShouldBeFound("ciudadId.equals=" + ciudadId);

        // Get all the clienteList where ciudad equals to (ciudadId + 1)
        defaultClienteShouldNotBeFound("ciudadId.equals=" + (ciudadId + 1));
    }

    private void defaultClienteFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultClienteShouldBeFound(shouldBeFound);
        defaultClienteShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClienteShouldBeFound(String filter) throws Exception {
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].nombres").value(hasItem(DEFAULT_NOMBRES)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].nombreComercial").value(hasItem(DEFAULT_NOMBRE_COMERCIAL)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO)))
            .andExpect(jsonPath("$.[*].estadoCivil").value(hasItem(DEFAULT_ESTADO_CIVIL)))
            .andExpect(jsonPath("$.[*].tipoSangre").value(hasItem(DEFAULT_TIPO_SANGRE)))
            .andExpect(jsonPath("$.[*].pathImagen").value(hasItem(DEFAULT_PATH_IMAGEN)));

        // Check, that the count call also returns 1
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClienteShouldNotBeFound(String filter) throws Exception {
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCliente() throws Exception {
        // Get the cliente
        restClienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCliente() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cliente
        Cliente updatedCliente = clienteRepository.findById(cliente.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCliente are not directly saved in db
        em.detach(updatedCliente);
        updatedCliente
            .noCia(UPDATED_NO_CIA)
            .dni(UPDATED_DNI)
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .nombreComercial(UPDATED_NOMBRE_COMERCIAL)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .sexo(UPDATED_SEXO)
            .estadoCivil(UPDATED_ESTADO_CIVIL)
            .tipoSangre(UPDATED_TIPO_SANGRE)
            .pathImagen(UPDATED_PATH_IMAGEN);
        ClienteDTO clienteDTO = clienteMapper.toDto(updatedCliente);

        restClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clienteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clienteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClienteToMatchAllProperties(updatedCliente);
    }

    @Test
    @Transactional
    void putNonExistingCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cliente.setId(longCount.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clienteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cliente.setId(longCount.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cliente.setId(longCount.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClienteWithPatch() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cliente using partial update
        Cliente partialUpdatedCliente = new Cliente();
        partialUpdatedCliente.setId(cliente.getId());

        partialUpdatedCliente
            .nombres(UPDATED_NOMBRES)
            .nombreComercial(UPDATED_NOMBRE_COMERCIAL)
            .sexo(UPDATED_SEXO)
            .pathImagen(UPDATED_PATH_IMAGEN);

        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCliente.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCliente))
            )
            .andExpect(status().isOk());

        // Validate the Cliente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClienteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCliente, cliente), getPersistedCliente(cliente));
    }

    @Test
    @Transactional
    void fullUpdateClienteWithPatch() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cliente using partial update
        Cliente partialUpdatedCliente = new Cliente();
        partialUpdatedCliente.setId(cliente.getId());

        partialUpdatedCliente
            .noCia(UPDATED_NO_CIA)
            .dni(UPDATED_DNI)
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .nombreComercial(UPDATED_NOMBRE_COMERCIAL)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .sexo(UPDATED_SEXO)
            .estadoCivil(UPDATED_ESTADO_CIVIL)
            .tipoSangre(UPDATED_TIPO_SANGRE)
            .pathImagen(UPDATED_PATH_IMAGEN);

        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCliente.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCliente))
            )
            .andExpect(status().isOk());

        // Validate the Cliente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClienteUpdatableFieldsEquals(partialUpdatedCliente, getPersistedCliente(partialUpdatedCliente));
    }

    @Test
    @Transactional
    void patchNonExistingCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cliente.setId(longCount.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clienteDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cliente.setId(longCount.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cliente.setId(longCount.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(clienteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCliente() throws Exception {
        // Initialize the database
        insertedCliente = clienteRepository.saveAndFlush(cliente);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cliente
        restClienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, cliente.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return clienteRepository.count();
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

    protected Cliente getPersistedCliente(Cliente cliente) {
        return clienteRepository.findById(cliente.getId()).orElseThrow();
    }

    protected void assertPersistedClienteToMatchAllProperties(Cliente expectedCliente) {
        assertClienteAllPropertiesEquals(expectedCliente, getPersistedCliente(expectedCliente));
    }

    protected void assertPersistedClienteToMatchUpdatableProperties(Cliente expectedCliente) {
        assertClienteAllUpdatablePropertiesEquals(expectedCliente, getPersistedCliente(expectedCliente));
    }
}
