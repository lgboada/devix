package com.devix.web.rest;

import static com.devix.domain.ProvinciaAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.Pais;
import com.devix.domain.Provincia;
import com.devix.repository.ProvinciaRepository;
import com.devix.service.dto.ProvinciaDTO;
import com.devix.service.mapper.ProvinciaMapper;
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
 * Integration tests for the {@link ProvinciaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProvinciaResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/provincias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProvinciaRepository provinciaRepository;

    @Autowired
    private ProvinciaMapper provinciaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProvinciaMockMvc;

    private Provincia provincia;

    private Provincia insertedProvincia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Provincia createEntity() {
        return new Provincia().noCia(DEFAULT_NO_CIA).descripcion(DEFAULT_DESCRIPCION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Provincia createUpdatedEntity() {
        return new Provincia().noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);
    }

    @BeforeEach
    void initTest() {
        provincia = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProvincia != null) {
            provinciaRepository.delete(insertedProvincia);
            insertedProvincia = null;
        }
    }

    @Test
    @Transactional
    void createProvincia() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Provincia
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);
        var returnedProvinciaDTO = om.readValue(
            restProvinciaMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(provinciaDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProvinciaDTO.class
        );

        // Validate the Provincia in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProvincia = provinciaMapper.toEntity(returnedProvinciaDTO);
        assertProvinciaUpdatableFieldsEquals(returnedProvincia, getPersistedProvincia(returnedProvincia));

        insertedProvincia = returnedProvincia;
    }

    @Test
    @Transactional
    void createProvinciaWithExistingId() throws Exception {
        // Create the Provincia with an existing ID
        provincia.setId(1L);
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProvinciaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(provinciaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Provincia in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        provincia.setNoCia(null);

        // Create the Provincia, which fails.
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);

        restProvinciaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(provinciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        provincia.setDescripcion(null);

        // Create the Provincia, which fails.
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);

        restProvinciaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(provinciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProvincias() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList
        restProvinciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(provincia.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getProvincia() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get the provincia
        restProvinciaMockMvc
            .perform(get(ENTITY_API_URL_ID, provincia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(provincia.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getProvinciasByIdFiltering() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        Long id = provincia.getId();

        defaultProvinciaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProvinciaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProvinciaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProvinciasByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where noCia equals to
        defaultProvinciaFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllProvinciasByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where noCia in
        defaultProvinciaFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllProvinciasByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where noCia is not null
        defaultProvinciaFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllProvinciasByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where noCia is greater than or equal to
        defaultProvinciaFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllProvinciasByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where noCia is less than or equal to
        defaultProvinciaFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllProvinciasByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where noCia is less than
        defaultProvinciaFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllProvinciasByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where noCia is greater than
        defaultProvinciaFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllProvinciasByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where descripcion equals to
        defaultProvinciaFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllProvinciasByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where descripcion in
        defaultProvinciaFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllProvinciasByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where descripcion is not null
        defaultProvinciaFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllProvinciasByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where descripcion contains
        defaultProvinciaFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllProvinciasByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where descripcion does not contain
        defaultProvinciaFiltering("descripcion.doesNotContain=" + UPDATED_DESCRIPCION, "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllProvinciasByPaisIsEqualToSomething() throws Exception {
        Pais pais;
        if (TestUtil.findAll(em, Pais.class).isEmpty()) {
            provinciaRepository.saveAndFlush(provincia);
            pais = PaisResourceIT.createEntity();
        } else {
            pais = TestUtil.findAll(em, Pais.class).get(0);
        }
        em.persist(pais);
        em.flush();
        provincia.setPais(pais);
        provinciaRepository.saveAndFlush(provincia);
        Long paisId = pais.getId();
        // Get all the provinciaList where pais equals to paisId
        defaultProvinciaShouldBeFound("paisId.equals=" + paisId);

        // Get all the provinciaList where pais equals to (paisId + 1)
        defaultProvinciaShouldNotBeFound("paisId.equals=" + (paisId + 1));
    }

    private void defaultProvinciaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProvinciaShouldBeFound(shouldBeFound);
        defaultProvinciaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProvinciaShouldBeFound(String filter) throws Exception {
        restProvinciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(provincia.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restProvinciaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProvinciaShouldNotBeFound(String filter) throws Exception {
        restProvinciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProvinciaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProvincia() throws Exception {
        // Get the provincia
        restProvinciaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProvincia() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the provincia
        Provincia updatedProvincia = provinciaRepository.findById(provincia.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProvincia are not directly saved in db
        em.detach(updatedProvincia);
        updatedProvincia.noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(updatedProvincia);

        restProvinciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, provinciaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(provinciaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Provincia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProvinciaToMatchAllProperties(updatedProvincia);
    }

    @Test
    @Transactional
    void putNonExistingProvincia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        provincia.setId(longCount.incrementAndGet());

        // Create the Provincia
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProvinciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, provinciaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(provinciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Provincia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProvincia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        provincia.setId(longCount.incrementAndGet());

        // Create the Provincia
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(provinciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Provincia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProvincia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        provincia.setId(longCount.incrementAndGet());

        // Create the Provincia
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinciaMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(provinciaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Provincia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProvinciaWithPatch() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the provincia using partial update
        Provincia partialUpdatedProvincia = new Provincia();
        partialUpdatedProvincia.setId(provincia.getId());

        partialUpdatedProvincia.noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);

        restProvinciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProvincia.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProvincia))
            )
            .andExpect(status().isOk());

        // Validate the Provincia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProvinciaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProvincia, provincia),
            getPersistedProvincia(provincia)
        );
    }

    @Test
    @Transactional
    void fullUpdateProvinciaWithPatch() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the provincia using partial update
        Provincia partialUpdatedProvincia = new Provincia();
        partialUpdatedProvincia.setId(provincia.getId());

        partialUpdatedProvincia.noCia(UPDATED_NO_CIA).descripcion(UPDATED_DESCRIPCION);

        restProvinciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProvincia.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProvincia))
            )
            .andExpect(status().isOk());

        // Validate the Provincia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProvinciaUpdatableFieldsEquals(partialUpdatedProvincia, getPersistedProvincia(partialUpdatedProvincia));
    }

    @Test
    @Transactional
    void patchNonExistingProvincia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        provincia.setId(longCount.incrementAndGet());

        // Create the Provincia
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProvinciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, provinciaDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(provinciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Provincia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProvincia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        provincia.setId(longCount.incrementAndGet());

        // Create the Provincia
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(provinciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Provincia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProvincia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        provincia.setId(longCount.incrementAndGet());

        // Create the Provincia
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinciaMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(provinciaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Provincia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProvincia() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the provincia
        restProvinciaMockMvc
            .perform(delete(ENTITY_API_URL_ID, provincia.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return provinciaRepository.count();
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

    protected Provincia getPersistedProvincia(Provincia provincia) {
        return provinciaRepository.findById(provincia.getId()).orElseThrow();
    }

    protected void assertPersistedProvinciaToMatchAllProperties(Provincia expectedProvincia) {
        assertProvinciaAllPropertiesEquals(expectedProvincia, getPersistedProvincia(expectedProvincia));
    }

    protected void assertPersistedProvinciaToMatchUpdatableProperties(Provincia expectedProvincia) {
        assertProvinciaAllUpdatablePropertiesEquals(expectedProvincia, getPersistedProvincia(expectedProvincia));
    }
}
