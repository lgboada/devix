package com.devix.web.rest;

import static com.devix.domain.TipoProductoAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.TipoProducto;
import com.devix.repository.TipoProductoRepository;
import com.devix.service.dto.TipoProductoDTO;
import com.devix.service.mapper.TipoProductoMapper;
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
 * Integration tests for the {@link TipoProductoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoProductoResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-productos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    @Autowired
    private TipoProductoMapper tipoProductoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoProductoMockMvc;

    private TipoProducto tipoProducto;

    private TipoProducto insertedTipoProducto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoProducto createEntity() {
        return new TipoProducto().noCia(DEFAULT_NO_CIA).nombre(DEFAULT_NOMBRE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoProducto createUpdatedEntity() {
        return new TipoProducto().noCia(UPDATED_NO_CIA).nombre(UPDATED_NOMBRE);
    }

    @BeforeEach
    void initTest() {
        tipoProducto = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTipoProducto != null) {
            tipoProductoRepository.delete(insertedTipoProducto);
            insertedTipoProducto = null;
        }
    }

    @Test
    @Transactional
    void createTipoProducto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);
        var returnedTipoProductoDTO = om.readValue(
            restTipoProductoMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoProductoDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TipoProductoDTO.class
        );

        // Validate the TipoProducto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTipoProducto = tipoProductoMapper.toEntity(returnedTipoProductoDTO);
        assertTipoProductoUpdatableFieldsEquals(returnedTipoProducto, getPersistedTipoProducto(returnedTipoProducto));

        insertedTipoProducto = returnedTipoProducto;
    }

    @Test
    @Transactional
    void createTipoProductoWithExistingId() throws Exception {
        // Create the TipoProducto with an existing ID
        tipoProducto.setId(1L);
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoProductoMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoProducto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoProducto.setNoCia(null);

        // Create the TipoProducto, which fails.
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        restTipoProductoMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoProductoDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoProducto.setNombre(null);

        // Create the TipoProducto, which fails.
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        restTipoProductoMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoProductoDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipoProductos() throws Exception {
        // Initialize the database
        insertedTipoProducto = tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList
        restTipoProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoProducto.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    void getTipoProducto() throws Exception {
        // Initialize the database
        insertedTipoProducto = tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get the tipoProducto
        restTipoProductoMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoProducto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoProducto.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    void getTipoProductosByIdFiltering() throws Exception {
        // Initialize the database
        insertedTipoProducto = tipoProductoRepository.saveAndFlush(tipoProducto);

        Long id = tipoProducto.getId();

        defaultTipoProductoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTipoProductoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTipoProductoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTipoProductosByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoProducto = tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList where noCia equals to
        defaultTipoProductoFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoProductosByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoProducto = tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList where noCia in
        defaultTipoProductoFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoProductosByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoProducto = tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList where noCia is not null
        defaultTipoProductoFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoProductosByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoProducto = tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList where noCia is greater than or equal to
        defaultTipoProductoFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoProductosByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoProducto = tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList where noCia is less than or equal to
        defaultTipoProductoFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoProductosByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTipoProducto = tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList where noCia is less than
        defaultTipoProductoFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoProductosByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTipoProducto = tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList where noCia is greater than
        defaultTipoProductoFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoProductosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoProducto = tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList where nombre equals to
        defaultTipoProductoFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTipoProductosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoProducto = tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList where nombre in
        defaultTipoProductoFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTipoProductosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoProducto = tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList where nombre is not null
        defaultTipoProductoFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoProductosByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoProducto = tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList where nombre contains
        defaultTipoProductoFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTipoProductosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoProducto = tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList where nombre does not contain
        defaultTipoProductoFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    private void defaultTipoProductoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTipoProductoShouldBeFound(shouldBeFound);
        defaultTipoProductoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTipoProductoShouldBeFound(String filter) throws Exception {
        restTipoProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoProducto.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));

        // Check, that the count call also returns 1
        restTipoProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTipoProductoShouldNotBeFound(String filter) throws Exception {
        restTipoProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTipoProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTipoProducto() throws Exception {
        // Get the tipoProducto
        restTipoProductoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTipoProducto() throws Exception {
        // Initialize the database
        insertedTipoProducto = tipoProductoRepository.saveAndFlush(tipoProducto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoProducto
        TipoProducto updatedTipoProducto = tipoProductoRepository.findById(tipoProducto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTipoProducto are not directly saved in db
        em.detach(updatedTipoProducto);
        updatedTipoProducto.noCia(UPDATED_NO_CIA).nombre(UPDATED_NOMBRE);
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(updatedTipoProducto);

        restTipoProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoProductoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoProductoDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoProducto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTipoProductoToMatchAllProperties(updatedTipoProducto);
    }

    @Test
    @Transactional
    void putNonExistingTipoProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoProducto.setId(longCount.incrementAndGet());

        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoProductoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoProducto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoProducto.setId(longCount.incrementAndGet());

        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoProducto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoProducto.setId(longCount.incrementAndGet());

        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoProductoMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoProductoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoProducto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoProductoWithPatch() throws Exception {
        // Initialize the database
        insertedTipoProducto = tipoProductoRepository.saveAndFlush(tipoProducto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoProducto using partial update
        TipoProducto partialUpdatedTipoProducto = new TipoProducto();
        partialUpdatedTipoProducto.setId(tipoProducto.getId());

        partialUpdatedTipoProducto.nombre(UPDATED_NOMBRE);

        restTipoProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoProducto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoProducto))
            )
            .andExpect(status().isOk());

        // Validate the TipoProducto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoProductoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTipoProducto, tipoProducto),
            getPersistedTipoProducto(tipoProducto)
        );
    }

    @Test
    @Transactional
    void fullUpdateTipoProductoWithPatch() throws Exception {
        // Initialize the database
        insertedTipoProducto = tipoProductoRepository.saveAndFlush(tipoProducto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoProducto using partial update
        TipoProducto partialUpdatedTipoProducto = new TipoProducto();
        partialUpdatedTipoProducto.setId(tipoProducto.getId());

        partialUpdatedTipoProducto.noCia(UPDATED_NO_CIA).nombre(UPDATED_NOMBRE);

        restTipoProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoProducto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoProducto))
            )
            .andExpect(status().isOk());

        // Validate the TipoProducto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoProductoUpdatableFieldsEquals(partialUpdatedTipoProducto, getPersistedTipoProducto(partialUpdatedTipoProducto));
    }

    @Test
    @Transactional
    void patchNonExistingTipoProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoProducto.setId(longCount.incrementAndGet());

        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoProductoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoProducto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoProducto.setId(longCount.incrementAndGet());

        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoProducto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoProducto.setId(longCount.incrementAndGet());

        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoProductoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoProductoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoProducto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoProducto() throws Exception {
        // Initialize the database
        insertedTipoProducto = tipoProductoRepository.saveAndFlush(tipoProducto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tipoProducto
        restTipoProductoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoProducto.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tipoProductoRepository.count();
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

    protected TipoProducto getPersistedTipoProducto(TipoProducto tipoProducto) {
        return tipoProductoRepository.findById(tipoProducto.getId()).orElseThrow();
    }

    protected void assertPersistedTipoProductoToMatchAllProperties(TipoProducto expectedTipoProducto) {
        assertTipoProductoAllPropertiesEquals(expectedTipoProducto, getPersistedTipoProducto(expectedTipoProducto));
    }

    protected void assertPersistedTipoProductoToMatchUpdatableProperties(TipoProducto expectedTipoProducto) {
        assertTipoProductoAllUpdatablePropertiesEquals(expectedTipoProducto, getPersistedTipoProducto(expectedTipoProducto));
    }
}
