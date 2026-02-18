package com.devix.web.rest;

import static com.devix.domain.CentroAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.Centro;
import com.devix.domain.Compania;
import com.devix.repository.CentroRepository;
import com.devix.service.dto.CentroDTO;
import com.devix.service.mapper.CentroMapper;
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
 * Integration tests for the {@link CentroResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CentroResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/centros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CentroRepository centroRepository;

    @Autowired
    private CentroMapper centroMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCentroMockMvc;

    private Centro centro;

    private Centro insertedCentro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Centro createEntity() {
        return new Centro().noCia(DEFAULT_NO_CIA).descripcion(DEFAULT_DESCRIPCION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Centro createUpdatedEntity() {
        return new Centro().noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);
    }

    @BeforeEach
    void initTest() {
        centro = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCentro != null) {
            centroRepository.delete(insertedCentro);
            insertedCentro = null;
        }
    }

    @Test
    @Transactional
    void createCentro() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Centro
        CentroDTO centroDTO = centroMapper.toDto(centro);
        var returnedCentroDTO = om.readValue(
            restCentroMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(centroDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CentroDTO.class
        );

        // Validate the Centro in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCentro = centroMapper.toEntity(returnedCentroDTO);
        assertCentroUpdatableFieldsEquals(returnedCentro, getPersistedCentro(returnedCentro));

        insertedCentro = returnedCentro;
    }

    @Test
    @Transactional
    void createCentroWithExistingId() throws Exception {
        // Create the Centro with an existing ID
        centro.setId(1L);
        CentroDTO centroDTO = centroMapper.toDto(centro);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCentroMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(centroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Centro in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        centro.setNoCia(null);

        // Create the Centro, which fails.
        CentroDTO centroDTO = centroMapper.toDto(centro);

        restCentroMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(centroDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        centro.setDescripcion(null);

        // Create the Centro, which fails.
        CentroDTO centroDTO = centroMapper.toDto(centro);

        restCentroMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(centroDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCentros() throws Exception {
        // Initialize the database
        insertedCentro = centroRepository.saveAndFlush(centro);

        // Get all the centroList
        restCentroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(centro.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getCentro() throws Exception {
        // Initialize the database
        insertedCentro = centroRepository.saveAndFlush(centro);

        // Get the centro
        restCentroMockMvc
            .perform(get(ENTITY_API_URL_ID, centro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(centro.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getCentrosByIdFiltering() throws Exception {
        // Initialize the database
        insertedCentro = centroRepository.saveAndFlush(centro);

        Long id = centro.getId();

        defaultCentroFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCentroFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCentroFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCentrosByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCentro = centroRepository.saveAndFlush(centro);

        // Get all the centroList where noCia equals to
        defaultCentroFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCentrosByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCentro = centroRepository.saveAndFlush(centro);

        // Get all the centroList where noCia in
        defaultCentroFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCentrosByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCentro = centroRepository.saveAndFlush(centro);

        // Get all the centroList where noCia is not null
        defaultCentroFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllCentrosByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCentro = centroRepository.saveAndFlush(centro);

        // Get all the centroList where noCia is greater than or equal to
        defaultCentroFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCentrosByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCentro = centroRepository.saveAndFlush(centro);

        // Get all the centroList where noCia is less than or equal to
        defaultCentroFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCentrosByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCentro = centroRepository.saveAndFlush(centro);

        // Get all the centroList where noCia is less than
        defaultCentroFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCentrosByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCentro = centroRepository.saveAndFlush(centro);

        // Get all the centroList where noCia is greater than
        defaultCentroFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCentrosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCentro = centroRepository.saveAndFlush(centro);

        // Get all the centroList where descripcion equals to
        defaultCentroFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCentrosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCentro = centroRepository.saveAndFlush(centro);

        // Get all the centroList where descripcion in
        defaultCentroFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllCentrosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCentro = centroRepository.saveAndFlush(centro);

        // Get all the centroList where descripcion is not null
        defaultCentroFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllCentrosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedCentro = centroRepository.saveAndFlush(centro);

        // Get all the centroList where descripcion contains
        defaultCentroFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCentrosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCentro = centroRepository.saveAndFlush(centro);

        // Get all the centroList where descripcion does not contain
        defaultCentroFiltering("descripcion.doesNotContain=" + UPDATED_DESCRIPCION, "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCentrosByCompaniaIsEqualToSomething() throws Exception {
        Compania compania;
        if (TestUtil.findAll(em, Compania.class).isEmpty()) {
            centroRepository.saveAndFlush(centro);
            compania = CompaniaResourceIT.createEntity();
        } else {
            compania = TestUtil.findAll(em, Compania.class).get(0);
        }
        em.persist(compania);
        em.flush();
        centro.setCompania(compania);
        centroRepository.saveAndFlush(centro);
        Long companiaId = compania.getId();
        // Get all the centroList where compania equals to companiaId
        defaultCentroShouldBeFound("companiaId.equals=" + companiaId);

        // Get all the centroList where compania equals to (companiaId + 1)
        defaultCentroShouldNotBeFound("companiaId.equals=" + (companiaId + 1));
    }

    private void defaultCentroFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCentroShouldBeFound(shouldBeFound);
        defaultCentroShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCentroShouldBeFound(String filter) throws Exception {
        restCentroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(centro.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restCentroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCentroShouldNotBeFound(String filter) throws Exception {
        restCentroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCentroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCentro() throws Exception {
        // Get the centro
        restCentroMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCentro() throws Exception {
        // Initialize the database
        insertedCentro = centroRepository.saveAndFlush(centro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the centro
        Centro updatedCentro = centroRepository.findById(centro.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCentro are not directly saved in db
        em.detach(updatedCentro);
        updatedCentro.noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);
        CentroDTO centroDTO = centroMapper.toDto(updatedCentro);

        restCentroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, centroDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(centroDTO))
            )
            .andExpect(status().isOk());

        // Validate the Centro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCentroToMatchAllProperties(updatedCentro);
    }

    @Test
    @Transactional
    void putNonExistingCentro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        centro.setId(longCount.incrementAndGet());

        // Create the Centro
        CentroDTO centroDTO = centroMapper.toDto(centro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCentroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, centroDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(centroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Centro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCentro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        centro.setId(longCount.incrementAndGet());

        // Create the Centro
        CentroDTO centroDTO = centroMapper.toDto(centro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCentroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(centroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Centro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCentro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        centro.setId(longCount.incrementAndGet());

        // Create the Centro
        CentroDTO centroDTO = centroMapper.toDto(centro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCentroMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(centroDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Centro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCentroWithPatch() throws Exception {
        // Initialize the database
        insertedCentro = centroRepository.saveAndFlush(centro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the centro using partial update
        Centro partialUpdatedCentro = new Centro();
        partialUpdatedCentro.setId(centro.getId());

        partialUpdatedCentro.descripcion(UPDATED_DESCRIPCION);

        restCentroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCentro.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCentro))
            )
            .andExpect(status().isOk());

        // Validate the Centro in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCentroUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCentro, centro), getPersistedCentro(centro));
    }

    @Test
    @Transactional
    void fullUpdateCentroWithPatch() throws Exception {
        // Initialize the database
        insertedCentro = centroRepository.saveAndFlush(centro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the centro using partial update
        Centro partialUpdatedCentro = new Centro();
        partialUpdatedCentro.setId(centro.getId());

        partialUpdatedCentro.noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);

        restCentroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCentro.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCentro))
            )
            .andExpect(status().isOk());

        // Validate the Centro in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCentroUpdatableFieldsEquals(partialUpdatedCentro, getPersistedCentro(partialUpdatedCentro));
    }

    @Test
    @Transactional
    void patchNonExistingCentro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        centro.setId(longCount.incrementAndGet());

        // Create the Centro
        CentroDTO centroDTO = centroMapper.toDto(centro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCentroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, centroDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(centroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Centro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCentro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        centro.setId(longCount.incrementAndGet());

        // Create the Centro
        CentroDTO centroDTO = centroMapper.toDto(centro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCentroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(centroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Centro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCentro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        centro.setId(longCount.incrementAndGet());

        // Create the Centro
        CentroDTO centroDTO = centroMapper.toDto(centro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCentroMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(centroDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Centro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCentro() throws Exception {
        // Initialize the database
        insertedCentro = centroRepository.saveAndFlush(centro);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the centro
        restCentroMockMvc
            .perform(delete(ENTITY_API_URL_ID, centro.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return centroRepository.count();
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

    protected Centro getPersistedCentro(Centro centro) {
        return centroRepository.findById(centro.getId()).orElseThrow();
    }

    protected void assertPersistedCentroToMatchAllProperties(Centro expectedCentro) {
        assertCentroAllPropertiesEquals(expectedCentro, getPersistedCentro(expectedCentro));
    }

    protected void assertPersistedCentroToMatchUpdatableProperties(Centro expectedCentro) {
        assertCentroAllUpdatablePropertiesEquals(expectedCentro, getPersistedCentro(expectedCentro));
    }
}
