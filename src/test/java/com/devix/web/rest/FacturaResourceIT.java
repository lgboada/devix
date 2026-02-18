package com.devix.web.rest;

import static com.devix.domain.FacturaAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.Centro;
import com.devix.domain.Cliente;
import com.devix.domain.Factura;
import com.devix.repository.FacturaRepository;
import com.devix.service.dto.FacturaDTO;
import com.devix.service.mapper.FacturaMapper;
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
 * Integration tests for the {@link FacturaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FacturaResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_SERIE = "AAAAAAAAAA";
    private static final String UPDATED_SERIE = "BBBBBBBBBB";

    private static final String DEFAULT_NO_FISICO = "AAAAAAAAAA";
    private static final String UPDATED_NO_FISICO = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_SUBTOTAL = 0D;
    private static final Double UPDATED_SUBTOTAL = 1D;
    private static final Double SMALLER_SUBTOTAL = 0D - 1D;

    private static final Double DEFAULT_IMPUESTO = 0D;
    private static final Double UPDATED_IMPUESTO = 1D;
    private static final Double SMALLER_IMPUESTO = 0D - 1D;

    private static final Double DEFAULT_IMPUESTO_CERO = 0D;
    private static final Double UPDATED_IMPUESTO_CERO = 1D;
    private static final Double SMALLER_IMPUESTO_CERO = 0D - 1D;

    private static final Double DEFAULT_DESCUENTO = 0D;
    private static final Double UPDATED_DESCUENTO = 1D;
    private static final Double SMALLER_DESCUENTO = 0D - 1D;

    private static final Double DEFAULT_TOTAL = 0D;
    private static final Double UPDATED_TOTAL = 1D;
    private static final Double SMALLER_TOTAL = 0D - 1D;

    private static final Double DEFAULT_PORCENTAJE_IMPUESTO = 0D;
    private static final Double UPDATED_PORCENTAJE_IMPUESTO = 1D;
    private static final Double SMALLER_PORCENTAJE_IMPUESTO = 0D - 1D;

    private static final String DEFAULT_CEDULA = "AAAAAAAAAA";
    private static final String UPDATED_CEDULA = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/facturas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private FacturaMapper facturaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacturaMockMvc;

    private Factura factura;

    private Factura insertedFactura;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factura createEntity() {
        return new Factura()
            .noCia(DEFAULT_NO_CIA)
            .serie(DEFAULT_SERIE)
            .noFisico(DEFAULT_NO_FISICO)
            .fecha(DEFAULT_FECHA)
            .subtotal(DEFAULT_SUBTOTAL)
            .impuesto(DEFAULT_IMPUESTO)
            .impuestoCero(DEFAULT_IMPUESTO_CERO)
            .descuento(DEFAULT_DESCUENTO)
            .total(DEFAULT_TOTAL)
            .porcentajeImpuesto(DEFAULT_PORCENTAJE_IMPUESTO)
            .cedula(DEFAULT_CEDULA)
            .direccion(DEFAULT_DIRECCION)
            .email(DEFAULT_EMAIL)
            .estado(DEFAULT_ESTADO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factura createUpdatedEntity() {
        return new Factura()
            .noCia(UPDATED_NO_CIA)
            .serie(UPDATED_SERIE)
            .noFisico(UPDATED_NO_FISICO)
            .fecha(UPDATED_FECHA)
            .subtotal(UPDATED_SUBTOTAL)
            .impuesto(UPDATED_IMPUESTO)
            .impuestoCero(UPDATED_IMPUESTO_CERO)
            .descuento(UPDATED_DESCUENTO)
            .total(UPDATED_TOTAL)
            .porcentajeImpuesto(UPDATED_PORCENTAJE_IMPUESTO)
            .cedula(UPDATED_CEDULA)
            .direccion(UPDATED_DIRECCION)
            .email(UPDATED_EMAIL)
            .estado(UPDATED_ESTADO);
    }

    @BeforeEach
    void initTest() {
        factura = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFactura != null) {
            facturaRepository.delete(insertedFactura);
            insertedFactura = null;
        }
    }

    @Test
    @Transactional
    void createFactura() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);
        var returnedFacturaDTO = om.readValue(
            restFacturaMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FacturaDTO.class
        );

        // Validate the Factura in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFactura = facturaMapper.toEntity(returnedFacturaDTO);
        assertFacturaUpdatableFieldsEquals(returnedFactura, getPersistedFactura(returnedFactura));

        insertedFactura = returnedFactura;
    }

    @Test
    @Transactional
    void createFacturaWithExistingId() throws Exception {
        // Create the Factura with an existing ID
        factura.setId(1L);
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factura.setNoCia(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSerieIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factura.setSerie(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNoFisicoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factura.setNoFisico(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factura.setFecha(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubtotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factura.setSubtotal(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImpuestoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factura.setImpuesto(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImpuestoCeroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factura.setImpuestoCero(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescuentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factura.setDescuento(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factura.setTotal(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPorcentajeImpuestoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factura.setPorcentajeImpuesto(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCedulaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factura.setCedula(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDireccionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factura.setDireccion(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factura.setEmail(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factura.setEstado(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFacturas() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factura.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].serie").value(hasItem(DEFAULT_SERIE)))
            .andExpect(jsonPath("$.[*].noFisico").value(hasItem(DEFAULT_NO_FISICO)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(DEFAULT_SUBTOTAL)))
            .andExpect(jsonPath("$.[*].impuesto").value(hasItem(DEFAULT_IMPUESTO)))
            .andExpect(jsonPath("$.[*].impuestoCero").value(hasItem(DEFAULT_IMPUESTO_CERO)))
            .andExpect(jsonPath("$.[*].descuento").value(hasItem(DEFAULT_DESCUENTO)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.[*].porcentajeImpuesto").value(hasItem(DEFAULT_PORCENTAJE_IMPUESTO)))
            .andExpect(jsonPath("$.[*].cedula").value(hasItem(DEFAULT_CEDULA)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));
    }

    @Test
    @Transactional
    void getFactura() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get the factura
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL_ID, factura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factura.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.serie").value(DEFAULT_SERIE))
            .andExpect(jsonPath("$.noFisico").value(DEFAULT_NO_FISICO))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.subtotal").value(DEFAULT_SUBTOTAL))
            .andExpect(jsonPath("$.impuesto").value(DEFAULT_IMPUESTO))
            .andExpect(jsonPath("$.impuestoCero").value(DEFAULT_IMPUESTO_CERO))
            .andExpect(jsonPath("$.descuento").value(DEFAULT_DESCUENTO))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL))
            .andExpect(jsonPath("$.porcentajeImpuesto").value(DEFAULT_PORCENTAJE_IMPUESTO))
            .andExpect(jsonPath("$.cedula").value(DEFAULT_CEDULA))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO));
    }

    @Test
    @Transactional
    void getFacturasByIdFiltering() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        Long id = factura.getId();

        defaultFacturaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFacturaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFacturaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacturasByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where noCia equals to
        defaultFacturaFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllFacturasByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where noCia in
        defaultFacturaFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllFacturasByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where noCia is not null
        defaultFacturaFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where noCia is greater than or equal to
        defaultFacturaFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllFacturasByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where noCia is less than or equal to
        defaultFacturaFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllFacturasByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where noCia is less than
        defaultFacturaFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllFacturasByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where noCia is greater than
        defaultFacturaFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllFacturasBySerieIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where serie equals to
        defaultFacturaFiltering("serie.equals=" + DEFAULT_SERIE, "serie.equals=" + UPDATED_SERIE);
    }

    @Test
    @Transactional
    void getAllFacturasBySerieIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where serie in
        defaultFacturaFiltering("serie.in=" + DEFAULT_SERIE + "," + UPDATED_SERIE, "serie.in=" + UPDATED_SERIE);
    }

    @Test
    @Transactional
    void getAllFacturasBySerieIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where serie is not null
        defaultFacturaFiltering("serie.specified=true", "serie.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasBySerieContainsSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where serie contains
        defaultFacturaFiltering("serie.contains=" + DEFAULT_SERIE, "serie.contains=" + UPDATED_SERIE);
    }

    @Test
    @Transactional
    void getAllFacturasBySerieNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where serie does not contain
        defaultFacturaFiltering("serie.doesNotContain=" + UPDATED_SERIE, "serie.doesNotContain=" + DEFAULT_SERIE);
    }

    @Test
    @Transactional
    void getAllFacturasByNoFisicoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where noFisico equals to
        defaultFacturaFiltering("noFisico.equals=" + DEFAULT_NO_FISICO, "noFisico.equals=" + UPDATED_NO_FISICO);
    }

    @Test
    @Transactional
    void getAllFacturasByNoFisicoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where noFisico in
        defaultFacturaFiltering("noFisico.in=" + DEFAULT_NO_FISICO + "," + UPDATED_NO_FISICO, "noFisico.in=" + UPDATED_NO_FISICO);
    }

    @Test
    @Transactional
    void getAllFacturasByNoFisicoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where noFisico is not null
        defaultFacturaFiltering("noFisico.specified=true", "noFisico.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByNoFisicoContainsSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where noFisico contains
        defaultFacturaFiltering("noFisico.contains=" + DEFAULT_NO_FISICO, "noFisico.contains=" + UPDATED_NO_FISICO);
    }

    @Test
    @Transactional
    void getAllFacturasByNoFisicoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where noFisico does not contain
        defaultFacturaFiltering("noFisico.doesNotContain=" + UPDATED_NO_FISICO, "noFisico.doesNotContain=" + DEFAULT_NO_FISICO);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fecha equals to
        defaultFacturaFiltering("fecha.equals=" + DEFAULT_FECHA, "fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fecha in
        defaultFacturaFiltering("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA, "fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fecha is not null
        defaultFacturaFiltering("fecha.specified=true", "fecha.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasBySubtotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where subtotal equals to
        defaultFacturaFiltering("subtotal.equals=" + DEFAULT_SUBTOTAL, "subtotal.equals=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasBySubtotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where subtotal in
        defaultFacturaFiltering("subtotal.in=" + DEFAULT_SUBTOTAL + "," + UPDATED_SUBTOTAL, "subtotal.in=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasBySubtotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where subtotal is not null
        defaultFacturaFiltering("subtotal.specified=true", "subtotal.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasBySubtotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where subtotal is greater than or equal to
        defaultFacturaFiltering("subtotal.greaterThanOrEqual=" + DEFAULT_SUBTOTAL, "subtotal.greaterThanOrEqual=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasBySubtotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where subtotal is less than or equal to
        defaultFacturaFiltering("subtotal.lessThanOrEqual=" + DEFAULT_SUBTOTAL, "subtotal.lessThanOrEqual=" + SMALLER_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasBySubtotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where subtotal is less than
        defaultFacturaFiltering("subtotal.lessThan=" + UPDATED_SUBTOTAL, "subtotal.lessThan=" + DEFAULT_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasBySubtotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where subtotal is greater than
        defaultFacturaFiltering("subtotal.greaterThan=" + SMALLER_SUBTOTAL, "subtotal.greaterThan=" + DEFAULT_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasByImpuestoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where impuesto equals to
        defaultFacturaFiltering("impuesto.equals=" + DEFAULT_IMPUESTO, "impuesto.equals=" + UPDATED_IMPUESTO);
    }

    @Test
    @Transactional
    void getAllFacturasByImpuestoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where impuesto in
        defaultFacturaFiltering("impuesto.in=" + DEFAULT_IMPUESTO + "," + UPDATED_IMPUESTO, "impuesto.in=" + UPDATED_IMPUESTO);
    }

    @Test
    @Transactional
    void getAllFacturasByImpuestoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where impuesto is not null
        defaultFacturaFiltering("impuesto.specified=true", "impuesto.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByImpuestoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where impuesto is greater than or equal to
        defaultFacturaFiltering("impuesto.greaterThanOrEqual=" + DEFAULT_IMPUESTO, "impuesto.greaterThanOrEqual=" + UPDATED_IMPUESTO);
    }

    @Test
    @Transactional
    void getAllFacturasByImpuestoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where impuesto is less than or equal to
        defaultFacturaFiltering("impuesto.lessThanOrEqual=" + DEFAULT_IMPUESTO, "impuesto.lessThanOrEqual=" + SMALLER_IMPUESTO);
    }

    @Test
    @Transactional
    void getAllFacturasByImpuestoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where impuesto is less than
        defaultFacturaFiltering("impuesto.lessThan=" + UPDATED_IMPUESTO, "impuesto.lessThan=" + DEFAULT_IMPUESTO);
    }

    @Test
    @Transactional
    void getAllFacturasByImpuestoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where impuesto is greater than
        defaultFacturaFiltering("impuesto.greaterThan=" + SMALLER_IMPUESTO, "impuesto.greaterThan=" + DEFAULT_IMPUESTO);
    }

    @Test
    @Transactional
    void getAllFacturasByImpuestoCeroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where impuestoCero equals to
        defaultFacturaFiltering("impuestoCero.equals=" + DEFAULT_IMPUESTO_CERO, "impuestoCero.equals=" + UPDATED_IMPUESTO_CERO);
    }

    @Test
    @Transactional
    void getAllFacturasByImpuestoCeroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where impuestoCero in
        defaultFacturaFiltering(
            "impuestoCero.in=" + DEFAULT_IMPUESTO_CERO + "," + UPDATED_IMPUESTO_CERO,
            "impuestoCero.in=" + UPDATED_IMPUESTO_CERO
        );
    }

    @Test
    @Transactional
    void getAllFacturasByImpuestoCeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where impuestoCero is not null
        defaultFacturaFiltering("impuestoCero.specified=true", "impuestoCero.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByImpuestoCeroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where impuestoCero is greater than or equal to
        defaultFacturaFiltering(
            "impuestoCero.greaterThanOrEqual=" + DEFAULT_IMPUESTO_CERO,
            "impuestoCero.greaterThanOrEqual=" + UPDATED_IMPUESTO_CERO
        );
    }

    @Test
    @Transactional
    void getAllFacturasByImpuestoCeroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where impuestoCero is less than or equal to
        defaultFacturaFiltering(
            "impuestoCero.lessThanOrEqual=" + DEFAULT_IMPUESTO_CERO,
            "impuestoCero.lessThanOrEqual=" + SMALLER_IMPUESTO_CERO
        );
    }

    @Test
    @Transactional
    void getAllFacturasByImpuestoCeroIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where impuestoCero is less than
        defaultFacturaFiltering("impuestoCero.lessThan=" + UPDATED_IMPUESTO_CERO, "impuestoCero.lessThan=" + DEFAULT_IMPUESTO_CERO);
    }

    @Test
    @Transactional
    void getAllFacturasByImpuestoCeroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where impuestoCero is greater than
        defaultFacturaFiltering("impuestoCero.greaterThan=" + SMALLER_IMPUESTO_CERO, "impuestoCero.greaterThan=" + DEFAULT_IMPUESTO_CERO);
    }

    @Test
    @Transactional
    void getAllFacturasByDescuentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where descuento equals to
        defaultFacturaFiltering("descuento.equals=" + DEFAULT_DESCUENTO, "descuento.equals=" + UPDATED_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllFacturasByDescuentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where descuento in
        defaultFacturaFiltering("descuento.in=" + DEFAULT_DESCUENTO + "," + UPDATED_DESCUENTO, "descuento.in=" + UPDATED_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllFacturasByDescuentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where descuento is not null
        defaultFacturaFiltering("descuento.specified=true", "descuento.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByDescuentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where descuento is greater than or equal to
        defaultFacturaFiltering("descuento.greaterThanOrEqual=" + DEFAULT_DESCUENTO, "descuento.greaterThanOrEqual=" + UPDATED_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllFacturasByDescuentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where descuento is less than or equal to
        defaultFacturaFiltering("descuento.lessThanOrEqual=" + DEFAULT_DESCUENTO, "descuento.lessThanOrEqual=" + SMALLER_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllFacturasByDescuentoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where descuento is less than
        defaultFacturaFiltering("descuento.lessThan=" + UPDATED_DESCUENTO, "descuento.lessThan=" + DEFAULT_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllFacturasByDescuentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where descuento is greater than
        defaultFacturaFiltering("descuento.greaterThan=" + SMALLER_DESCUENTO, "descuento.greaterThan=" + DEFAULT_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where total equals to
        defaultFacturaFiltering("total.equals=" + DEFAULT_TOTAL, "total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where total in
        defaultFacturaFiltering("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL, "total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where total is not null
        defaultFacturaFiltering("total.specified=true", "total.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where total is greater than or equal to
        defaultFacturaFiltering("total.greaterThanOrEqual=" + DEFAULT_TOTAL, "total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where total is less than or equal to
        defaultFacturaFiltering("total.lessThanOrEqual=" + DEFAULT_TOTAL, "total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where total is less than
        defaultFacturaFiltering("total.lessThan=" + UPDATED_TOTAL, "total.lessThan=" + DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where total is greater than
        defaultFacturaFiltering("total.greaterThan=" + SMALLER_TOTAL, "total.greaterThan=" + DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasByPorcentajeImpuestoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where porcentajeImpuesto equals to
        defaultFacturaFiltering(
            "porcentajeImpuesto.equals=" + DEFAULT_PORCENTAJE_IMPUESTO,
            "porcentajeImpuesto.equals=" + UPDATED_PORCENTAJE_IMPUESTO
        );
    }

    @Test
    @Transactional
    void getAllFacturasByPorcentajeImpuestoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where porcentajeImpuesto in
        defaultFacturaFiltering(
            "porcentajeImpuesto.in=" + DEFAULT_PORCENTAJE_IMPUESTO + "," + UPDATED_PORCENTAJE_IMPUESTO,
            "porcentajeImpuesto.in=" + UPDATED_PORCENTAJE_IMPUESTO
        );
    }

    @Test
    @Transactional
    void getAllFacturasByPorcentajeImpuestoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where porcentajeImpuesto is not null
        defaultFacturaFiltering("porcentajeImpuesto.specified=true", "porcentajeImpuesto.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByPorcentajeImpuestoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where porcentajeImpuesto is greater than or equal to
        defaultFacturaFiltering(
            "porcentajeImpuesto.greaterThanOrEqual=" + DEFAULT_PORCENTAJE_IMPUESTO,
            "porcentajeImpuesto.greaterThanOrEqual=" + UPDATED_PORCENTAJE_IMPUESTO
        );
    }

    @Test
    @Transactional
    void getAllFacturasByPorcentajeImpuestoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where porcentajeImpuesto is less than or equal to
        defaultFacturaFiltering(
            "porcentajeImpuesto.lessThanOrEqual=" + DEFAULT_PORCENTAJE_IMPUESTO,
            "porcentajeImpuesto.lessThanOrEqual=" + SMALLER_PORCENTAJE_IMPUESTO
        );
    }

    @Test
    @Transactional
    void getAllFacturasByPorcentajeImpuestoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where porcentajeImpuesto is less than
        defaultFacturaFiltering(
            "porcentajeImpuesto.lessThan=" + UPDATED_PORCENTAJE_IMPUESTO,
            "porcentajeImpuesto.lessThan=" + DEFAULT_PORCENTAJE_IMPUESTO
        );
    }

    @Test
    @Transactional
    void getAllFacturasByPorcentajeImpuestoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where porcentajeImpuesto is greater than
        defaultFacturaFiltering(
            "porcentajeImpuesto.greaterThan=" + SMALLER_PORCENTAJE_IMPUESTO,
            "porcentajeImpuesto.greaterThan=" + DEFAULT_PORCENTAJE_IMPUESTO
        );
    }

    @Test
    @Transactional
    void getAllFacturasByCedulaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where cedula equals to
        defaultFacturaFiltering("cedula.equals=" + DEFAULT_CEDULA, "cedula.equals=" + UPDATED_CEDULA);
    }

    @Test
    @Transactional
    void getAllFacturasByCedulaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where cedula in
        defaultFacturaFiltering("cedula.in=" + DEFAULT_CEDULA + "," + UPDATED_CEDULA, "cedula.in=" + UPDATED_CEDULA);
    }

    @Test
    @Transactional
    void getAllFacturasByCedulaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where cedula is not null
        defaultFacturaFiltering("cedula.specified=true", "cedula.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByCedulaContainsSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where cedula contains
        defaultFacturaFiltering("cedula.contains=" + DEFAULT_CEDULA, "cedula.contains=" + UPDATED_CEDULA);
    }

    @Test
    @Transactional
    void getAllFacturasByCedulaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where cedula does not contain
        defaultFacturaFiltering("cedula.doesNotContain=" + UPDATED_CEDULA, "cedula.doesNotContain=" + DEFAULT_CEDULA);
    }

    @Test
    @Transactional
    void getAllFacturasByDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where direccion equals to
        defaultFacturaFiltering("direccion.equals=" + DEFAULT_DIRECCION, "direccion.equals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllFacturasByDireccionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where direccion in
        defaultFacturaFiltering("direccion.in=" + DEFAULT_DIRECCION + "," + UPDATED_DIRECCION, "direccion.in=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllFacturasByDireccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where direccion is not null
        defaultFacturaFiltering("direccion.specified=true", "direccion.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByDireccionContainsSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where direccion contains
        defaultFacturaFiltering("direccion.contains=" + DEFAULT_DIRECCION, "direccion.contains=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllFacturasByDireccionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where direccion does not contain
        defaultFacturaFiltering("direccion.doesNotContain=" + UPDATED_DIRECCION, "direccion.doesNotContain=" + DEFAULT_DIRECCION);
    }

    @Test
    @Transactional
    void getAllFacturasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where email equals to
        defaultFacturaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFacturasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where email in
        defaultFacturaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFacturasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where email is not null
        defaultFacturaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where email contains
        defaultFacturaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFacturasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where email does not contain
        defaultFacturaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllFacturasByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where estado equals to
        defaultFacturaFiltering("estado.equals=" + DEFAULT_ESTADO, "estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllFacturasByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where estado in
        defaultFacturaFiltering("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO, "estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllFacturasByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where estado is not null
        defaultFacturaFiltering("estado.specified=true", "estado.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByEstadoContainsSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where estado contains
        defaultFacturaFiltering("estado.contains=" + DEFAULT_ESTADO, "estado.contains=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllFacturasByEstadoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where estado does not contain
        defaultFacturaFiltering("estado.doesNotContain=" + UPDATED_ESTADO, "estado.doesNotContain=" + DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void getAllFacturasByCentroIsEqualToSomething() throws Exception {
        Centro centro;
        if (TestUtil.findAll(em, Centro.class).isEmpty()) {
            facturaRepository.saveAndFlush(factura);
            centro = CentroResourceIT.createEntity();
        } else {
            centro = TestUtil.findAll(em, Centro.class).get(0);
        }
        em.persist(centro);
        em.flush();
        factura.setCentro(centro);
        facturaRepository.saveAndFlush(factura);
        Long centroId = centro.getId();
        // Get all the facturaList where centro equals to centroId
        defaultFacturaShouldBeFound("centroId.equals=" + centroId);

        // Get all the facturaList where centro equals to (centroId + 1)
        defaultFacturaShouldNotBeFound("centroId.equals=" + (centroId + 1));
    }

    @Test
    @Transactional
    void getAllFacturasByClienteIsEqualToSomething() throws Exception {
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            facturaRepository.saveAndFlush(factura);
            cliente = ClienteResourceIT.createEntity();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        em.persist(cliente);
        em.flush();
        factura.setCliente(cliente);
        facturaRepository.saveAndFlush(factura);
        Long clienteId = cliente.getId();
        // Get all the facturaList where cliente equals to clienteId
        defaultFacturaShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the facturaList where cliente equals to (clienteId + 1)
        defaultFacturaShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    private void defaultFacturaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFacturaShouldBeFound(shouldBeFound);
        defaultFacturaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacturaShouldBeFound(String filter) throws Exception {
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factura.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].serie").value(hasItem(DEFAULT_SERIE)))
            .andExpect(jsonPath("$.[*].noFisico").value(hasItem(DEFAULT_NO_FISICO)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(DEFAULT_SUBTOTAL)))
            .andExpect(jsonPath("$.[*].impuesto").value(hasItem(DEFAULT_IMPUESTO)))
            .andExpect(jsonPath("$.[*].impuestoCero").value(hasItem(DEFAULT_IMPUESTO_CERO)))
            .andExpect(jsonPath("$.[*].descuento").value(hasItem(DEFAULT_DESCUENTO)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.[*].porcentajeImpuesto").value(hasItem(DEFAULT_PORCENTAJE_IMPUESTO)))
            .andExpect(jsonPath("$.[*].cedula").value(hasItem(DEFAULT_CEDULA)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));

        // Check, that the count call also returns 1
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacturaShouldNotBeFound(String filter) throws Exception {
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFactura() throws Exception {
        // Get the factura
        restFacturaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFactura() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factura
        Factura updatedFactura = facturaRepository.findById(factura.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFactura are not directly saved in db
        em.detach(updatedFactura);
        updatedFactura
            .noCia(UPDATED_NO_CIA)
            .serie(UPDATED_SERIE)
            .noFisico(UPDATED_NO_FISICO)
            .fecha(UPDATED_FECHA)
            .subtotal(UPDATED_SUBTOTAL)
            .impuesto(UPDATED_IMPUESTO)
            .impuestoCero(UPDATED_IMPUESTO_CERO)
            .descuento(UPDATED_DESCUENTO)
            .total(UPDATED_TOTAL)
            .porcentajeImpuesto(UPDATED_PORCENTAJE_IMPUESTO)
            .cedula(UPDATED_CEDULA)
            .direccion(UPDATED_DIRECCION)
            .email(UPDATED_EMAIL)
            .estado(UPDATED_ESTADO);
        FacturaDTO facturaDTO = facturaMapper.toDto(updatedFactura);

        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facturaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facturaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFacturaToMatchAllProperties(updatedFactura);
    }

    @Test
    @Transactional
    void putNonExistingFactura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factura.setId(longCount.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facturaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFactura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factura.setId(longCount.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFactura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factura.setId(longCount.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Factura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacturaWithPatch() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factura using partial update
        Factura partialUpdatedFactura = new Factura();
        partialUpdatedFactura.setId(factura.getId());

        partialUpdatedFactura
            .noCia(UPDATED_NO_CIA)
            .noFisico(UPDATED_NO_FISICO)
            .fecha(UPDATED_FECHA)
            .impuesto(UPDATED_IMPUESTO)
            .impuestoCero(UPDATED_IMPUESTO_CERO)
            .total(UPDATED_TOTAL)
            .porcentajeImpuesto(UPDATED_PORCENTAJE_IMPUESTO)
            .cedula(UPDATED_CEDULA);

        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactura.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactura))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacturaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFactura, factura), getPersistedFactura(factura));
    }

    @Test
    @Transactional
    void fullUpdateFacturaWithPatch() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factura using partial update
        Factura partialUpdatedFactura = new Factura();
        partialUpdatedFactura.setId(factura.getId());

        partialUpdatedFactura
            .noCia(UPDATED_NO_CIA)
            .serie(UPDATED_SERIE)
            .noFisico(UPDATED_NO_FISICO)
            .fecha(UPDATED_FECHA)
            .subtotal(UPDATED_SUBTOTAL)
            .impuesto(UPDATED_IMPUESTO)
            .impuestoCero(UPDATED_IMPUESTO_CERO)
            .descuento(UPDATED_DESCUENTO)
            .total(UPDATED_TOTAL)
            .porcentajeImpuesto(UPDATED_PORCENTAJE_IMPUESTO)
            .cedula(UPDATED_CEDULA)
            .direccion(UPDATED_DIRECCION)
            .email(UPDATED_EMAIL)
            .estado(UPDATED_ESTADO);

        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactura.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactura))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacturaUpdatableFieldsEquals(partialUpdatedFactura, getPersistedFactura(partialUpdatedFactura));
    }

    @Test
    @Transactional
    void patchNonExistingFactura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factura.setId(longCount.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facturaDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFactura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factura.setId(longCount.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFactura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factura.setId(longCount.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(facturaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Factura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFactura() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the factura
        restFacturaMockMvc
            .perform(delete(ENTITY_API_URL_ID, factura.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return facturaRepository.count();
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

    protected Factura getPersistedFactura(Factura factura) {
        return facturaRepository.findById(factura.getId()).orElseThrow();
    }

    protected void assertPersistedFacturaToMatchAllProperties(Factura expectedFactura) {
        assertFacturaAllPropertiesEquals(expectedFactura, getPersistedFactura(expectedFactura));
    }

    protected void assertPersistedFacturaToMatchUpdatableProperties(Factura expectedFactura) {
        assertFacturaAllUpdatablePropertiesEquals(expectedFactura, getPersistedFactura(expectedFactura));
    }
}
