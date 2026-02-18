package com.devix.web.rest;

import static com.devix.domain.TipoDireccionAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.TipoDireccion;
import com.devix.repository.TipoDireccionRepository;
import com.devix.service.dto.TipoDireccionDTO;
import com.devix.service.mapper.TipoDireccionMapper;
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
 * Integration tests for the {@link TipoDireccionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoDireccionResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-direccions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TipoDireccionRepository tipoDireccionRepository;

    @Autowired
    private TipoDireccionMapper tipoDireccionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoDireccionMockMvc;

    private TipoDireccion tipoDireccion;

    private TipoDireccion insertedTipoDireccion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoDireccion createEntity() {
        return new TipoDireccion().noCia(DEFAULT_NO_CIA).descripcion(DEFAULT_DESCRIPCION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoDireccion createUpdatedEntity() {
        return new TipoDireccion().noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);
    }

    @BeforeEach
    void initTest() {
        tipoDireccion = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTipoDireccion != null) {
            tipoDireccionRepository.delete(insertedTipoDireccion);
            insertedTipoDireccion = null;
        }
    }

    @Test
    @Transactional
    void createTipoDireccion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TipoDireccion
        TipoDireccionDTO tipoDireccionDTO = tipoDireccionMapper.toDto(tipoDireccion);
        var returnedTipoDireccionDTO = om.readValue(
            restTipoDireccionMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(tipoDireccionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TipoDireccionDTO.class
        );

        // Validate the TipoDireccion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTipoDireccion = tipoDireccionMapper.toEntity(returnedTipoDireccionDTO);
        assertTipoDireccionUpdatableFieldsEquals(returnedTipoDireccion, getPersistedTipoDireccion(returnedTipoDireccion));

        insertedTipoDireccion = returnedTipoDireccion;
    }

    @Test
    @Transactional
    void createTipoDireccionWithExistingId() throws Exception {
        // Create the TipoDireccion with an existing ID
        tipoDireccion.setId(1L);
        TipoDireccionDTO tipoDireccionDTO = tipoDireccionMapper.toDto(tipoDireccion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoDireccionMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoDireccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoDireccion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoDireccion.setNoCia(null);

        // Create the TipoDireccion, which fails.
        TipoDireccionDTO tipoDireccionDTO = tipoDireccionMapper.toDto(tipoDireccion);

        restTipoDireccionMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoDireccionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoDireccion.setDescripcion(null);

        // Create the TipoDireccion, which fails.
        TipoDireccionDTO tipoDireccionDTO = tipoDireccionMapper.toDto(tipoDireccion);

        restTipoDireccionMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoDireccionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipoDireccions() throws Exception {
        // Initialize the database
        insertedTipoDireccion = tipoDireccionRepository.saveAndFlush(tipoDireccion);

        // Get all the tipoDireccionList
        restTipoDireccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoDireccion.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getTipoDireccion() throws Exception {
        // Initialize the database
        insertedTipoDireccion = tipoDireccionRepository.saveAndFlush(tipoDireccion);

        // Get the tipoDireccion
        restTipoDireccionMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoDireccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoDireccion.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getTipoDireccionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTipoDireccion = tipoDireccionRepository.saveAndFlush(tipoDireccion);

        Long id = tipoDireccion.getId();

        defaultTipoDireccionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTipoDireccionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTipoDireccionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTipoDireccionsByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDireccion = tipoDireccionRepository.saveAndFlush(tipoDireccion);

        // Get all the tipoDireccionList where noCia equals to
        defaultTipoDireccionFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoDireccionsByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoDireccion = tipoDireccionRepository.saveAndFlush(tipoDireccion);

        // Get all the tipoDireccionList where noCia in
        defaultTipoDireccionFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoDireccionsByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoDireccion = tipoDireccionRepository.saveAndFlush(tipoDireccion);

        // Get all the tipoDireccionList where noCia is not null
        defaultTipoDireccionFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoDireccionsByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDireccion = tipoDireccionRepository.saveAndFlush(tipoDireccion);

        // Get all the tipoDireccionList where noCia is greater than or equal to
        defaultTipoDireccionFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoDireccionsByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDireccion = tipoDireccionRepository.saveAndFlush(tipoDireccion);

        // Get all the tipoDireccionList where noCia is less than or equal to
        defaultTipoDireccionFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoDireccionsByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTipoDireccion = tipoDireccionRepository.saveAndFlush(tipoDireccion);

        // Get all the tipoDireccionList where noCia is less than
        defaultTipoDireccionFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoDireccionsByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTipoDireccion = tipoDireccionRepository.saveAndFlush(tipoDireccion);

        // Get all the tipoDireccionList where noCia is greater than
        defaultTipoDireccionFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoDireccionsByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDireccion = tipoDireccionRepository.saveAndFlush(tipoDireccion);

        // Get all the tipoDireccionList where descripcion equals to
        defaultTipoDireccionFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTipoDireccionsByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoDireccion = tipoDireccionRepository.saveAndFlush(tipoDireccion);

        // Get all the tipoDireccionList where descripcion in
        defaultTipoDireccionFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllTipoDireccionsByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoDireccion = tipoDireccionRepository.saveAndFlush(tipoDireccion);

        // Get all the tipoDireccionList where descripcion is not null
        defaultTipoDireccionFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoDireccionsByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoDireccion = tipoDireccionRepository.saveAndFlush(tipoDireccion);

        // Get all the tipoDireccionList where descripcion contains
        defaultTipoDireccionFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTipoDireccionsByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoDireccion = tipoDireccionRepository.saveAndFlush(tipoDireccion);

        // Get all the tipoDireccionList where descripcion does not contain
        defaultTipoDireccionFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    private void defaultTipoDireccionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTipoDireccionShouldBeFound(shouldBeFound);
        defaultTipoDireccionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTipoDireccionShouldBeFound(String filter) throws Exception {
        restTipoDireccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoDireccion.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restTipoDireccionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTipoDireccionShouldNotBeFound(String filter) throws Exception {
        restTipoDireccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTipoDireccionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTipoDireccion() throws Exception {
        // Get the tipoDireccion
        restTipoDireccionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTipoDireccion() throws Exception {
        // Initialize the database
        insertedTipoDireccion = tipoDireccionRepository.saveAndFlush(tipoDireccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoDireccion
        TipoDireccion updatedTipoDireccion = tipoDireccionRepository.findById(tipoDireccion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTipoDireccion are not directly saved in db
        em.detach(updatedTipoDireccion);
        updatedTipoDireccion.noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);
        TipoDireccionDTO tipoDireccionDTO = tipoDireccionMapper.toDto(updatedTipoDireccion);

        restTipoDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoDireccionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoDireccionDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoDireccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTipoDireccionToMatchAllProperties(updatedTipoDireccion);
    }

    @Test
    @Transactional
    void putNonExistingTipoDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoDireccion.setId(longCount.incrementAndGet());

        // Create the TipoDireccion
        TipoDireccionDTO tipoDireccionDTO = tipoDireccionMapper.toDto(tipoDireccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoDireccionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoDireccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoDireccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoDireccion.setId(longCount.incrementAndGet());

        // Create the TipoDireccion
        TipoDireccionDTO tipoDireccionDTO = tipoDireccionMapper.toDto(tipoDireccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoDireccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoDireccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoDireccion.setId(longCount.incrementAndGet());

        // Create the TipoDireccion
        TipoDireccionDTO tipoDireccionDTO = tipoDireccionMapper.toDto(tipoDireccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoDireccionMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoDireccionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoDireccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoDireccionWithPatch() throws Exception {
        // Initialize the database
        insertedTipoDireccion = tipoDireccionRepository.saveAndFlush(tipoDireccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoDireccion using partial update
        TipoDireccion partialUpdatedTipoDireccion = new TipoDireccion();
        partialUpdatedTipoDireccion.setId(tipoDireccion.getId());

        partialUpdatedTipoDireccion.noCia(UPDATED_NO_CIA);

        restTipoDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoDireccion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoDireccion))
            )
            .andExpect(status().isOk());

        // Validate the TipoDireccion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoDireccionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTipoDireccion, tipoDireccion),
            getPersistedTipoDireccion(tipoDireccion)
        );
    }

    @Test
    @Transactional
    void fullUpdateTipoDireccionWithPatch() throws Exception {
        // Initialize the database
        insertedTipoDireccion = tipoDireccionRepository.saveAndFlush(tipoDireccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoDireccion using partial update
        TipoDireccion partialUpdatedTipoDireccion = new TipoDireccion();
        partialUpdatedTipoDireccion.setId(tipoDireccion.getId());

        partialUpdatedTipoDireccion.noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);

        restTipoDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoDireccion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoDireccion))
            )
            .andExpect(status().isOk());

        // Validate the TipoDireccion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoDireccionUpdatableFieldsEquals(partialUpdatedTipoDireccion, getPersistedTipoDireccion(partialUpdatedTipoDireccion));
    }

    @Test
    @Transactional
    void patchNonExistingTipoDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoDireccion.setId(longCount.incrementAndGet());

        // Create the TipoDireccion
        TipoDireccionDTO tipoDireccionDTO = tipoDireccionMapper.toDto(tipoDireccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoDireccionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoDireccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoDireccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoDireccion.setId(longCount.incrementAndGet());

        // Create the TipoDireccion
        TipoDireccionDTO tipoDireccionDTO = tipoDireccionMapper.toDto(tipoDireccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoDireccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoDireccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoDireccion.setId(longCount.incrementAndGet());

        // Create the TipoDireccion
        TipoDireccionDTO tipoDireccionDTO = tipoDireccionMapper.toDto(tipoDireccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoDireccionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoDireccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoDireccion() throws Exception {
        // Initialize the database
        insertedTipoDireccion = tipoDireccionRepository.saveAndFlush(tipoDireccion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tipoDireccion
        restTipoDireccionMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoDireccion.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tipoDireccionRepository.count();
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

    protected TipoDireccion getPersistedTipoDireccion(TipoDireccion tipoDireccion) {
        return tipoDireccionRepository.findById(tipoDireccion.getId()).orElseThrow();
    }

    protected void assertPersistedTipoDireccionToMatchAllProperties(TipoDireccion expectedTipoDireccion) {
        assertTipoDireccionAllPropertiesEquals(expectedTipoDireccion, getPersistedTipoDireccion(expectedTipoDireccion));
    }

    protected void assertPersistedTipoDireccionToMatchUpdatableProperties(TipoDireccion expectedTipoDireccion) {
        assertTipoDireccionAllUpdatablePropertiesEquals(expectedTipoDireccion, getPersistedTipoDireccion(expectedTipoDireccion));
    }
}
