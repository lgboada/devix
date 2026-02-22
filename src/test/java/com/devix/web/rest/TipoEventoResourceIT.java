package com.devix.web.rest;

import static com.devix.domain.TipoEventoAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.TipoEvento;
import com.devix.repository.TipoEventoRepository;
import com.devix.service.dto.TipoEventoDTO;
import com.devix.service.mapper.TipoEventoMapper;
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
 * Integration tests for the {@link TipoEventoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoEventoResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-eventos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TipoEventoRepository tipoEventoRepository;

    @Autowired
    private TipoEventoMapper tipoEventoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoEventoMockMvc;

    private TipoEvento tipoEvento;

    private TipoEvento insertedTipoEvento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoEvento createEntity() {
        return new TipoEvento().noCia(DEFAULT_NO_CIA).nombre(DEFAULT_NOMBRE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoEvento createUpdatedEntity() {
        return new TipoEvento().noCia(UPDATED_NO_CIA).nombre(UPDATED_NOMBRE);
    }

    @BeforeEach
    void initTest() {
        tipoEvento = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTipoEvento != null) {
            tipoEventoRepository.delete(insertedTipoEvento);
            insertedTipoEvento = null;
        }
    }

    @Test
    @Transactional
    void createTipoEvento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TipoEvento
        TipoEventoDTO tipoEventoDTO = tipoEventoMapper.toDto(tipoEvento);
        var returnedTipoEventoDTO = om.readValue(
            restTipoEventoMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoEventoDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TipoEventoDTO.class
        );

        // Validate the TipoEvento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTipoEvento = tipoEventoMapper.toEntity(returnedTipoEventoDTO);
        assertTipoEventoUpdatableFieldsEquals(returnedTipoEvento, getPersistedTipoEvento(returnedTipoEvento));

        insertedTipoEvento = returnedTipoEvento;
    }

    @Test
    @Transactional
    void createTipoEventoWithExistingId() throws Exception {
        // Create the TipoEvento with an existing ID
        tipoEvento.setId(1L);
        TipoEventoDTO tipoEventoDTO = tipoEventoMapper.toDto(tipoEvento);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoEventoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoEventoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoEvento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoEvento.setNoCia(null);

        // Create the TipoEvento, which fails.
        TipoEventoDTO tipoEventoDTO = tipoEventoMapper.toDto(tipoEvento);

        restTipoEventoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoEventoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoEvento.setNombre(null);

        // Create the TipoEvento, which fails.
        TipoEventoDTO tipoEventoDTO = tipoEventoMapper.toDto(tipoEvento);

        restTipoEventoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoEventoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipoEventos() throws Exception {
        // Initialize the database
        insertedTipoEvento = tipoEventoRepository.saveAndFlush(tipoEvento);

        // Get all the tipoEventoList
        restTipoEventoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoEvento.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    void getTipoEvento() throws Exception {
        // Initialize the database
        insertedTipoEvento = tipoEventoRepository.saveAndFlush(tipoEvento);

        // Get the tipoEvento
        restTipoEventoMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoEvento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoEvento.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    void getTipoEventosByIdFiltering() throws Exception {
        // Initialize the database
        insertedTipoEvento = tipoEventoRepository.saveAndFlush(tipoEvento);

        Long id = tipoEvento.getId();

        defaultTipoEventoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTipoEventoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTipoEventoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTipoEventosByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoEvento = tipoEventoRepository.saveAndFlush(tipoEvento);

        // Get all the tipoEventoList where noCia equals to
        defaultTipoEventoFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoEventosByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoEvento = tipoEventoRepository.saveAndFlush(tipoEvento);

        // Get all the tipoEventoList where noCia in
        defaultTipoEventoFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoEventosByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoEvento = tipoEventoRepository.saveAndFlush(tipoEvento);

        // Get all the tipoEventoList where noCia is not null
        defaultTipoEventoFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoEventosByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoEvento = tipoEventoRepository.saveAndFlush(tipoEvento);

        // Get all the tipoEventoList where noCia is greater than or equal to
        defaultTipoEventoFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoEventosByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoEvento = tipoEventoRepository.saveAndFlush(tipoEvento);

        // Get all the tipoEventoList where noCia is less than or equal to
        defaultTipoEventoFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoEventosByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTipoEvento = tipoEventoRepository.saveAndFlush(tipoEvento);

        // Get all the tipoEventoList where noCia is less than
        defaultTipoEventoFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoEventosByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTipoEvento = tipoEventoRepository.saveAndFlush(tipoEvento);

        // Get all the tipoEventoList where noCia is greater than
        defaultTipoEventoFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllTipoEventosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoEvento = tipoEventoRepository.saveAndFlush(tipoEvento);

        // Get all the tipoEventoList where nombre equals to
        defaultTipoEventoFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTipoEventosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoEvento = tipoEventoRepository.saveAndFlush(tipoEvento);

        // Get all the tipoEventoList where nombre in
        defaultTipoEventoFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTipoEventosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoEvento = tipoEventoRepository.saveAndFlush(tipoEvento);

        // Get all the tipoEventoList where nombre is not null
        defaultTipoEventoFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoEventosByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoEvento = tipoEventoRepository.saveAndFlush(tipoEvento);

        // Get all the tipoEventoList where nombre contains
        defaultTipoEventoFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTipoEventosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoEvento = tipoEventoRepository.saveAndFlush(tipoEvento);

        // Get all the tipoEventoList where nombre does not contain
        defaultTipoEventoFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    private void defaultTipoEventoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTipoEventoShouldBeFound(shouldBeFound);
        defaultTipoEventoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTipoEventoShouldBeFound(String filter) throws Exception {
        restTipoEventoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoEvento.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));

        // Check, that the count call also returns 1
        restTipoEventoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTipoEventoShouldNotBeFound(String filter) throws Exception {
        restTipoEventoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTipoEventoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTipoEvento() throws Exception {
        // Get the tipoEvento
        restTipoEventoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTipoEvento() throws Exception {
        // Initialize the database
        insertedTipoEvento = tipoEventoRepository.saveAndFlush(tipoEvento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoEvento
        TipoEvento updatedTipoEvento = tipoEventoRepository.findById(tipoEvento.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTipoEvento are not directly saved in db
        em.detach(updatedTipoEvento);
        updatedTipoEvento.noCia(UPDATED_NO_CIA).nombre(UPDATED_NOMBRE);
        TipoEventoDTO tipoEventoDTO = tipoEventoMapper.toDto(updatedTipoEvento);

        restTipoEventoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoEventoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoEventoDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoEvento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTipoEventoToMatchAllProperties(updatedTipoEvento);
    }

    @Test
    @Transactional
    void putNonExistingTipoEvento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoEvento.setId(longCount.incrementAndGet());

        // Create the TipoEvento
        TipoEventoDTO tipoEventoDTO = tipoEventoMapper.toDto(tipoEvento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoEventoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoEventoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoEventoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoEvento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoEvento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoEvento.setId(longCount.incrementAndGet());

        // Create the TipoEvento
        TipoEventoDTO tipoEventoDTO = tipoEventoMapper.toDto(tipoEvento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoEventoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoEventoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoEvento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoEvento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoEvento.setId(longCount.incrementAndGet());

        // Create the TipoEvento
        TipoEventoDTO tipoEventoDTO = tipoEventoMapper.toDto(tipoEvento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoEventoMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoEventoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoEvento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoEventoWithPatch() throws Exception {
        // Initialize the database
        insertedTipoEvento = tipoEventoRepository.saveAndFlush(tipoEvento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoEvento using partial update
        TipoEvento partialUpdatedTipoEvento = new TipoEvento();
        partialUpdatedTipoEvento.setId(tipoEvento.getId());

        partialUpdatedTipoEvento.nombre(UPDATED_NOMBRE);

        restTipoEventoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoEvento.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoEvento))
            )
            .andExpect(status().isOk());

        // Validate the TipoEvento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoEventoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTipoEvento, tipoEvento),
            getPersistedTipoEvento(tipoEvento)
        );
    }

    @Test
    @Transactional
    void fullUpdateTipoEventoWithPatch() throws Exception {
        // Initialize the database
        insertedTipoEvento = tipoEventoRepository.saveAndFlush(tipoEvento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoEvento using partial update
        TipoEvento partialUpdatedTipoEvento = new TipoEvento();
        partialUpdatedTipoEvento.setId(tipoEvento.getId());

        partialUpdatedTipoEvento.noCia(UPDATED_NO_CIA).nombre(UPDATED_NOMBRE);

        restTipoEventoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoEvento.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoEvento))
            )
            .andExpect(status().isOk());

        // Validate the TipoEvento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoEventoUpdatableFieldsEquals(partialUpdatedTipoEvento, getPersistedTipoEvento(partialUpdatedTipoEvento));
    }

    @Test
    @Transactional
    void patchNonExistingTipoEvento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoEvento.setId(longCount.incrementAndGet());

        // Create the TipoEvento
        TipoEventoDTO tipoEventoDTO = tipoEventoMapper.toDto(tipoEvento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoEventoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoEventoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoEventoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoEvento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoEvento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoEvento.setId(longCount.incrementAndGet());

        // Create the TipoEvento
        TipoEventoDTO tipoEventoDTO = tipoEventoMapper.toDto(tipoEvento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoEventoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoEventoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoEvento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoEvento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoEvento.setId(longCount.incrementAndGet());

        // Create the TipoEvento
        TipoEventoDTO tipoEventoDTO = tipoEventoMapper.toDto(tipoEvento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoEventoMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tipoEventoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoEvento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoEvento() throws Exception {
        // Initialize the database
        insertedTipoEvento = tipoEventoRepository.saveAndFlush(tipoEvento);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tipoEvento
        restTipoEventoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoEvento.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tipoEventoRepository.count();
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

    protected TipoEvento getPersistedTipoEvento(TipoEvento tipoEvento) {
        return tipoEventoRepository.findById(tipoEvento.getId()).orElseThrow();
    }

    protected void assertPersistedTipoEventoToMatchAllProperties(TipoEvento expectedTipoEvento) {
        assertTipoEventoAllPropertiesEquals(expectedTipoEvento, getPersistedTipoEvento(expectedTipoEvento));
    }

    protected void assertPersistedTipoEventoToMatchUpdatableProperties(TipoEvento expectedTipoEvento) {
        assertTipoEventoAllUpdatablePropertiesEquals(expectedTipoEvento, getPersistedTipoEvento(expectedTipoEvento));
    }
}
