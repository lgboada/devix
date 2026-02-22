package com.devix.web.rest;

import static com.devix.domain.CatalogoAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.Catalogo;
import com.devix.domain.TipoCatalogo;
import com.devix.repository.CatalogoRepository;
import com.devix.service.dto.CatalogoDTO;
import com.devix.service.mapper.CatalogoMapper;
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
 * Integration tests for the {@link CatalogoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CatalogoResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_DESCRIPCION_1 = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION_1 = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION_2 = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDEN = 1;
    private static final Integer UPDATED_ORDEN = 2;
    private static final Integer SMALLER_ORDEN = 1 - 1;

    private static final String DEFAULT_TEXTO_1 = "AAAAAAAAAA";
    private static final String UPDATED_TEXTO_1 = "BBBBBBBBBB";

    private static final String DEFAULT_TEXTO_2 = "AAAAAAAAAA";
    private static final String UPDATED_TEXTO_2 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/catalogos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CatalogoRepository catalogoRepository;

    @Autowired
    private CatalogoMapper catalogoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatalogoMockMvc;

    private Catalogo catalogo;

    private Catalogo insertedCatalogo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catalogo createEntity() {
        return new Catalogo()
            .noCia(DEFAULT_NO_CIA)
            .descripcion1(DEFAULT_DESCRIPCION_1)
            .descripcion2(DEFAULT_DESCRIPCION_2)
            .estado(DEFAULT_ESTADO)
            .orden(DEFAULT_ORDEN)
            .texto1(DEFAULT_TEXTO_1)
            .texto2(DEFAULT_TEXTO_2);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catalogo createUpdatedEntity() {
        return new Catalogo()
            .noCia(UPDATED_NO_CIA)
            .descripcion1(UPDATED_DESCRIPCION_1)
            .descripcion2(UPDATED_DESCRIPCION_2)
            .estado(UPDATED_ESTADO)
            .orden(UPDATED_ORDEN)
            .texto1(UPDATED_TEXTO_1)
            .texto2(UPDATED_TEXTO_2);
    }

    @BeforeEach
    void initTest() {
        catalogo = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCatalogo != null) {
            catalogoRepository.delete(insertedCatalogo);
            insertedCatalogo = null;
        }
    }

    @Test
    @Transactional
    void createCatalogo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Catalogo
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);
        var returnedCatalogoDTO = om.readValue(
            restCatalogoMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(catalogoDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CatalogoDTO.class
        );

        // Validate the Catalogo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCatalogo = catalogoMapper.toEntity(returnedCatalogoDTO);
        assertCatalogoUpdatableFieldsEquals(returnedCatalogo, getPersistedCatalogo(returnedCatalogo));

        insertedCatalogo = returnedCatalogo;
    }

    @Test
    @Transactional
    void createCatalogoWithExistingId() throws Exception {
        // Create the Catalogo with an existing ID
        catalogo.setId(1L);
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatalogoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(catalogoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Catalogo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        catalogo.setNoCia(null);

        // Create the Catalogo, which fails.
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);

        restCatalogoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(catalogoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcion1IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        catalogo.setDescripcion1(null);

        // Create the Catalogo, which fails.
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);

        restCatalogoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(catalogoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCatalogos() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList
        restCatalogoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogo.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion1").value(hasItem(DEFAULT_DESCRIPCION_1)))
            .andExpect(jsonPath("$.[*].descripcion2").value(hasItem(DEFAULT_DESCRIPCION_2)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].orden").value(hasItem(DEFAULT_ORDEN)))
            .andExpect(jsonPath("$.[*].texto1").value(hasItem(DEFAULT_TEXTO_1)))
            .andExpect(jsonPath("$.[*].texto2").value(hasItem(DEFAULT_TEXTO_2)));
    }

    @Test
    @Transactional
    void getCatalogo() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get the catalogo
        restCatalogoMockMvc
            .perform(get(ENTITY_API_URL_ID, catalogo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catalogo.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.descripcion1").value(DEFAULT_DESCRIPCION_1))
            .andExpect(jsonPath("$.descripcion2").value(DEFAULT_DESCRIPCION_2))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.orden").value(DEFAULT_ORDEN))
            .andExpect(jsonPath("$.texto1").value(DEFAULT_TEXTO_1))
            .andExpect(jsonPath("$.texto2").value(DEFAULT_TEXTO_2));
    }

    @Test
    @Transactional
    void getCatalogosByIdFiltering() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        Long id = catalogo.getId();

        defaultCatalogoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCatalogoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCatalogoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCatalogosByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where noCia equals to
        defaultCatalogoFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCatalogosByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where noCia in
        defaultCatalogoFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCatalogosByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where noCia is not null
        defaultCatalogoFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogosByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where noCia is greater than or equal to
        defaultCatalogoFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCatalogosByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where noCia is less than or equal to
        defaultCatalogoFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCatalogosByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where noCia is less than
        defaultCatalogoFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCatalogosByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where noCia is greater than
        defaultCatalogoFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCatalogosByDescripcion1IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where descripcion1 equals to
        defaultCatalogoFiltering("descripcion1.equals=" + DEFAULT_DESCRIPCION_1, "descripcion1.equals=" + UPDATED_DESCRIPCION_1);
    }

    @Test
    @Transactional
    void getAllCatalogosByDescripcion1IsInShouldWork() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where descripcion1 in
        defaultCatalogoFiltering(
            "descripcion1.in=" + DEFAULT_DESCRIPCION_1 + "," + UPDATED_DESCRIPCION_1,
            "descripcion1.in=" + UPDATED_DESCRIPCION_1
        );
    }

    @Test
    @Transactional
    void getAllCatalogosByDescripcion1IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where descripcion1 is not null
        defaultCatalogoFiltering("descripcion1.specified=true", "descripcion1.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogosByDescripcion1ContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where descripcion1 contains
        defaultCatalogoFiltering("descripcion1.contains=" + DEFAULT_DESCRIPCION_1, "descripcion1.contains=" + UPDATED_DESCRIPCION_1);
    }

    @Test
    @Transactional
    void getAllCatalogosByDescripcion1NotContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where descripcion1 does not contain
        defaultCatalogoFiltering(
            "descripcion1.doesNotContain=" + UPDATED_DESCRIPCION_1,
            "descripcion1.doesNotContain=" + DEFAULT_DESCRIPCION_1
        );
    }

    @Test
    @Transactional
    void getAllCatalogosByDescripcion2IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where descripcion2 equals to
        defaultCatalogoFiltering("descripcion2.equals=" + DEFAULT_DESCRIPCION_2, "descripcion2.equals=" + UPDATED_DESCRIPCION_2);
    }

    @Test
    @Transactional
    void getAllCatalogosByDescripcion2IsInShouldWork() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where descripcion2 in
        defaultCatalogoFiltering(
            "descripcion2.in=" + DEFAULT_DESCRIPCION_2 + "," + UPDATED_DESCRIPCION_2,
            "descripcion2.in=" + UPDATED_DESCRIPCION_2
        );
    }

    @Test
    @Transactional
    void getAllCatalogosByDescripcion2IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where descripcion2 is not null
        defaultCatalogoFiltering("descripcion2.specified=true", "descripcion2.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogosByDescripcion2ContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where descripcion2 contains
        defaultCatalogoFiltering("descripcion2.contains=" + DEFAULT_DESCRIPCION_2, "descripcion2.contains=" + UPDATED_DESCRIPCION_2);
    }

    @Test
    @Transactional
    void getAllCatalogosByDescripcion2NotContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where descripcion2 does not contain
        defaultCatalogoFiltering(
            "descripcion2.doesNotContain=" + UPDATED_DESCRIPCION_2,
            "descripcion2.doesNotContain=" + DEFAULT_DESCRIPCION_2
        );
    }

    @Test
    @Transactional
    void getAllCatalogosByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where estado equals to
        defaultCatalogoFiltering("estado.equals=" + DEFAULT_ESTADO, "estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllCatalogosByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where estado in
        defaultCatalogoFiltering("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO, "estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllCatalogosByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where estado is not null
        defaultCatalogoFiltering("estado.specified=true", "estado.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogosByEstadoContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where estado contains
        defaultCatalogoFiltering("estado.contains=" + DEFAULT_ESTADO, "estado.contains=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllCatalogosByEstadoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where estado does not contain
        defaultCatalogoFiltering("estado.doesNotContain=" + UPDATED_ESTADO, "estado.doesNotContain=" + DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void getAllCatalogosByOrdenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where orden equals to
        defaultCatalogoFiltering("orden.equals=" + DEFAULT_ORDEN, "orden.equals=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllCatalogosByOrdenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where orden in
        defaultCatalogoFiltering("orden.in=" + DEFAULT_ORDEN + "," + UPDATED_ORDEN, "orden.in=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllCatalogosByOrdenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where orden is not null
        defaultCatalogoFiltering("orden.specified=true", "orden.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogosByOrdenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where orden is greater than or equal to
        defaultCatalogoFiltering("orden.greaterThanOrEqual=" + DEFAULT_ORDEN, "orden.greaterThanOrEqual=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllCatalogosByOrdenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where orden is less than or equal to
        defaultCatalogoFiltering("orden.lessThanOrEqual=" + DEFAULT_ORDEN, "orden.lessThanOrEqual=" + SMALLER_ORDEN);
    }

    @Test
    @Transactional
    void getAllCatalogosByOrdenIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where orden is less than
        defaultCatalogoFiltering("orden.lessThan=" + UPDATED_ORDEN, "orden.lessThan=" + DEFAULT_ORDEN);
    }

    @Test
    @Transactional
    void getAllCatalogosByOrdenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where orden is greater than
        defaultCatalogoFiltering("orden.greaterThan=" + SMALLER_ORDEN, "orden.greaterThan=" + DEFAULT_ORDEN);
    }

    @Test
    @Transactional
    void getAllCatalogosByTexto1IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where texto1 equals to
        defaultCatalogoFiltering("texto1.equals=" + DEFAULT_TEXTO_1, "texto1.equals=" + UPDATED_TEXTO_1);
    }

    @Test
    @Transactional
    void getAllCatalogosByTexto1IsInShouldWork() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where texto1 in
        defaultCatalogoFiltering("texto1.in=" + DEFAULT_TEXTO_1 + "," + UPDATED_TEXTO_1, "texto1.in=" + UPDATED_TEXTO_1);
    }

    @Test
    @Transactional
    void getAllCatalogosByTexto1IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where texto1 is not null
        defaultCatalogoFiltering("texto1.specified=true", "texto1.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogosByTexto1ContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where texto1 contains
        defaultCatalogoFiltering("texto1.contains=" + DEFAULT_TEXTO_1, "texto1.contains=" + UPDATED_TEXTO_1);
    }

    @Test
    @Transactional
    void getAllCatalogosByTexto1NotContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where texto1 does not contain
        defaultCatalogoFiltering("texto1.doesNotContain=" + UPDATED_TEXTO_1, "texto1.doesNotContain=" + DEFAULT_TEXTO_1);
    }

    @Test
    @Transactional
    void getAllCatalogosByTexto2IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where texto2 equals to
        defaultCatalogoFiltering("texto2.equals=" + DEFAULT_TEXTO_2, "texto2.equals=" + UPDATED_TEXTO_2);
    }

    @Test
    @Transactional
    void getAllCatalogosByTexto2IsInShouldWork() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where texto2 in
        defaultCatalogoFiltering("texto2.in=" + DEFAULT_TEXTO_2 + "," + UPDATED_TEXTO_2, "texto2.in=" + UPDATED_TEXTO_2);
    }

    @Test
    @Transactional
    void getAllCatalogosByTexto2IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where texto2 is not null
        defaultCatalogoFiltering("texto2.specified=true", "texto2.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogosByTexto2ContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where texto2 contains
        defaultCatalogoFiltering("texto2.contains=" + DEFAULT_TEXTO_2, "texto2.contains=" + UPDATED_TEXTO_2);
    }

    @Test
    @Transactional
    void getAllCatalogosByTexto2NotContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList where texto2 does not contain
        defaultCatalogoFiltering("texto2.doesNotContain=" + UPDATED_TEXTO_2, "texto2.doesNotContain=" + DEFAULT_TEXTO_2);
    }

    @Test
    @Transactional
    void getAllCatalogosByTipoCatalogoIsEqualToSomething() throws Exception {
        TipoCatalogo tipoCatalogo;
        if (TestUtil.findAll(em, TipoCatalogo.class).isEmpty()) {
            catalogoRepository.saveAndFlush(catalogo);
            tipoCatalogo = TipoCatalogoResourceIT.createEntity();
        } else {
            tipoCatalogo = TestUtil.findAll(em, TipoCatalogo.class).get(0);
        }
        em.persist(tipoCatalogo);
        em.flush();
        catalogo.setTipoCatalogo(tipoCatalogo);
        catalogoRepository.saveAndFlush(catalogo);
        Long tipoCatalogoId = tipoCatalogo.getId();
        // Get all the catalogoList where tipoCatalogo equals to tipoCatalogoId
        defaultCatalogoShouldBeFound("tipoCatalogoId.equals=" + tipoCatalogoId);

        // Get all the catalogoList where tipoCatalogo equals to (tipoCatalogoId + 1)
        defaultCatalogoShouldNotBeFound("tipoCatalogoId.equals=" + (tipoCatalogoId + 1));
    }

    private void defaultCatalogoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCatalogoShouldBeFound(shouldBeFound);
        defaultCatalogoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCatalogoShouldBeFound(String filter) throws Exception {
        restCatalogoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogo.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion1").value(hasItem(DEFAULT_DESCRIPCION_1)))
            .andExpect(jsonPath("$.[*].descripcion2").value(hasItem(DEFAULT_DESCRIPCION_2)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].orden").value(hasItem(DEFAULT_ORDEN)))
            .andExpect(jsonPath("$.[*].texto1").value(hasItem(DEFAULT_TEXTO_1)))
            .andExpect(jsonPath("$.[*].texto2").value(hasItem(DEFAULT_TEXTO_2)));

        // Check, that the count call also returns 1
        restCatalogoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCatalogoShouldNotBeFound(String filter) throws Exception {
        restCatalogoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCatalogoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCatalogo() throws Exception {
        // Get the catalogo
        restCatalogoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCatalogo() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the catalogo
        Catalogo updatedCatalogo = catalogoRepository.findById(catalogo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCatalogo are not directly saved in db
        em.detach(updatedCatalogo);
        updatedCatalogo
            .noCia(UPDATED_NO_CIA)
            .descripcion1(UPDATED_DESCRIPCION_1)
            .descripcion2(UPDATED_DESCRIPCION_2)
            .estado(UPDATED_ESTADO)
            .orden(UPDATED_ORDEN)
            .texto1(UPDATED_TEXTO_1)
            .texto2(UPDATED_TEXTO_2);
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(updatedCatalogo);

        restCatalogoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(catalogoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Catalogo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCatalogoToMatchAllProperties(updatedCatalogo);
    }

    @Test
    @Transactional
    void putNonExistingCatalogo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogo.setId(longCount.incrementAndGet());

        // Create the Catalogo
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(catalogoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalogo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCatalogo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogo.setId(longCount.incrementAndGet());

        // Create the Catalogo
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(catalogoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalogo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCatalogo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogo.setId(longCount.incrementAndGet());

        // Create the Catalogo
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogoMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(catalogoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Catalogo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCatalogoWithPatch() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the catalogo using partial update
        Catalogo partialUpdatedCatalogo = new Catalogo();
        partialUpdatedCatalogo.setId(catalogo.getId());

        partialUpdatedCatalogo.noCia(UPDATED_NO_CIA).estado(UPDATED_ESTADO).orden(UPDATED_ORDEN);

        restCatalogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCatalogo))
            )
            .andExpect(status().isOk());

        // Validate the Catalogo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCatalogoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCatalogo, catalogo), getPersistedCatalogo(catalogo));
    }

    @Test
    @Transactional
    void fullUpdateCatalogoWithPatch() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the catalogo using partial update
        Catalogo partialUpdatedCatalogo = new Catalogo();
        partialUpdatedCatalogo.setId(catalogo.getId());

        partialUpdatedCatalogo
            .noCia(UPDATED_NO_CIA)
            .descripcion1(UPDATED_DESCRIPCION_1)
            .descripcion2(UPDATED_DESCRIPCION_2)
            .estado(UPDATED_ESTADO)
            .orden(UPDATED_ORDEN)
            .texto1(UPDATED_TEXTO_1)
            .texto2(UPDATED_TEXTO_2);

        restCatalogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCatalogo))
            )
            .andExpect(status().isOk());

        // Validate the Catalogo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCatalogoUpdatableFieldsEquals(partialUpdatedCatalogo, getPersistedCatalogo(partialUpdatedCatalogo));
    }

    @Test
    @Transactional
    void patchNonExistingCatalogo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogo.setId(longCount.incrementAndGet());

        // Create the Catalogo
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, catalogoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(catalogoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalogo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCatalogo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogo.setId(longCount.incrementAndGet());

        // Create the Catalogo
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(catalogoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalogo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCatalogo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogo.setId(longCount.incrementAndGet());

        // Create the Catalogo
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogoMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(catalogoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Catalogo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCatalogo() throws Exception {
        // Initialize the database
        insertedCatalogo = catalogoRepository.saveAndFlush(catalogo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the catalogo
        restCatalogoMockMvc
            .perform(delete(ENTITY_API_URL_ID, catalogo.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return catalogoRepository.count();
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

    protected Catalogo getPersistedCatalogo(Catalogo catalogo) {
        return catalogoRepository.findById(catalogo.getId()).orElseThrow();
    }

    protected void assertPersistedCatalogoToMatchAllProperties(Catalogo expectedCatalogo) {
        assertCatalogoAllPropertiesEquals(expectedCatalogo, getPersistedCatalogo(expectedCatalogo));
    }

    protected void assertPersistedCatalogoToMatchUpdatableProperties(Catalogo expectedCatalogo) {
        assertCatalogoAllUpdatablePropertiesEquals(expectedCatalogo, getPersistedCatalogo(expectedCatalogo));
    }
}
