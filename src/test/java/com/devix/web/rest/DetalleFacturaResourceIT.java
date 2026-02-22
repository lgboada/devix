package com.devix.web.rest;

import static com.devix.domain.DetalleFacturaAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.DetalleFactura;
import com.devix.domain.Factura;
import com.devix.domain.Producto;
import com.devix.repository.DetalleFacturaRepository;
import com.devix.service.dto.DetalleFacturaDTO;
import com.devix.service.mapper.DetalleFacturaMapper;
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
 * Integration tests for the {@link DetalleFacturaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DetalleFacturaResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;
    private static final Integer SMALLER_CANTIDAD = 1 - 1;

    private static final Double DEFAULT_PRECIO_UNITARIO = 0D;
    private static final Double UPDATED_PRECIO_UNITARIO = 1D;
    private static final Double SMALLER_PRECIO_UNITARIO = 0D - 1D;

    private static final Double DEFAULT_SUBTOTAL = 0D;
    private static final Double UPDATED_SUBTOTAL = 1D;
    private static final Double SMALLER_SUBTOTAL = 0D - 1D;

    private static final Double DEFAULT_DESCUENTO = 0D;
    private static final Double UPDATED_DESCUENTO = 1D;
    private static final Double SMALLER_DESCUENTO = 0D - 1D;

    private static final Double DEFAULT_IMPUESTO = 0D;
    private static final Double UPDATED_IMPUESTO = 1D;
    private static final Double SMALLER_IMPUESTO = 0D - 1D;

    private static final Double DEFAULT_TOTAL = 0D;
    private static final Double UPDATED_TOTAL = 1D;
    private static final Double SMALLER_TOTAL = 0D - 1D;

    private static final String ENTITY_API_URL = "/api/detalle-facturas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DetalleFacturaRepository detalleFacturaRepository;

    @Autowired
    private DetalleFacturaMapper detalleFacturaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDetalleFacturaMockMvc;

    private DetalleFactura detalleFactura;

    private DetalleFactura insertedDetalleFactura;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetalleFactura createEntity() {
        return new DetalleFactura()
            .noCia(DEFAULT_NO_CIA)
            .cantidad(DEFAULT_CANTIDAD)
            .precioUnitario(DEFAULT_PRECIO_UNITARIO)
            .subtotal(DEFAULT_SUBTOTAL)
            .descuento(DEFAULT_DESCUENTO)
            .impuesto(DEFAULT_IMPUESTO)
            .total(DEFAULT_TOTAL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetalleFactura createUpdatedEntity() {
        return new DetalleFactura()
            .noCia(UPDATED_NO_CIA)
            .cantidad(UPDATED_CANTIDAD)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .subtotal(UPDATED_SUBTOTAL)
            .descuento(UPDATED_DESCUENTO)
            .impuesto(UPDATED_IMPUESTO)
            .total(UPDATED_TOTAL);
    }

    @BeforeEach
    void initTest() {
        detalleFactura = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDetalleFactura != null) {
            detalleFacturaRepository.delete(insertedDetalleFactura);
            insertedDetalleFactura = null;
        }
    }

    @Test
    @Transactional
    void createDetalleFactura() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DetalleFactura
        DetalleFacturaDTO detalleFacturaDTO = detalleFacturaMapper.toDto(detalleFactura);
        var returnedDetalleFacturaDTO = om.readValue(
            restDetalleFacturaMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(detalleFacturaDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DetalleFacturaDTO.class
        );

        // Validate the DetalleFactura in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDetalleFactura = detalleFacturaMapper.toEntity(returnedDetalleFacturaDTO);
        assertDetalleFacturaUpdatableFieldsEquals(returnedDetalleFactura, getPersistedDetalleFactura(returnedDetalleFactura));

        insertedDetalleFactura = returnedDetalleFactura;
    }

    @Test
    @Transactional
    void createDetalleFacturaWithExistingId() throws Exception {
        // Create the DetalleFactura with an existing ID
        detalleFactura.setId(1L);
        DetalleFacturaDTO detalleFacturaDTO = detalleFacturaMapper.toDto(detalleFactura);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetalleFacturaMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleFacturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleFactura in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        detalleFactura.setNoCia(null);

        // Create the DetalleFactura, which fails.
        DetalleFacturaDTO detalleFacturaDTO = detalleFacturaMapper.toDto(detalleFactura);

        restDetalleFacturaMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleFacturaDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        detalleFactura.setCantidad(null);

        // Create the DetalleFactura, which fails.
        DetalleFacturaDTO detalleFacturaDTO = detalleFacturaMapper.toDto(detalleFactura);

        restDetalleFacturaMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleFacturaDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioUnitarioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        detalleFactura.setPrecioUnitario(null);

        // Create the DetalleFactura, which fails.
        DetalleFacturaDTO detalleFacturaDTO = detalleFacturaMapper.toDto(detalleFactura);

        restDetalleFacturaMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleFacturaDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubtotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        detalleFactura.setSubtotal(null);

        // Create the DetalleFactura, which fails.
        DetalleFacturaDTO detalleFacturaDTO = detalleFacturaMapper.toDto(detalleFactura);

        restDetalleFacturaMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleFacturaDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescuentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        detalleFactura.setDescuento(null);

        // Create the DetalleFactura, which fails.
        DetalleFacturaDTO detalleFacturaDTO = detalleFacturaMapper.toDto(detalleFactura);

        restDetalleFacturaMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleFacturaDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImpuestoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        detalleFactura.setImpuesto(null);

        // Create the DetalleFactura, which fails.
        DetalleFacturaDTO detalleFacturaDTO = detalleFacturaMapper.toDto(detalleFactura);

        restDetalleFacturaMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleFacturaDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        detalleFactura.setTotal(null);

        // Create the DetalleFactura, which fails.
        DetalleFacturaDTO detalleFacturaDTO = detalleFacturaMapper.toDto(detalleFactura);

        restDetalleFacturaMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleFacturaDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDetalleFacturas() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList
        restDetalleFacturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detalleFactura.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].precioUnitario").value(hasItem(DEFAULT_PRECIO_UNITARIO)))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(DEFAULT_SUBTOTAL)))
            .andExpect(jsonPath("$.[*].descuento").value(hasItem(DEFAULT_DESCUENTO)))
            .andExpect(jsonPath("$.[*].impuesto").value(hasItem(DEFAULT_IMPUESTO)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)));
    }

    @Test
    @Transactional
    void getDetalleFactura() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get the detalleFactura
        restDetalleFacturaMockMvc
            .perform(get(ENTITY_API_URL_ID, detalleFactura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detalleFactura.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.precioUnitario").value(DEFAULT_PRECIO_UNITARIO))
            .andExpect(jsonPath("$.subtotal").value(DEFAULT_SUBTOTAL))
            .andExpect(jsonPath("$.descuento").value(DEFAULT_DESCUENTO))
            .andExpect(jsonPath("$.impuesto").value(DEFAULT_IMPUESTO))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL));
    }

    @Test
    @Transactional
    void getDetalleFacturasByIdFiltering() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        Long id = detalleFactura.getId();

        defaultDetalleFacturaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDetalleFacturaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDetalleFacturaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where noCia equals to
        defaultDetalleFacturaFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where noCia in
        defaultDetalleFacturaFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where noCia is not null
        defaultDetalleFacturaFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where noCia is greater than or equal to
        defaultDetalleFacturaFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where noCia is less than or equal to
        defaultDetalleFacturaFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where noCia is less than
        defaultDetalleFacturaFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where noCia is greater than
        defaultDetalleFacturaFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByCantidadIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where cantidad equals to
        defaultDetalleFacturaFiltering("cantidad.equals=" + DEFAULT_CANTIDAD, "cantidad.equals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByCantidadIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where cantidad in
        defaultDetalleFacturaFiltering("cantidad.in=" + DEFAULT_CANTIDAD + "," + UPDATED_CANTIDAD, "cantidad.in=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByCantidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where cantidad is not null
        defaultDetalleFacturaFiltering("cantidad.specified=true", "cantidad.specified=false");
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByCantidadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where cantidad is greater than or equal to
        defaultDetalleFacturaFiltering(
            "cantidad.greaterThanOrEqual=" + DEFAULT_CANTIDAD,
            "cantidad.greaterThanOrEqual=" + UPDATED_CANTIDAD
        );
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByCantidadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where cantidad is less than or equal to
        defaultDetalleFacturaFiltering("cantidad.lessThanOrEqual=" + DEFAULT_CANTIDAD, "cantidad.lessThanOrEqual=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByCantidadIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where cantidad is less than
        defaultDetalleFacturaFiltering("cantidad.lessThan=" + UPDATED_CANTIDAD, "cantidad.lessThan=" + DEFAULT_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByCantidadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where cantidad is greater than
        defaultDetalleFacturaFiltering("cantidad.greaterThan=" + SMALLER_CANTIDAD, "cantidad.greaterThan=" + DEFAULT_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByPrecioUnitarioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where precioUnitario equals to
        defaultDetalleFacturaFiltering(
            "precioUnitario.equals=" + DEFAULT_PRECIO_UNITARIO,
            "precioUnitario.equals=" + UPDATED_PRECIO_UNITARIO
        );
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByPrecioUnitarioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where precioUnitario in
        defaultDetalleFacturaFiltering(
            "precioUnitario.in=" + DEFAULT_PRECIO_UNITARIO + "," + UPDATED_PRECIO_UNITARIO,
            "precioUnitario.in=" + UPDATED_PRECIO_UNITARIO
        );
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByPrecioUnitarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where precioUnitario is not null
        defaultDetalleFacturaFiltering("precioUnitario.specified=true", "precioUnitario.specified=false");
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByPrecioUnitarioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where precioUnitario is greater than or equal to
        defaultDetalleFacturaFiltering(
            "precioUnitario.greaterThanOrEqual=" + DEFAULT_PRECIO_UNITARIO,
            "precioUnitario.greaterThanOrEqual=" + UPDATED_PRECIO_UNITARIO
        );
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByPrecioUnitarioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where precioUnitario is less than or equal to
        defaultDetalleFacturaFiltering(
            "precioUnitario.lessThanOrEqual=" + DEFAULT_PRECIO_UNITARIO,
            "precioUnitario.lessThanOrEqual=" + SMALLER_PRECIO_UNITARIO
        );
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByPrecioUnitarioIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where precioUnitario is less than
        defaultDetalleFacturaFiltering(
            "precioUnitario.lessThan=" + UPDATED_PRECIO_UNITARIO,
            "precioUnitario.lessThan=" + DEFAULT_PRECIO_UNITARIO
        );
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByPrecioUnitarioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where precioUnitario is greater than
        defaultDetalleFacturaFiltering(
            "precioUnitario.greaterThan=" + SMALLER_PRECIO_UNITARIO,
            "precioUnitario.greaterThan=" + DEFAULT_PRECIO_UNITARIO
        );
    }

    @Test
    @Transactional
    void getAllDetalleFacturasBySubtotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where subtotal equals to
        defaultDetalleFacturaFiltering("subtotal.equals=" + DEFAULT_SUBTOTAL, "subtotal.equals=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasBySubtotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where subtotal in
        defaultDetalleFacturaFiltering("subtotal.in=" + DEFAULT_SUBTOTAL + "," + UPDATED_SUBTOTAL, "subtotal.in=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasBySubtotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where subtotal is not null
        defaultDetalleFacturaFiltering("subtotal.specified=true", "subtotal.specified=false");
    }

    @Test
    @Transactional
    void getAllDetalleFacturasBySubtotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where subtotal is greater than or equal to
        defaultDetalleFacturaFiltering(
            "subtotal.greaterThanOrEqual=" + DEFAULT_SUBTOTAL,
            "subtotal.greaterThanOrEqual=" + UPDATED_SUBTOTAL
        );
    }

    @Test
    @Transactional
    void getAllDetalleFacturasBySubtotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where subtotal is less than or equal to
        defaultDetalleFacturaFiltering("subtotal.lessThanOrEqual=" + DEFAULT_SUBTOTAL, "subtotal.lessThanOrEqual=" + SMALLER_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasBySubtotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where subtotal is less than
        defaultDetalleFacturaFiltering("subtotal.lessThan=" + UPDATED_SUBTOTAL, "subtotal.lessThan=" + DEFAULT_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasBySubtotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where subtotal is greater than
        defaultDetalleFacturaFiltering("subtotal.greaterThan=" + SMALLER_SUBTOTAL, "subtotal.greaterThan=" + DEFAULT_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByDescuentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where descuento equals to
        defaultDetalleFacturaFiltering("descuento.equals=" + DEFAULT_DESCUENTO, "descuento.equals=" + UPDATED_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByDescuentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where descuento in
        defaultDetalleFacturaFiltering("descuento.in=" + DEFAULT_DESCUENTO + "," + UPDATED_DESCUENTO, "descuento.in=" + UPDATED_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByDescuentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where descuento is not null
        defaultDetalleFacturaFiltering("descuento.specified=true", "descuento.specified=false");
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByDescuentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where descuento is greater than or equal to
        defaultDetalleFacturaFiltering(
            "descuento.greaterThanOrEqual=" + DEFAULT_DESCUENTO,
            "descuento.greaterThanOrEqual=" + UPDATED_DESCUENTO
        );
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByDescuentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where descuento is less than or equal to
        defaultDetalleFacturaFiltering("descuento.lessThanOrEqual=" + DEFAULT_DESCUENTO, "descuento.lessThanOrEqual=" + SMALLER_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByDescuentoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where descuento is less than
        defaultDetalleFacturaFiltering("descuento.lessThan=" + UPDATED_DESCUENTO, "descuento.lessThan=" + DEFAULT_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByDescuentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where descuento is greater than
        defaultDetalleFacturaFiltering("descuento.greaterThan=" + SMALLER_DESCUENTO, "descuento.greaterThan=" + DEFAULT_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByImpuestoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where impuesto equals to
        defaultDetalleFacturaFiltering("impuesto.equals=" + DEFAULT_IMPUESTO, "impuesto.equals=" + UPDATED_IMPUESTO);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByImpuestoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where impuesto in
        defaultDetalleFacturaFiltering("impuesto.in=" + DEFAULT_IMPUESTO + "," + UPDATED_IMPUESTO, "impuesto.in=" + UPDATED_IMPUESTO);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByImpuestoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where impuesto is not null
        defaultDetalleFacturaFiltering("impuesto.specified=true", "impuesto.specified=false");
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByImpuestoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where impuesto is greater than or equal to
        defaultDetalleFacturaFiltering(
            "impuesto.greaterThanOrEqual=" + DEFAULT_IMPUESTO,
            "impuesto.greaterThanOrEqual=" + UPDATED_IMPUESTO
        );
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByImpuestoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where impuesto is less than or equal to
        defaultDetalleFacturaFiltering("impuesto.lessThanOrEqual=" + DEFAULT_IMPUESTO, "impuesto.lessThanOrEqual=" + SMALLER_IMPUESTO);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByImpuestoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where impuesto is less than
        defaultDetalleFacturaFiltering("impuesto.lessThan=" + UPDATED_IMPUESTO, "impuesto.lessThan=" + DEFAULT_IMPUESTO);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByImpuestoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where impuesto is greater than
        defaultDetalleFacturaFiltering("impuesto.greaterThan=" + SMALLER_IMPUESTO, "impuesto.greaterThan=" + DEFAULT_IMPUESTO);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where total equals to
        defaultDetalleFacturaFiltering("total.equals=" + DEFAULT_TOTAL, "total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where total in
        defaultDetalleFacturaFiltering("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL, "total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where total is not null
        defaultDetalleFacturaFiltering("total.specified=true", "total.specified=false");
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where total is greater than or equal to
        defaultDetalleFacturaFiltering("total.greaterThanOrEqual=" + DEFAULT_TOTAL, "total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where total is less than or equal to
        defaultDetalleFacturaFiltering("total.lessThanOrEqual=" + DEFAULT_TOTAL, "total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where total is less than
        defaultDetalleFacturaFiltering("total.lessThan=" + UPDATED_TOTAL, "total.lessThan=" + DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        // Get all the detalleFacturaList where total is greater than
        defaultDetalleFacturaFiltering("total.greaterThan=" + SMALLER_TOTAL, "total.greaterThan=" + DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByFacturaIsEqualToSomething() throws Exception {
        Factura factura;
        if (TestUtil.findAll(em, Factura.class).isEmpty()) {
            detalleFacturaRepository.saveAndFlush(detalleFactura);
            factura = FacturaResourceIT.createEntity();
        } else {
            factura = TestUtil.findAll(em, Factura.class).get(0);
        }
        em.persist(factura);
        em.flush();
        detalleFactura.setFactura(factura);
        detalleFacturaRepository.saveAndFlush(detalleFactura);
        Long facturaId = factura.getId();
        // Get all the detalleFacturaList where factura equals to facturaId
        defaultDetalleFacturaShouldBeFound("facturaId.equals=" + facturaId);

        // Get all the detalleFacturaList where factura equals to (facturaId + 1)
        defaultDetalleFacturaShouldNotBeFound("facturaId.equals=" + (facturaId + 1));
    }

    @Test
    @Transactional
    void getAllDetalleFacturasByProductoIsEqualToSomething() throws Exception {
        Producto producto;
        if (TestUtil.findAll(em, Producto.class).isEmpty()) {
            detalleFacturaRepository.saveAndFlush(detalleFactura);
            producto = ProductoResourceIT.createEntity();
        } else {
            producto = TestUtil.findAll(em, Producto.class).get(0);
        }
        em.persist(producto);
        em.flush();
        detalleFactura.setProducto(producto);
        detalleFacturaRepository.saveAndFlush(detalleFactura);
        Long productoId = producto.getId();
        // Get all the detalleFacturaList where producto equals to productoId
        defaultDetalleFacturaShouldBeFound("productoId.equals=" + productoId);

        // Get all the detalleFacturaList where producto equals to (productoId + 1)
        defaultDetalleFacturaShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    private void defaultDetalleFacturaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDetalleFacturaShouldBeFound(shouldBeFound);
        defaultDetalleFacturaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDetalleFacturaShouldBeFound(String filter) throws Exception {
        restDetalleFacturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detalleFactura.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].precioUnitario").value(hasItem(DEFAULT_PRECIO_UNITARIO)))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(DEFAULT_SUBTOTAL)))
            .andExpect(jsonPath("$.[*].descuento").value(hasItem(DEFAULT_DESCUENTO)))
            .andExpect(jsonPath("$.[*].impuesto").value(hasItem(DEFAULT_IMPUESTO)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)));

        // Check, that the count call also returns 1
        restDetalleFacturaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDetalleFacturaShouldNotBeFound(String filter) throws Exception {
        restDetalleFacturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDetalleFacturaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDetalleFactura() throws Exception {
        // Get the detalleFactura
        restDetalleFacturaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDetalleFactura() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the detalleFactura
        DetalleFactura updatedDetalleFactura = detalleFacturaRepository.findById(detalleFactura.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDetalleFactura are not directly saved in db
        em.detach(updatedDetalleFactura);
        updatedDetalleFactura
            .noCia(UPDATED_NO_CIA)
            .cantidad(UPDATED_CANTIDAD)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .subtotal(UPDATED_SUBTOTAL)
            .descuento(UPDATED_DESCUENTO)
            .impuesto(UPDATED_IMPUESTO)
            .total(UPDATED_TOTAL);
        DetalleFacturaDTO detalleFacturaDTO = detalleFacturaMapper.toDto(updatedDetalleFactura);

        restDetalleFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detalleFacturaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(detalleFacturaDTO))
            )
            .andExpect(status().isOk());

        // Validate the DetalleFactura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDetalleFacturaToMatchAllProperties(updatedDetalleFactura);
    }

    @Test
    @Transactional
    void putNonExistingDetalleFactura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detalleFactura.setId(longCount.incrementAndGet());

        // Create the DetalleFactura
        DetalleFacturaDTO detalleFacturaDTO = detalleFacturaMapper.toDto(detalleFactura);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetalleFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detalleFacturaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(detalleFacturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleFactura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDetalleFactura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detalleFactura.setId(longCount.incrementAndGet());

        // Create the DetalleFactura
        DetalleFacturaDTO detalleFacturaDTO = detalleFacturaMapper.toDto(detalleFactura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(detalleFacturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleFactura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDetalleFactura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detalleFactura.setId(longCount.incrementAndGet());

        // Create the DetalleFactura
        DetalleFacturaDTO detalleFacturaDTO = detalleFacturaMapper.toDto(detalleFactura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleFacturaMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleFacturaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetalleFactura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDetalleFacturaWithPatch() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the detalleFactura using partial update
        DetalleFactura partialUpdatedDetalleFactura = new DetalleFactura();
        partialUpdatedDetalleFactura.setId(detalleFactura.getId());

        partialUpdatedDetalleFactura
            .noCia(UPDATED_NO_CIA)
            .cantidad(UPDATED_CANTIDAD)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .impuesto(UPDATED_IMPUESTO);

        restDetalleFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetalleFactura.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDetalleFactura))
            )
            .andExpect(status().isOk());

        // Validate the DetalleFactura in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDetalleFacturaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDetalleFactura, detalleFactura),
            getPersistedDetalleFactura(detalleFactura)
        );
    }

    @Test
    @Transactional
    void fullUpdateDetalleFacturaWithPatch() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the detalleFactura using partial update
        DetalleFactura partialUpdatedDetalleFactura = new DetalleFactura();
        partialUpdatedDetalleFactura.setId(detalleFactura.getId());

        partialUpdatedDetalleFactura
            .noCia(UPDATED_NO_CIA)
            .cantidad(UPDATED_CANTIDAD)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .subtotal(UPDATED_SUBTOTAL)
            .descuento(UPDATED_DESCUENTO)
            .impuesto(UPDATED_IMPUESTO)
            .total(UPDATED_TOTAL);

        restDetalleFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetalleFactura.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDetalleFactura))
            )
            .andExpect(status().isOk());

        // Validate the DetalleFactura in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDetalleFacturaUpdatableFieldsEquals(partialUpdatedDetalleFactura, getPersistedDetalleFactura(partialUpdatedDetalleFactura));
    }

    @Test
    @Transactional
    void patchNonExistingDetalleFactura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detalleFactura.setId(longCount.incrementAndGet());

        // Create the DetalleFactura
        DetalleFacturaDTO detalleFacturaDTO = detalleFacturaMapper.toDto(detalleFactura);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetalleFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, detalleFacturaDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(detalleFacturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleFactura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDetalleFactura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detalleFactura.setId(longCount.incrementAndGet());

        // Create the DetalleFactura
        DetalleFacturaDTO detalleFacturaDTO = detalleFacturaMapper.toDto(detalleFactura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(detalleFacturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleFactura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDetalleFactura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detalleFactura.setId(longCount.incrementAndGet());

        // Create the DetalleFactura
        DetalleFacturaDTO detalleFacturaDTO = detalleFacturaMapper.toDto(detalleFactura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(detalleFacturaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetalleFactura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDetalleFactura() throws Exception {
        // Initialize the database
        insertedDetalleFactura = detalleFacturaRepository.saveAndFlush(detalleFactura);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the detalleFactura
        restDetalleFacturaMockMvc
            .perform(delete(ENTITY_API_URL_ID, detalleFactura.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return detalleFacturaRepository.count();
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

    protected DetalleFactura getPersistedDetalleFactura(DetalleFactura detalleFactura) {
        return detalleFacturaRepository.findById(detalleFactura.getId()).orElseThrow();
    }

    protected void assertPersistedDetalleFacturaToMatchAllProperties(DetalleFactura expectedDetalleFactura) {
        assertDetalleFacturaAllPropertiesEquals(expectedDetalleFactura, getPersistedDetalleFactura(expectedDetalleFactura));
    }

    protected void assertPersistedDetalleFacturaToMatchUpdatableProperties(DetalleFactura expectedDetalleFactura) {
        assertDetalleFacturaAllUpdatablePropertiesEquals(expectedDetalleFactura, getPersistedDetalleFactura(expectedDetalleFactura));
    }
}
