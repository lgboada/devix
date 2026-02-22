package com.devix.web.rest;

import static com.devix.domain.PaisAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.Pais;
import com.devix.repository.PaisRepository;
import com.devix.service.dto.PaisDTO;
import com.devix.service.mapper.PaisMapper;
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
 * Integration tests for the {@link PaisResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaisResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pais";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private PaisMapper paisMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaisMockMvc;

    private Pais pais;

    private Pais insertedPais;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pais createEntity() {
        return new Pais().noCia(DEFAULT_NO_CIA).descripcion(DEFAULT_DESCRIPCION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pais createUpdatedEntity() {
        return new Pais().noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);
    }

    @BeforeEach
    void initTest() {
        pais = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPais != null) {
            paisRepository.delete(insertedPais);
            insertedPais = null;
        }
    }

    @Test
    @Transactional
    void createPais() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);
        var returnedPaisDTO = om.readValue(
            restPaisMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaisDTO.class
        );

        // Validate the Pais in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPais = paisMapper.toEntity(returnedPaisDTO);
        assertPaisUpdatableFieldsEquals(returnedPais, getPersistedPais(returnedPais));

        insertedPais = returnedPais;
    }

    @Test
    @Transactional
    void createPaisWithExistingId() throws Exception {
        // Create the Pais with an existing ID
        pais.setId(1L);
        PaisDTO paisDTO = paisMapper.toDto(pais);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaisMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pais.setNoCia(null);

        // Create the Pais, which fails.
        PaisDTO paisDTO = paisMapper.toDto(pais);

        restPaisMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pais.setDescripcion(null);

        // Create the Pais, which fails.
        PaisDTO paisDTO = paisMapper.toDto(pais);

        restPaisMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPais() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList
        restPaisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pais.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getPais() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get the pais
        restPaisMockMvc
            .perform(get(ENTITY_API_URL_ID, pais.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pais.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getPaisByIdFiltering() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        Long id = pais.getId();

        defaultPaisFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaisFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaisFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaisByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where noCia equals to
        defaultPaisFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllPaisByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where noCia in
        defaultPaisFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllPaisByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where noCia is not null
        defaultPaisFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllPaisByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where noCia is greater than or equal to
        defaultPaisFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllPaisByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where noCia is less than or equal to
        defaultPaisFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllPaisByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where noCia is less than
        defaultPaisFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllPaisByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where noCia is greater than
        defaultPaisFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllPaisByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where descripcion equals to
        defaultPaisFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPaisByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where descripcion in
        defaultPaisFiltering("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION, "descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPaisByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where descripcion is not null
        defaultPaisFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllPaisByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where descripcion contains
        defaultPaisFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPaisByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where descripcion does not contain
        defaultPaisFiltering("descripcion.doesNotContain=" + UPDATED_DESCRIPCION, "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);
    }

    private void defaultPaisFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaisShouldBeFound(shouldBeFound);
        defaultPaisShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaisShouldBeFound(String filter) throws Exception {
        restPaisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pais.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restPaisMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaisShouldNotBeFound(String filter) throws Exception {
        restPaisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaisMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPais() throws Exception {
        // Get the pais
        restPaisMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPais() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pais
        Pais updatedPais = paisRepository.findById(pais.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPais are not directly saved in db
        em.detach(updatedPais);
        updatedPais.noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);
        PaisDTO paisDTO = paisMapper.toDto(updatedPais);

        restPaisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paisDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paisDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaisToMatchAllProperties(updatedPais);
    }

    @Test
    @Transactional
    void putNonExistingPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paisDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaisWithPatch() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pais using partial update
        Pais partialUpdatedPais = new Pais();
        partialUpdatedPais.setId(pais.getId());

        partialUpdatedPais.descripcion(UPDATED_DESCRIPCION);

        restPaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPais.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPais))
            )
            .andExpect(status().isOk());

        // Validate the Pais in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaisUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPais, pais), getPersistedPais(pais));
    }

    @Test
    @Transactional
    void fullUpdatePaisWithPatch() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pais using partial update
        Pais partialUpdatedPais = new Pais();
        partialUpdatedPais.setId(pais.getId());

        partialUpdatedPais.noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);

        restPaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPais.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPais))
            )
            .andExpect(status().isOk());

        // Validate the Pais in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaisUpdatableFieldsEquals(partialUpdatedPais, getPersistedPais(partialUpdatedPais));
    }

    @Test
    @Transactional
    void patchNonExistingPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paisDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePais() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pais
        restPaisMockMvc
            .perform(delete(ENTITY_API_URL_ID, pais.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paisRepository.count();
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

    protected Pais getPersistedPais(Pais pais) {
        return paisRepository.findById(pais.getId()).orElseThrow();
    }

    protected void assertPersistedPaisToMatchAllProperties(Pais expectedPais) {
        assertPaisAllPropertiesEquals(expectedPais, getPersistedPais(expectedPais));
    }

    protected void assertPersistedPaisToMatchUpdatableProperties(Pais expectedPais) {
        assertPaisAllUpdatablePropertiesEquals(expectedPais, getPersistedPais(expectedPais));
    }
}
