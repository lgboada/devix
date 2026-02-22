package com.devix.web.rest;

import static com.devix.domain.TipoCatalogoAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.TipoCatalogo;
import com.devix.repository.TipoCatalogoRepository;
import com.devix.service.dto.TipoCatalogoDTO;
import com.devix.service.mapper.TipoCatalogoMapper;
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
 * Integration tests for the {@link TipoCatalogoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoCatalogoResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORIA = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORIA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-catalogos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TipoCatalogoRepository tipoCatalogoRepository;

    @Autowired
    private TipoCatalogoMapper tipoCatalogoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoCatalogoMockMvc;

    private TipoCatalogo tipoCatalogo;

    private TipoCatalogo insertedTipoCatalogo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoCatalogo createEntity() {
        return new TipoCatalogo().noCia(DEFAULT_NO_CIA).descripcion(DEFAULT_DESCRIPCION).categoria(DEFAULT_CATEGORIA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoCatalogo createUpdatedEntity() {
        return new TipoCatalogo().noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION).categoria(UPDATED_CATEGORIA);
    }

    @BeforeEach
    void initTest() {
        tipoCatalogo = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTipoCatalogo != null) {
            tipoCatalogoRepository.delete(insertedTipoCatalogo);
            insertedTipoCatalogo = null;
        }
    }

    @Test
    @Transactional
    void createTipoCatalogo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TipoCatalogo
        TipoCatalogoDTO tipoCatalogoDTO = tipoCatalogoMapper.toDto(tipoCatalogo);
        var returnedTipoCatalogoDTO = om.readValue(
            restTipoCatalogoMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoCatalogoDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TipoCatalogoDTO.class
        );

        // Validate the TipoCatalogo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTipoCatalogo = tipoCatalogoMapper.toEntity(returnedTipoCatalogoDTO);
        assertTipoCatalogoUpdatableFieldsEquals(returnedTipoCatalogo, getPersistedTipoCatalogo(returnedTipoCatalogo));

        insertedTipoCatalogo = returnedTipoCatalogo;
    }

    @Test
    @Transactional
    void createTipoCatalogoWithExistingId() throws Exception {
        // Create the TipoCatalogo with an existing ID
        tipoCatalogo.setId(1L);
        TipoCatalogoDTO tipoCatalogoDTO = tipoCatalogoMapper.toDto(tipoCatalogo);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoCatalogoMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoCatalogoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoCatalogo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoCatalogo.setNoCia(null);

        // Create the TipoCatalogo, which fails.
        TipoCatalogoDTO tipoCatalogoDTO = tipoCatalogoMapper.toDto(tipoCatalogo);

        restTipoCatalogoMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoCatalogoDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoCatalogo.setDescripcion(null);

        // Create the TipoCatalogo, which fails.
        TipoCatalogoDTO tipoCatalogoDTO = tipoCatalogoMapper.toDto(tipoCatalogo);

        restTipoCatalogoMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoCatalogoDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoriaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoCatalogo.setCategoria(null);

        // Create the TipoCatalogo, which fails.
        TipoCatalogoDTO tipoCatalogoDTO = tipoCatalogoMapper.toDto(tipoCatalogo);

        restTipoCatalogoMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoCatalogoDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipoCatalogos() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        // Get all the tipoCatalogoList
        restTipoCatalogoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoCatalogo.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].categoria").value(hasItem(DEFAULT_CATEGORIA)));
    }

    @Test
    @Transactional
    void getTipoCatalogo() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        // Get the tipoCatalogo
        restTipoCatalogoMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoCatalogo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoCatalogo.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.categoria").value(DEFAULT_CATEGORIA));
    }

    @Test
    @Transactional
    void getTipoCatalogosByIdFiltering() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        Long id = tipoCatalogo.getId();

        defaultTipoCatalogoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTipoCatalogoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTipoCatalogoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTipoCatalogosByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        // Get all the tipoCatalogoList where noCia equals to
        defaultTipoCatalogoFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoCatalogosByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        // Get all the tipoCatalogoList where noCia in
        defaultTipoCatalogoFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoCatalogosByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        // Get all the tipoCatalogoList where noCia is not null
        defaultTipoCatalogoFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoCatalogosByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        // Get all the tipoCatalogoList where noCia is greater than or equal to
        defaultTipoCatalogoFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoCatalogosByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        // Get all the tipoCatalogoList where noCia is less than or equal to
        defaultTipoCatalogoFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoCatalogosByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        // Get all the tipoCatalogoList where noCia is less than
        defaultTipoCatalogoFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoCatalogosByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        // Get all the tipoCatalogoList where noCia is greater than
        defaultTipoCatalogoFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoCatalogosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        // Get all the tipoCatalogoList where descripcion equals to
        defaultTipoCatalogoFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTipoCatalogosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        // Get all the tipoCatalogoList where descripcion in
        defaultTipoCatalogoFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllTipoCatalogosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        // Get all the tipoCatalogoList where descripcion is not null
        defaultTipoCatalogoFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoCatalogosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        // Get all the tipoCatalogoList where descripcion contains
        defaultTipoCatalogoFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTipoCatalogosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        // Get all the tipoCatalogoList where descripcion does not contain
        defaultTipoCatalogoFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllTipoCatalogosByCategoriaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        // Get all the tipoCatalogoList where categoria equals to
        defaultTipoCatalogoFiltering("categoria.equals=" + DEFAULT_CATEGORIA, "categoria.equals=" + UPDATED_CATEGORIA);
    }

    @Test
    @Transactional
    void getAllTipoCatalogosByCategoriaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        // Get all the tipoCatalogoList where categoria in
        defaultTipoCatalogoFiltering("categoria.in=" + DEFAULT_CATEGORIA + "," + UPDATED_CATEGORIA, "categoria.in=" + UPDATED_CATEGORIA);
    }

    @Test
    @Transactional
    void getAllTipoCatalogosByCategoriaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        // Get all the tipoCatalogoList where categoria is not null
        defaultTipoCatalogoFiltering("categoria.specified=true", "categoria.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoCatalogosByCategoriaContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        // Get all the tipoCatalogoList where categoria contains
        defaultTipoCatalogoFiltering("categoria.contains=" + DEFAULT_CATEGORIA, "categoria.contains=" + UPDATED_CATEGORIA);
    }

    @Test
    @Transactional
    void getAllTipoCatalogosByCategoriaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        // Get all the tipoCatalogoList where categoria does not contain
        defaultTipoCatalogoFiltering("categoria.doesNotContain=" + UPDATED_CATEGORIA, "categoria.doesNotContain=" + DEFAULT_CATEGORIA);
    }

    private void defaultTipoCatalogoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTipoCatalogoShouldBeFound(shouldBeFound);
        defaultTipoCatalogoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTipoCatalogoShouldBeFound(String filter) throws Exception {
        restTipoCatalogoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoCatalogo.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].categoria").value(hasItem(DEFAULT_CATEGORIA)));

        // Check, that the count call also returns 1
        restTipoCatalogoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTipoCatalogoShouldNotBeFound(String filter) throws Exception {
        restTipoCatalogoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTipoCatalogoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTipoCatalogo() throws Exception {
        // Get the tipoCatalogo
        restTipoCatalogoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTipoCatalogo() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoCatalogo
        TipoCatalogo updatedTipoCatalogo = tipoCatalogoRepository.findById(tipoCatalogo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTipoCatalogo are not directly saved in db
        em.detach(updatedTipoCatalogo);
        updatedTipoCatalogo.noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION).categoria(UPDATED_CATEGORIA);
        TipoCatalogoDTO tipoCatalogoDTO = tipoCatalogoMapper.toDto(updatedTipoCatalogo);

        restTipoCatalogoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoCatalogoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoCatalogoDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoCatalogo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTipoCatalogoToMatchAllProperties(updatedTipoCatalogo);
    }

    @Test
    @Transactional
    void putNonExistingTipoCatalogo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoCatalogo.setId(longCount.incrementAndGet());

        // Create the TipoCatalogo
        TipoCatalogoDTO tipoCatalogoDTO = tipoCatalogoMapper.toDto(tipoCatalogo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoCatalogoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoCatalogoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoCatalogoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoCatalogo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoCatalogo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoCatalogo.setId(longCount.incrementAndGet());

        // Create the TipoCatalogo
        TipoCatalogoDTO tipoCatalogoDTO = tipoCatalogoMapper.toDto(tipoCatalogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoCatalogoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoCatalogoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoCatalogo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoCatalogo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoCatalogo.setId(longCount.incrementAndGet());

        // Create the TipoCatalogo
        TipoCatalogoDTO tipoCatalogoDTO = tipoCatalogoMapper.toDto(tipoCatalogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoCatalogoMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoCatalogoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoCatalogo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoCatalogoWithPatch() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoCatalogo using partial update
        TipoCatalogo partialUpdatedTipoCatalogo = new TipoCatalogo();
        partialUpdatedTipoCatalogo.setId(tipoCatalogo.getId());

        partialUpdatedTipoCatalogo.descripcion(UPDATED_DESCRIPCION);

        restTipoCatalogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoCatalogo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoCatalogo))
            )
            .andExpect(status().isOk());

        // Validate the TipoCatalogo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoCatalogoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTipoCatalogo, tipoCatalogo),
            getPersistedTipoCatalogo(tipoCatalogo)
        );
    }

    @Test
    @Transactional
    void fullUpdateTipoCatalogoWithPatch() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoCatalogo using partial update
        TipoCatalogo partialUpdatedTipoCatalogo = new TipoCatalogo();
        partialUpdatedTipoCatalogo.setId(tipoCatalogo.getId());

        partialUpdatedTipoCatalogo.noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION).categoria(UPDATED_CATEGORIA);

        restTipoCatalogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoCatalogo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoCatalogo))
            )
            .andExpect(status().isOk());

        // Validate the TipoCatalogo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoCatalogoUpdatableFieldsEquals(partialUpdatedTipoCatalogo, getPersistedTipoCatalogo(partialUpdatedTipoCatalogo));
    }

    @Test
    @Transactional
    void patchNonExistingTipoCatalogo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoCatalogo.setId(longCount.incrementAndGet());

        // Create the TipoCatalogo
        TipoCatalogoDTO tipoCatalogoDTO = tipoCatalogoMapper.toDto(tipoCatalogo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoCatalogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoCatalogoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoCatalogoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoCatalogo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoCatalogo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoCatalogo.setId(longCount.incrementAndGet());

        // Create the TipoCatalogo
        TipoCatalogoDTO tipoCatalogoDTO = tipoCatalogoMapper.toDto(tipoCatalogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoCatalogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoCatalogoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoCatalogo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoCatalogo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoCatalogo.setId(longCount.incrementAndGet());

        // Create the TipoCatalogo
        TipoCatalogoDTO tipoCatalogoDTO = tipoCatalogoMapper.toDto(tipoCatalogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoCatalogoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoCatalogoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoCatalogo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoCatalogo() throws Exception {
        // Initialize the database
        insertedTipoCatalogo = tipoCatalogoRepository.saveAndFlush(tipoCatalogo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tipoCatalogo
        restTipoCatalogoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoCatalogo.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tipoCatalogoRepository.count();
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

    protected TipoCatalogo getPersistedTipoCatalogo(TipoCatalogo tipoCatalogo) {
        return tipoCatalogoRepository.findById(tipoCatalogo.getId()).orElseThrow();
    }

    protected void assertPersistedTipoCatalogoToMatchAllProperties(TipoCatalogo expectedTipoCatalogo) {
        assertTipoCatalogoAllPropertiesEquals(expectedTipoCatalogo, getPersistedTipoCatalogo(expectedTipoCatalogo));
    }

    protected void assertPersistedTipoCatalogoToMatchUpdatableProperties(TipoCatalogo expectedTipoCatalogo) {
        assertTipoCatalogoAllUpdatablePropertiesEquals(expectedTipoCatalogo, getPersistedTipoCatalogo(expectedTipoCatalogo));
    }
}
