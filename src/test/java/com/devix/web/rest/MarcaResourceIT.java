package com.devix.web.rest;

import static com.devix.domain.MarcaAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.Marca;
import com.devix.repository.MarcaRepository;
import com.devix.service.dto.MarcaDTO;
import com.devix.service.mapper.MarcaMapper;
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
 * Integration tests for the {@link MarcaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MarcaResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_PATH_IMAGEN = "AAAAAAAAAA";
    private static final String UPDATED_PATH_IMAGEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/marcas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private MarcaMapper marcaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMarcaMockMvc;

    private Marca marca;

    private Marca insertedMarca;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Marca createEntity() {
        return new Marca().noCia(DEFAULT_NO_CIA).nombre(DEFAULT_NOMBRE).pathImagen(DEFAULT_PATH_IMAGEN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Marca createUpdatedEntity() {
        return new Marca().noCia(UPDATED_NO_CIA).nombre(UPDATED_NOMBRE).pathImagen(UPDATED_PATH_IMAGEN);
    }

    @BeforeEach
    void initTest() {
        marca = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMarca != null) {
            marcaRepository.delete(insertedMarca);
            insertedMarca = null;
        }
    }

    @Test
    @Transactional
    void createMarca() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Marca
        MarcaDTO marcaDTO = marcaMapper.toDto(marca);
        var returnedMarcaDTO = om.readValue(
            restMarcaMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marcaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MarcaDTO.class
        );

        // Validate the Marca in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMarca = marcaMapper.toEntity(returnedMarcaDTO);
        assertMarcaUpdatableFieldsEquals(returnedMarca, getPersistedMarca(returnedMarca));

        insertedMarca = returnedMarca;
    }

    @Test
    @Transactional
    void createMarcaWithExistingId() throws Exception {
        // Create the Marca with an existing ID
        marca.setId(1L);
        MarcaDTO marcaDTO = marcaMapper.toDto(marca);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarcaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marcaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        marca.setNoCia(null);

        // Create the Marca, which fails.
        MarcaDTO marcaDTO = marcaMapper.toDto(marca);

        restMarcaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marcaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        marca.setNombre(null);

        // Create the Marca, which fails.
        MarcaDTO marcaDTO = marcaMapper.toDto(marca);

        restMarcaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marcaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPathImagenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        marca.setPathImagen(null);

        // Create the Marca, which fails.
        MarcaDTO marcaDTO = marcaMapper.toDto(marca);

        restMarcaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marcaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMarcas() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        // Get all the marcaList
        restMarcaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marca.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].pathImagen").value(hasItem(DEFAULT_PATH_IMAGEN)));
    }

    @Test
    @Transactional
    void getMarca() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        // Get the marca
        restMarcaMockMvc
            .perform(get(ENTITY_API_URL_ID, marca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(marca.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.pathImagen").value(DEFAULT_PATH_IMAGEN));
    }

    @Test
    @Transactional
    void getMarcasByIdFiltering() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        Long id = marca.getId();

        defaultMarcaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMarcaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMarcaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMarcasByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        // Get all the marcaList where noCia equals to
        defaultMarcaFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllMarcasByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        // Get all the marcaList where noCia in
        defaultMarcaFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllMarcasByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        // Get all the marcaList where noCia is not null
        defaultMarcaFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllMarcasByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        // Get all the marcaList where noCia is greater than or equal to
        defaultMarcaFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllMarcasByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        // Get all the marcaList where noCia is less than or equal to
        defaultMarcaFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllMarcasByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        // Get all the marcaList where noCia is less than
        defaultMarcaFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllMarcasByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        // Get all the marcaList where noCia is greater than
        defaultMarcaFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllMarcasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        // Get all the marcaList where nombre equals to
        defaultMarcaFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllMarcasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        // Get all the marcaList where nombre in
        defaultMarcaFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllMarcasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        // Get all the marcaList where nombre is not null
        defaultMarcaFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllMarcasByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        // Get all the marcaList where nombre contains
        defaultMarcaFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllMarcasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        // Get all the marcaList where nombre does not contain
        defaultMarcaFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllMarcasByPathImagenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        // Get all the marcaList where pathImagen equals to
        defaultMarcaFiltering("pathImagen.equals=" + DEFAULT_PATH_IMAGEN, "pathImagen.equals=" + UPDATED_PATH_IMAGEN);
    }

    @Test
    @Transactional
    void getAllMarcasByPathImagenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        // Get all the marcaList where pathImagen in
        defaultMarcaFiltering("pathImagen.in=" + DEFAULT_PATH_IMAGEN + "," + UPDATED_PATH_IMAGEN, "pathImagen.in=" + UPDATED_PATH_IMAGEN);
    }

    @Test
    @Transactional
    void getAllMarcasByPathImagenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        // Get all the marcaList where pathImagen is not null
        defaultMarcaFiltering("pathImagen.specified=true", "pathImagen.specified=false");
    }

    @Test
    @Transactional
    void getAllMarcasByPathImagenContainsSomething() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        // Get all the marcaList where pathImagen contains
        defaultMarcaFiltering("pathImagen.contains=" + DEFAULT_PATH_IMAGEN, "pathImagen.contains=" + UPDATED_PATH_IMAGEN);
    }

    @Test
    @Transactional
    void getAllMarcasByPathImagenNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        // Get all the marcaList where pathImagen does not contain
        defaultMarcaFiltering("pathImagen.doesNotContain=" + UPDATED_PATH_IMAGEN, "pathImagen.doesNotContain=" + DEFAULT_PATH_IMAGEN);
    }

    private void defaultMarcaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMarcaShouldBeFound(shouldBeFound);
        defaultMarcaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMarcaShouldBeFound(String filter) throws Exception {
        restMarcaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marca.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].pathImagen").value(hasItem(DEFAULT_PATH_IMAGEN)));

        // Check, that the count call also returns 1
        restMarcaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMarcaShouldNotBeFound(String filter) throws Exception {
        restMarcaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMarcaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMarca() throws Exception {
        // Get the marca
        restMarcaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMarca() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the marca
        Marca updatedMarca = marcaRepository.findById(marca.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMarca are not directly saved in db
        em.detach(updatedMarca);
        updatedMarca.noCia(UPDATED_NO_CIA).nombre(UPDATED_NOMBRE).pathImagen(UPDATED_PATH_IMAGEN);
        MarcaDTO marcaDTO = marcaMapper.toDto(updatedMarca);

        restMarcaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, marcaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(marcaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMarcaToMatchAllProperties(updatedMarca);
    }

    @Test
    @Transactional
    void putNonExistingMarca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marca.setId(longCount.incrementAndGet());

        // Create the Marca
        MarcaDTO marcaDTO = marcaMapper.toDto(marca);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarcaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, marcaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(marcaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMarca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marca.setId(longCount.incrementAndGet());

        // Create the Marca
        MarcaDTO marcaDTO = marcaMapper.toDto(marca);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarcaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(marcaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMarca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marca.setId(longCount.incrementAndGet());

        // Create the Marca
        MarcaDTO marcaDTO = marcaMapper.toDto(marca);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarcaMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marcaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMarcaWithPatch() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the marca using partial update
        Marca partialUpdatedMarca = new Marca();
        partialUpdatedMarca.setId(marca.getId());

        partialUpdatedMarca.noCia(UPDATED_NO_CIA);

        restMarcaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarca.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMarca))
            )
            .andExpect(status().isOk());

        // Validate the Marca in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMarcaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMarca, marca), getPersistedMarca(marca));
    }

    @Test
    @Transactional
    void fullUpdateMarcaWithPatch() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the marca using partial update
        Marca partialUpdatedMarca = new Marca();
        partialUpdatedMarca.setId(marca.getId());

        partialUpdatedMarca.noCia(UPDATED_NO_CIA).nombre(UPDATED_NOMBRE).pathImagen(UPDATED_PATH_IMAGEN);

        restMarcaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarca.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMarca))
            )
            .andExpect(status().isOk());

        // Validate the Marca in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMarcaUpdatableFieldsEquals(partialUpdatedMarca, getPersistedMarca(partialUpdatedMarca));
    }

    @Test
    @Transactional
    void patchNonExistingMarca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marca.setId(longCount.incrementAndGet());

        // Create the Marca
        MarcaDTO marcaDTO = marcaMapper.toDto(marca);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarcaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, marcaDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(marcaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMarca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marca.setId(longCount.incrementAndGet());

        // Create the Marca
        MarcaDTO marcaDTO = marcaMapper.toDto(marca);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarcaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(marcaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMarca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marca.setId(longCount.incrementAndGet());

        // Create the Marca
        MarcaDTO marcaDTO = marcaMapper.toDto(marca);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarcaMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(marcaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMarca() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.saveAndFlush(marca);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the marca
        restMarcaMockMvc
            .perform(delete(ENTITY_API_URL_ID, marca.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return marcaRepository.count();
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

    protected Marca getPersistedMarca(Marca marca) {
        return marcaRepository.findById(marca.getId()).orElseThrow();
    }

    protected void assertPersistedMarcaToMatchAllProperties(Marca expectedMarca) {
        assertMarcaAllPropertiesEquals(expectedMarca, getPersistedMarca(expectedMarca));
    }

    protected void assertPersistedMarcaToMatchUpdatableProperties(Marca expectedMarca) {
        assertMarcaAllUpdatablePropertiesEquals(expectedMarca, getPersistedMarca(expectedMarca));
    }
}
