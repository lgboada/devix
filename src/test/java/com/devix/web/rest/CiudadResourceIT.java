package com.devix.web.rest;

import static com.devix.domain.CiudadAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.Ciudad;
import com.devix.domain.Provincia;
import com.devix.repository.CiudadRepository;
import com.devix.service.dto.CiudadDTO;
import com.devix.service.mapper.CiudadMapper;
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
 * Integration tests for the {@link CiudadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CiudadResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ciudads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CiudadRepository ciudadRepository;

    @Autowired
    private CiudadMapper ciudadMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCiudadMockMvc;

    private Ciudad ciudad;

    private Ciudad insertedCiudad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ciudad createEntity() {
        return new Ciudad().noCia(DEFAULT_NO_CIA).descripcion(DEFAULT_DESCRIPCION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ciudad createUpdatedEntity() {
        return new Ciudad().noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);
    }

    @BeforeEach
    void initTest() {
        ciudad = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCiudad != null) {
            ciudadRepository.delete(insertedCiudad);
            insertedCiudad = null;
        }
    }

    @Test
    @Transactional
    void createCiudad() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);
        var returnedCiudadDTO = om.readValue(
            restCiudadMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ciudadDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CiudadDTO.class
        );

        // Validate the Ciudad in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCiudad = ciudadMapper.toEntity(returnedCiudadDTO);
        assertCiudadUpdatableFieldsEquals(returnedCiudad, getPersistedCiudad(returnedCiudad));

        insertedCiudad = returnedCiudad;
    }

    @Test
    @Transactional
    void createCiudadWithExistingId() throws Exception {
        // Create the Ciudad with an existing ID
        ciudad.setId(1L);
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCiudadMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ciudadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ciudad in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ciudad.setNoCia(null);

        // Create the Ciudad, which fails.
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        restCiudadMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ciudadDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ciudad.setDescripcion(null);

        // Create the Ciudad, which fails.
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        restCiudadMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ciudadDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCiudads() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList
        restCiudadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ciudad.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getCiudad() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get the ciudad
        restCiudadMockMvc
            .perform(get(ENTITY_API_URL_ID, ciudad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ciudad.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getCiudadsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        Long id = ciudad.getId();

        defaultCiudadFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCiudadFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCiudadFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCiudadsByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where noCia equals to
        defaultCiudadFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCiudadsByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where noCia in
        defaultCiudadFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCiudadsByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where noCia is not null
        defaultCiudadFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllCiudadsByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where noCia is greater than or equal to
        defaultCiudadFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCiudadsByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where noCia is less than or equal to
        defaultCiudadFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCiudadsByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where noCia is less than
        defaultCiudadFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCiudadsByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where noCia is greater than
        defaultCiudadFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllCiudadsByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where descripcion equals to
        defaultCiudadFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCiudadsByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where descripcion in
        defaultCiudadFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllCiudadsByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where descripcion is not null
        defaultCiudadFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllCiudadsByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where descripcion contains
        defaultCiudadFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCiudadsByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where descripcion does not contain
        defaultCiudadFiltering("descripcion.doesNotContain=" + UPDATED_DESCRIPCION, "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCiudadsByProvinciaIsEqualToSomething() throws Exception {
        Provincia provincia;
        if (TestUtil.findAll(em, Provincia.class).isEmpty()) {
            ciudadRepository.saveAndFlush(ciudad);
            provincia = ProvinciaResourceIT.createEntity();
        } else {
            provincia = TestUtil.findAll(em, Provincia.class).get(0);
        }
        em.persist(provincia);
        em.flush();
        ciudad.setProvincia(provincia);
        ciudadRepository.saveAndFlush(ciudad);
        Long provinciaId = provincia.getId();
        // Get all the ciudadList where provincia equals to provinciaId
        defaultCiudadShouldBeFound("provinciaId.equals=" + provinciaId);

        // Get all the ciudadList where provincia equals to (provinciaId + 1)
        defaultCiudadShouldNotBeFound("provinciaId.equals=" + (provinciaId + 1));
    }

    private void defaultCiudadFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCiudadShouldBeFound(shouldBeFound);
        defaultCiudadShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCiudadShouldBeFound(String filter) throws Exception {
        restCiudadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ciudad.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restCiudadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCiudadShouldNotBeFound(String filter) throws Exception {
        restCiudadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCiudadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCiudad() throws Exception {
        // Get the ciudad
        restCiudadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCiudad() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ciudad
        Ciudad updatedCiudad = ciudadRepository.findById(ciudad.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCiudad are not directly saved in db
        em.detach(updatedCiudad);
        updatedCiudad.noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);
        CiudadDTO ciudadDTO = ciudadMapper.toDto(updatedCiudad);

        restCiudadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ciudadDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ciudadDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ciudad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCiudadToMatchAllProperties(updatedCiudad);
    }

    @Test
    @Transactional
    void putNonExistingCiudad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ciudad.setId(longCount.incrementAndGet());

        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCiudadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ciudadDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ciudadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ciudad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCiudad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ciudad.setId(longCount.incrementAndGet());

        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCiudadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ciudadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ciudad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCiudad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ciudad.setId(longCount.incrementAndGet());

        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCiudadMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ciudadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ciudad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCiudadWithPatch() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ciudad using partial update
        Ciudad partialUpdatedCiudad = new Ciudad();
        partialUpdatedCiudad.setId(ciudad.getId());

        partialUpdatedCiudad.noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);

        restCiudadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCiudad.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCiudad))
            )
            .andExpect(status().isOk());

        // Validate the Ciudad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCiudadUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCiudad, ciudad), getPersistedCiudad(ciudad));
    }

    @Test
    @Transactional
    void fullUpdateCiudadWithPatch() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ciudad using partial update
        Ciudad partialUpdatedCiudad = new Ciudad();
        partialUpdatedCiudad.setId(ciudad.getId());

        partialUpdatedCiudad.noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);

        restCiudadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCiudad.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCiudad))
            )
            .andExpect(status().isOk());

        // Validate the Ciudad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCiudadUpdatableFieldsEquals(partialUpdatedCiudad, getPersistedCiudad(partialUpdatedCiudad));
    }

    @Test
    @Transactional
    void patchNonExistingCiudad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ciudad.setId(longCount.incrementAndGet());

        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCiudadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ciudadDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ciudadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ciudad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCiudad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ciudad.setId(longCount.incrementAndGet());

        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCiudadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ciudadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ciudad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCiudad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ciudad.setId(longCount.incrementAndGet());

        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCiudadMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ciudadDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ciudad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCiudad() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ciudad
        restCiudadMockMvc
            .perform(delete(ENTITY_API_URL_ID, ciudad.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ciudadRepository.count();
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

    protected Ciudad getPersistedCiudad(Ciudad ciudad) {
        return ciudadRepository.findById(ciudad.getId()).orElseThrow();
    }

    protected void assertPersistedCiudadToMatchAllProperties(Ciudad expectedCiudad) {
        assertCiudadAllPropertiesEquals(expectedCiudad, getPersistedCiudad(expectedCiudad));
    }

    protected void assertPersistedCiudadToMatchUpdatableProperties(Ciudad expectedCiudad) {
        assertCiudadAllUpdatablePropertiesEquals(expectedCiudad, getPersistedCiudad(expectedCiudad));
    }
}
