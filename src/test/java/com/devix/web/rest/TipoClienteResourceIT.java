package com.devix.web.rest;

import static com.devix.domain.TipoClienteAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.TipoCliente;
import com.devix.repository.TipoClienteRepository;
import com.devix.service.dto.TipoClienteDTO;
import com.devix.service.mapper.TipoClienteMapper;
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
 * Integration tests for the {@link TipoClienteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoClienteResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-clientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TipoClienteRepository tipoClienteRepository;

    @Autowired
    private TipoClienteMapper tipoClienteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoClienteMockMvc;

    private TipoCliente tipoCliente;

    private TipoCliente insertedTipoCliente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoCliente createEntity() {
        return new TipoCliente().noCia(DEFAULT_NO_CIA).descripcion(DEFAULT_DESCRIPCION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoCliente createUpdatedEntity() {
        return new TipoCliente().noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);
    }

    @BeforeEach
    void initTest() {
        tipoCliente = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTipoCliente != null) {
            tipoClienteRepository.delete(insertedTipoCliente);
            insertedTipoCliente = null;
        }
    }

    @Test
    @Transactional
    void createTipoCliente() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TipoCliente
        TipoClienteDTO tipoClienteDTO = tipoClienteMapper.toDto(tipoCliente);
        var returnedTipoClienteDTO = om.readValue(
            restTipoClienteMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoClienteDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TipoClienteDTO.class
        );

        // Validate the TipoCliente in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTipoCliente = tipoClienteMapper.toEntity(returnedTipoClienteDTO);
        assertTipoClienteUpdatableFieldsEquals(returnedTipoCliente, getPersistedTipoCliente(returnedTipoCliente));

        insertedTipoCliente = returnedTipoCliente;
    }

    @Test
    @Transactional
    void createTipoClienteWithExistingId() throws Exception {
        // Create the TipoCliente with an existing ID
        tipoCliente.setId(1L);
        TipoClienteDTO tipoClienteDTO = tipoClienteMapper.toDto(tipoCliente);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoClienteMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoClienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoCliente in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoCliente.setNoCia(null);

        // Create the TipoCliente, which fails.
        TipoClienteDTO tipoClienteDTO = tipoClienteMapper.toDto(tipoCliente);

        restTipoClienteMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoClienteDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoCliente.setDescripcion(null);

        // Create the TipoCliente, which fails.
        TipoClienteDTO tipoClienteDTO = tipoClienteMapper.toDto(tipoCliente);

        restTipoClienteMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoClienteDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipoClientes() throws Exception {
        // Initialize the database
        insertedTipoCliente = tipoClienteRepository.saveAndFlush(tipoCliente);

        // Get all the tipoClienteList
        restTipoClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoCliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getTipoCliente() throws Exception {
        // Initialize the database
        insertedTipoCliente = tipoClienteRepository.saveAndFlush(tipoCliente);

        // Get the tipoCliente
        restTipoClienteMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoCliente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoCliente.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getTipoClientesByIdFiltering() throws Exception {
        // Initialize the database
        insertedTipoCliente = tipoClienteRepository.saveAndFlush(tipoCliente);

        Long id = tipoCliente.getId();

        defaultTipoClienteFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTipoClienteFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTipoClienteFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTipoClientesByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoCliente = tipoClienteRepository.saveAndFlush(tipoCliente);

        // Get all the tipoClienteList where noCia equals to
        defaultTipoClienteFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoClientesByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoCliente = tipoClienteRepository.saveAndFlush(tipoCliente);

        // Get all the tipoClienteList where noCia in
        defaultTipoClienteFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoClientesByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoCliente = tipoClienteRepository.saveAndFlush(tipoCliente);

        // Get all the tipoClienteList where noCia is not null
        defaultTipoClienteFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoClientesByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoCliente = tipoClienteRepository.saveAndFlush(tipoCliente);

        // Get all the tipoClienteList where noCia is greater than or equal to
        defaultTipoClienteFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoClientesByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoCliente = tipoClienteRepository.saveAndFlush(tipoCliente);

        // Get all the tipoClienteList where noCia is less than or equal to
        defaultTipoClienteFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoClientesByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTipoCliente = tipoClienteRepository.saveAndFlush(tipoCliente);

        // Get all the tipoClienteList where noCia is less than
        defaultTipoClienteFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoClientesByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTipoCliente = tipoClienteRepository.saveAndFlush(tipoCliente);

        // Get all the tipoClienteList where noCia is greater than
        defaultTipoClienteFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoClientesByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoCliente = tipoClienteRepository.saveAndFlush(tipoCliente);

        // Get all the tipoClienteList where descripcion equals to
        defaultTipoClienteFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTipoClientesByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoCliente = tipoClienteRepository.saveAndFlush(tipoCliente);

        // Get all the tipoClienteList where descripcion in
        defaultTipoClienteFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllTipoClientesByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoCliente = tipoClienteRepository.saveAndFlush(tipoCliente);

        // Get all the tipoClienteList where descripcion is not null
        defaultTipoClienteFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoClientesByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoCliente = tipoClienteRepository.saveAndFlush(tipoCliente);

        // Get all the tipoClienteList where descripcion contains
        defaultTipoClienteFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTipoClientesByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoCliente = tipoClienteRepository.saveAndFlush(tipoCliente);

        // Get all the tipoClienteList where descripcion does not contain
        defaultTipoClienteFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    private void defaultTipoClienteFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTipoClienteShouldBeFound(shouldBeFound);
        defaultTipoClienteShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTipoClienteShouldBeFound(String filter) throws Exception {
        restTipoClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoCliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restTipoClienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTipoClienteShouldNotBeFound(String filter) throws Exception {
        restTipoClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTipoClienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTipoCliente() throws Exception {
        // Get the tipoCliente
        restTipoClienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTipoCliente() throws Exception {
        // Initialize the database
        insertedTipoCliente = tipoClienteRepository.saveAndFlush(tipoCliente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoCliente
        TipoCliente updatedTipoCliente = tipoClienteRepository.findById(tipoCliente.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTipoCliente are not directly saved in db
        em.detach(updatedTipoCliente);
        updatedTipoCliente.noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);
        TipoClienteDTO tipoClienteDTO = tipoClienteMapper.toDto(updatedTipoCliente);

        restTipoClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoClienteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoClienteDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoCliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTipoClienteToMatchAllProperties(updatedTipoCliente);
    }

    @Test
    @Transactional
    void putNonExistingTipoCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoCliente.setId(longCount.incrementAndGet());

        // Create the TipoCliente
        TipoClienteDTO tipoClienteDTO = tipoClienteMapper.toDto(tipoCliente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoClienteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoClienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoCliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoCliente.setId(longCount.incrementAndGet());

        // Create the TipoCliente
        TipoClienteDTO tipoClienteDTO = tipoClienteMapper.toDto(tipoCliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoClienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoCliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoCliente.setId(longCount.incrementAndGet());

        // Create the TipoCliente
        TipoClienteDTO tipoClienteDTO = tipoClienteMapper.toDto(tipoCliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoClienteMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoClienteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoCliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoClienteWithPatch() throws Exception {
        // Initialize the database
        insertedTipoCliente = tipoClienteRepository.saveAndFlush(tipoCliente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoCliente using partial update
        TipoCliente partialUpdatedTipoCliente = new TipoCliente();
        partialUpdatedTipoCliente.setId(tipoCliente.getId());

        partialUpdatedTipoCliente.noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);

        restTipoClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoCliente.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoCliente))
            )
            .andExpect(status().isOk());

        // Validate the TipoCliente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoClienteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTipoCliente, tipoCliente),
            getPersistedTipoCliente(tipoCliente)
        );
    }

    @Test
    @Transactional
    void fullUpdateTipoClienteWithPatch() throws Exception {
        // Initialize the database
        insertedTipoCliente = tipoClienteRepository.saveAndFlush(tipoCliente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoCliente using partial update
        TipoCliente partialUpdatedTipoCliente = new TipoCliente();
        partialUpdatedTipoCliente.setId(tipoCliente.getId());

        partialUpdatedTipoCliente.noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);

        restTipoClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoCliente.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoCliente))
            )
            .andExpect(status().isOk());

        // Validate the TipoCliente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoClienteUpdatableFieldsEquals(partialUpdatedTipoCliente, getPersistedTipoCliente(partialUpdatedTipoCliente));
    }

    @Test
    @Transactional
    void patchNonExistingTipoCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoCliente.setId(longCount.incrementAndGet());

        // Create the TipoCliente
        TipoClienteDTO tipoClienteDTO = tipoClienteMapper.toDto(tipoCliente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoClienteDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoClienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoCliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoCliente.setId(longCount.incrementAndGet());

        // Create the TipoCliente
        TipoClienteDTO tipoClienteDTO = tipoClienteMapper.toDto(tipoCliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoClienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoCliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoCliente.setId(longCount.incrementAndGet());

        // Create the TipoCliente
        TipoClienteDTO tipoClienteDTO = tipoClienteMapper.toDto(tipoCliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoClienteMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tipoClienteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoCliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoCliente() throws Exception {
        // Initialize the database
        insertedTipoCliente = tipoClienteRepository.saveAndFlush(tipoCliente);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tipoCliente
        restTipoClienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoCliente.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tipoClienteRepository.count();
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

    protected TipoCliente getPersistedTipoCliente(TipoCliente tipoCliente) {
        return tipoClienteRepository.findById(tipoCliente.getId()).orElseThrow();
    }

    protected void assertPersistedTipoClienteToMatchAllProperties(TipoCliente expectedTipoCliente) {
        assertTipoClienteAllPropertiesEquals(expectedTipoCliente, getPersistedTipoCliente(expectedTipoCliente));
    }

    protected void assertPersistedTipoClienteToMatchUpdatableProperties(TipoCliente expectedTipoCliente) {
        assertTipoClienteAllUpdatablePropertiesEquals(expectedTipoCliente, getPersistedTipoCliente(expectedTipoCliente));
    }
}
