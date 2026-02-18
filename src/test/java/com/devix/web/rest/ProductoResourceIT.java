package com.devix.web.rest;

import static com.devix.domain.ProductoAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static com.devix.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.Modelo;
import com.devix.domain.Producto;
import com.devix.domain.Proveedor;
import com.devix.domain.TipoProducto;
import com.devix.repository.ProductoRepository;
import com.devix.service.dto.ProductoDTO;
import com.devix.service.mapper.ProductoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ProductoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductoResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRECIO = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRECIO = new BigDecimal(1);
    private static final BigDecimal SMALLER_PRECIO = new BigDecimal(0 - 1);

    private static final Integer DEFAULT_STOCK = 0;
    private static final Integer UPDATED_STOCK = 1;
    private static final Integer SMALLER_STOCK = 0 - 1;

    private static final String DEFAULT_PATH_IMAGEN = "AAAAAAAAAA";
    private static final String UPDATED_PATH_IMAGEN = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/productos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoMapper productoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductoMockMvc;

    private Producto producto;

    private Producto insertedProducto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producto createEntity() {
        return new Producto()
            .noCia(DEFAULT_NO_CIA)
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .precio(DEFAULT_PRECIO)
            .stock(DEFAULT_STOCK)
            .pathImagen(DEFAULT_PATH_IMAGEN)
            .codigo(DEFAULT_CODIGO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producto createUpdatedEntity() {
        return new Producto()
            .noCia(UPDATED_NO_CIA)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO)
            .stock(UPDATED_STOCK)
            .pathImagen(UPDATED_PATH_IMAGEN)
            .codigo(UPDATED_CODIGO);
    }

    @BeforeEach
    void initTest() {
        producto = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProducto != null) {
            productoRepository.delete(insertedProducto);
            insertedProducto = null;
        }
    }

    @Test
    @Transactional
    void createProducto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);
        var returnedProductoDTO = om.readValue(
            restProductoMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductoDTO.class
        );

        // Validate the Producto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProducto = productoMapper.toEntity(returnedProductoDTO);
        assertProductoUpdatableFieldsEquals(returnedProducto, getPersistedProducto(returnedProducto));

        insertedProducto = returnedProducto;
    }

    @Test
    @Transactional
    void createProductoWithExistingId() throws Exception {
        // Create the Producto with an existing ID
        producto.setId(1L);
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        producto.setNoCia(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        producto.setNombre(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        producto.setPrecio(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        producto.setStock(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPathImagenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        producto.setPathImagen(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        producto.setCodigo(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductos() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producto.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(sameNumber(DEFAULT_PRECIO))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].pathImagen").value(hasItem(DEFAULT_PATH_IMAGEN)))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)));
    }

    @Test
    @Transactional
    void getProducto() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get the producto
        restProductoMockMvc
            .perform(get(ENTITY_API_URL_ID, producto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(producto.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.precio").value(sameNumber(DEFAULT_PRECIO)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.pathImagen").value(DEFAULT_PATH_IMAGEN))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO));
    }

    @Test
    @Transactional
    void getProductosByIdFiltering() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        Long id = producto.getId();

        defaultProductoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductosByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where noCia equals to
        defaultProductoFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllProductosByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where noCia in
        defaultProductoFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllProductosByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where noCia is not null
        defaultProductoFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where noCia is greater than or equal to
        defaultProductoFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllProductosByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where noCia is less than or equal to
        defaultProductoFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllProductosByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where noCia is less than
        defaultProductoFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllProductosByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where noCia is greater than
        defaultProductoFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllProductosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre equals to
        defaultProductoFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProductosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre in
        defaultProductoFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProductosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre is not null
        defaultProductoFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre contains
        defaultProductoFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProductosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre does not contain
        defaultProductoFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProductosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion equals to
        defaultProductoFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllProductosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion in
        defaultProductoFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllProductosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion is not null
        defaultProductoFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion contains
        defaultProductoFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllProductosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion does not contain
        defaultProductoFiltering("descripcion.doesNotContain=" + UPDATED_DESCRIPCION, "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precio equals to
        defaultProductoFiltering("precio.equals=" + DEFAULT_PRECIO, "precio.equals=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precio in
        defaultProductoFiltering("precio.in=" + DEFAULT_PRECIO + "," + UPDATED_PRECIO, "precio.in=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precio is not null
        defaultProductoFiltering("precio.specified=true", "precio.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByPrecioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precio is greater than or equal to
        defaultProductoFiltering("precio.greaterThanOrEqual=" + DEFAULT_PRECIO, "precio.greaterThanOrEqual=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precio is less than or equal to
        defaultProductoFiltering("precio.lessThanOrEqual=" + DEFAULT_PRECIO, "precio.lessThanOrEqual=" + SMALLER_PRECIO);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precio is less than
        defaultProductoFiltering("precio.lessThan=" + UPDATED_PRECIO, "precio.lessThan=" + DEFAULT_PRECIO);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precio is greater than
        defaultProductoFiltering("precio.greaterThan=" + SMALLER_PRECIO, "precio.greaterThan=" + DEFAULT_PRECIO);
    }

    @Test
    @Transactional
    void getAllProductosByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where stock equals to
        defaultProductoFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductosByStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where stock in
        defaultProductoFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductosByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where stock is not null
        defaultProductoFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where stock is greater than or equal to
        defaultProductoFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductosByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where stock is less than or equal to
        defaultProductoFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllProductosByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where stock is less than
        defaultProductoFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllProductosByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where stock is greater than
        defaultProductoFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllProductosByPathImagenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where pathImagen equals to
        defaultProductoFiltering("pathImagen.equals=" + DEFAULT_PATH_IMAGEN, "pathImagen.equals=" + UPDATED_PATH_IMAGEN);
    }

    @Test
    @Transactional
    void getAllProductosByPathImagenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where pathImagen in
        defaultProductoFiltering(
            "pathImagen.in=" + DEFAULT_PATH_IMAGEN + "," + UPDATED_PATH_IMAGEN,
            "pathImagen.in=" + UPDATED_PATH_IMAGEN
        );
    }

    @Test
    @Transactional
    void getAllProductosByPathImagenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where pathImagen is not null
        defaultProductoFiltering("pathImagen.specified=true", "pathImagen.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByPathImagenContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where pathImagen contains
        defaultProductoFiltering("pathImagen.contains=" + DEFAULT_PATH_IMAGEN, "pathImagen.contains=" + UPDATED_PATH_IMAGEN);
    }

    @Test
    @Transactional
    void getAllProductosByPathImagenNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where pathImagen does not contain
        defaultProductoFiltering("pathImagen.doesNotContain=" + UPDATED_PATH_IMAGEN, "pathImagen.doesNotContain=" + DEFAULT_PATH_IMAGEN);
    }

    @Test
    @Transactional
    void getAllProductosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where codigo equals to
        defaultProductoFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllProductosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where codigo in
        defaultProductoFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllProductosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where codigo is not null
        defaultProductoFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where codigo contains
        defaultProductoFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllProductosByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where codigo does not contain
        defaultProductoFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllProductosByModeloIsEqualToSomething() throws Exception {
        Modelo modelo;
        if (TestUtil.findAll(em, Modelo.class).isEmpty()) {
            productoRepository.saveAndFlush(producto);
            modelo = ModeloResourceIT.createEntity();
        } else {
            modelo = TestUtil.findAll(em, Modelo.class).get(0);
        }
        em.persist(modelo);
        em.flush();
        producto.setModelo(modelo);
        productoRepository.saveAndFlush(producto);
        Long modeloId = modelo.getId();
        // Get all the productoList where modelo equals to modeloId
        defaultProductoShouldBeFound("modeloId.equals=" + modeloId);

        // Get all the productoList where modelo equals to (modeloId + 1)
        defaultProductoShouldNotBeFound("modeloId.equals=" + (modeloId + 1));
    }

    @Test
    @Transactional
    void getAllProductosByTipoProductoIsEqualToSomething() throws Exception {
        TipoProducto tipoProducto;
        if (TestUtil.findAll(em, TipoProducto.class).isEmpty()) {
            productoRepository.saveAndFlush(producto);
            tipoProducto = TipoProductoResourceIT.createEntity();
        } else {
            tipoProducto = TestUtil.findAll(em, TipoProducto.class).get(0);
        }
        em.persist(tipoProducto);
        em.flush();
        producto.setTipoProducto(tipoProducto);
        productoRepository.saveAndFlush(producto);
        Long tipoProductoId = tipoProducto.getId();
        // Get all the productoList where tipoProducto equals to tipoProductoId
        defaultProductoShouldBeFound("tipoProductoId.equals=" + tipoProductoId);

        // Get all the productoList where tipoProducto equals to (tipoProductoId + 1)
        defaultProductoShouldNotBeFound("tipoProductoId.equals=" + (tipoProductoId + 1));
    }

    @Test
    @Transactional
    void getAllProductosByProveedorIsEqualToSomething() throws Exception {
        Proveedor proveedor;
        if (TestUtil.findAll(em, Proveedor.class).isEmpty()) {
            productoRepository.saveAndFlush(producto);
            proveedor = ProveedorResourceIT.createEntity();
        } else {
            proveedor = TestUtil.findAll(em, Proveedor.class).get(0);
        }
        em.persist(proveedor);
        em.flush();
        producto.setProveedor(proveedor);
        productoRepository.saveAndFlush(producto);
        Long proveedorId = proveedor.getId();
        // Get all the productoList where proveedor equals to proveedorId
        defaultProductoShouldBeFound("proveedorId.equals=" + proveedorId);

        // Get all the productoList where proveedor equals to (proveedorId + 1)
        defaultProductoShouldNotBeFound("proveedorId.equals=" + (proveedorId + 1));
    }

    private void defaultProductoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductoShouldBeFound(shouldBeFound);
        defaultProductoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductoShouldBeFound(String filter) throws Exception {
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producto.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(sameNumber(DEFAULT_PRECIO))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].pathImagen").value(hasItem(DEFAULT_PATH_IMAGEN)))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)));

        // Check, that the count call also returns 1
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductoShouldNotBeFound(String filter) throws Exception {
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProducto() throws Exception {
        // Get the producto
        restProductoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProducto() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the producto
        Producto updatedProducto = productoRepository.findById(producto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProducto are not directly saved in db
        em.detach(updatedProducto);
        updatedProducto
            .noCia(UPDATED_NO_CIA)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO)
            .stock(UPDATED_STOCK)
            .pathImagen(UPDATED_PATH_IMAGEN)
            .codigo(UPDATED_CODIGO);
        ProductoDTO productoDTO = productoMapper.toDto(updatedProducto);

        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductoToMatchAllProperties(updatedProducto);
    }

    @Test
    @Transactional
    void putNonExistingProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductoWithPatch() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the producto using partial update
        Producto partialUpdatedProducto = new Producto();
        partialUpdatedProducto.setId(producto.getId());

        partialUpdatedProducto
            .noCia(UPDATED_NO_CIA)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .stock(UPDATED_STOCK)
            .codigo(UPDATED_CODIGO);

        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProducto))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProducto, producto), getPersistedProducto(producto));
    }

    @Test
    @Transactional
    void fullUpdateProductoWithPatch() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the producto using partial update
        Producto partialUpdatedProducto = new Producto();
        partialUpdatedProducto.setId(producto.getId());

        partialUpdatedProducto
            .noCia(UPDATED_NO_CIA)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO)
            .stock(UPDATED_STOCK)
            .pathImagen(UPDATED_PATH_IMAGEN)
            .codigo(UPDATED_CODIGO);

        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProducto))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductoUpdatableFieldsEquals(partialUpdatedProducto, getPersistedProducto(partialUpdatedProducto));
    }

    @Test
    @Transactional
    void patchNonExistingProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProducto() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the producto
        restProductoMockMvc
            .perform(delete(ENTITY_API_URL_ID, producto.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productoRepository.count();
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

    protected Producto getPersistedProducto(Producto producto) {
        return productoRepository.findById(producto.getId()).orElseThrow();
    }

    protected void assertPersistedProductoToMatchAllProperties(Producto expectedProducto) {
        assertProductoAllPropertiesEquals(expectedProducto, getPersistedProducto(expectedProducto));
    }

    protected void assertPersistedProductoToMatchUpdatableProperties(Producto expectedProducto) {
        assertProductoAllUpdatablePropertiesEquals(expectedProducto, getPersistedProducto(expectedProducto));
    }
}
