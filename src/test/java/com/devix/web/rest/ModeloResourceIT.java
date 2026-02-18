package com.devix.web.rest;

import static com.devix.domain.ModeloAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.Marca;
import com.devix.domain.Modelo;
import com.devix.repository.ModeloRepository;
import com.devix.service.dto.ModeloDTO;
import com.devix.service.mapper.ModeloMapper;
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
 * Integration tests for the {@link ModeloResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ModeloResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_PATH_IMAGEN = "AAAAAAAAAA";
    private static final String UPDATED_PATH_IMAGEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/modelos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private ModeloMapper modeloMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModeloMockMvc;

    private Modelo modelo;

    private Modelo insertedModelo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Modelo createEntity() {
        return new Modelo().noCia(DEFAULT_NO_CIA).nombre(DEFAULT_NOMBRE).pathImagen(DEFAULT_PATH_IMAGEN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Modelo createUpdatedEntity() {
        return new Modelo().noCia(UPDATED_NO_CIA).nombre(UPDATED_NOMBRE).pathImagen(UPDATED_PATH_IMAGEN);
    }

    @BeforeEach
    void initTest() {
        modelo = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedModelo != null) {
            modeloRepository.delete(insertedModelo);
            insertedModelo = null;
        }
    }

    @Test
    @Transactional
    void createModelo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Modelo
        ModeloDTO modeloDTO = modeloMapper.toDto(modelo);
        var returnedModeloDTO = om.readValue(
            restModeloMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modeloDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ModeloDTO.class
        );

        // Validate the Modelo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedModelo = modeloMapper.toEntity(returnedModeloDTO);
        assertModeloUpdatableFieldsEquals(returnedModelo, getPersistedModelo(returnedModelo));

        insertedModelo = returnedModelo;
    }

    @Test
    @Transactional
    void createModeloWithExistingId() throws Exception {
        // Create the Modelo with an existing ID
        modelo.setId(1L);
        ModeloDTO modeloDTO = modeloMapper.toDto(modelo);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restModeloMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modeloDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Modelo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        modelo.setNoCia(null);

        // Create the Modelo, which fails.
        ModeloDTO modeloDTO = modeloMapper.toDto(modelo);

        restModeloMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modeloDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        modelo.setNombre(null);

        // Create the Modelo, which fails.
        ModeloDTO modeloDTO = modeloMapper.toDto(modelo);

        restModeloMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modeloDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPathImagenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        modelo.setPathImagen(null);

        // Create the Modelo, which fails.
        ModeloDTO modeloDTO = modeloMapper.toDto(modelo);

        restModeloMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modeloDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllModelos() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        // Get all the modeloList
        restModeloMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modelo.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].pathImagen").value(hasItem(DEFAULT_PATH_IMAGEN)));
    }

    @Test
    @Transactional
    void getModelo() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        // Get the modelo
        restModeloMockMvc
            .perform(get(ENTITY_API_URL_ID, modelo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(modelo.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.pathImagen").value(DEFAULT_PATH_IMAGEN));
    }

    @Test
    @Transactional
    void getModelosByIdFiltering() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        Long id = modelo.getId();

        defaultModeloFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultModeloFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultModeloFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllModelosByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        // Get all the modeloList where noCia equals to
        defaultModeloFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllModelosByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        // Get all the modeloList where noCia in
        defaultModeloFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllModelosByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        // Get all the modeloList where noCia is not null
        defaultModeloFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllModelosByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        // Get all the modeloList where noCia is greater than or equal to
        defaultModeloFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllModelosByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        // Get all the modeloList where noCia is less than or equal to
        defaultModeloFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllModelosByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        // Get all the modeloList where noCia is less than
        defaultModeloFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllModelosByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        // Get all the modeloList where noCia is greater than
        defaultModeloFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllModelosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        // Get all the modeloList where nombre equals to
        defaultModeloFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllModelosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        // Get all the modeloList where nombre in
        defaultModeloFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllModelosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        // Get all the modeloList where nombre is not null
        defaultModeloFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllModelosByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        // Get all the modeloList where nombre contains
        defaultModeloFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllModelosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        // Get all the modeloList where nombre does not contain
        defaultModeloFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllModelosByPathImagenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        // Get all the modeloList where pathImagen equals to
        defaultModeloFiltering("pathImagen.equals=" + DEFAULT_PATH_IMAGEN, "pathImagen.equals=" + UPDATED_PATH_IMAGEN);
    }

    @Test
    @Transactional
    void getAllModelosByPathImagenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        // Get all the modeloList where pathImagen in
        defaultModeloFiltering("pathImagen.in=" + DEFAULT_PATH_IMAGEN + "," + UPDATED_PATH_IMAGEN, "pathImagen.in=" + UPDATED_PATH_IMAGEN);
    }

    @Test
    @Transactional
    void getAllModelosByPathImagenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        // Get all the modeloList where pathImagen is not null
        defaultModeloFiltering("pathImagen.specified=true", "pathImagen.specified=false");
    }

    @Test
    @Transactional
    void getAllModelosByPathImagenContainsSomething() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        // Get all the modeloList where pathImagen contains
        defaultModeloFiltering("pathImagen.contains=" + DEFAULT_PATH_IMAGEN, "pathImagen.contains=" + UPDATED_PATH_IMAGEN);
    }

    @Test
    @Transactional
    void getAllModelosByPathImagenNotContainsSomething() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        // Get all the modeloList where pathImagen does not contain
        defaultModeloFiltering("pathImagen.doesNotContain=" + UPDATED_PATH_IMAGEN, "pathImagen.doesNotContain=" + DEFAULT_PATH_IMAGEN);
    }

    @Test
    @Transactional
    void getAllModelosByMarcaIsEqualToSomething() throws Exception {
        Marca marca;
        if (TestUtil.findAll(em, Marca.class).isEmpty()) {
            modeloRepository.saveAndFlush(modelo);
            marca = MarcaResourceIT.createEntity();
        } else {
            marca = TestUtil.findAll(em, Marca.class).get(0);
        }
        em.persist(marca);
        em.flush();
        modelo.setMarca(marca);
        modeloRepository.saveAndFlush(modelo);
        Long marcaId = marca.getId();
        // Get all the modeloList where marca equals to marcaId
        defaultModeloShouldBeFound("marcaId.equals=" + marcaId);

        // Get all the modeloList where marca equals to (marcaId + 1)
        defaultModeloShouldNotBeFound("marcaId.equals=" + (marcaId + 1));
    }

    private void defaultModeloFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultModeloShouldBeFound(shouldBeFound);
        defaultModeloShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultModeloShouldBeFound(String filter) throws Exception {
        restModeloMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modelo.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].pathImagen").value(hasItem(DEFAULT_PATH_IMAGEN)));

        // Check, that the count call also returns 1
        restModeloMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultModeloShouldNotBeFound(String filter) throws Exception {
        restModeloMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restModeloMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingModelo() throws Exception {
        // Get the modelo
        restModeloMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingModelo() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the modelo
        Modelo updatedModelo = modeloRepository.findById(modelo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedModelo are not directly saved in db
        em.detach(updatedModelo);
        updatedModelo.noCia(UPDATED_NO_CIA).nombre(UPDATED_NOMBRE).pathImagen(UPDATED_PATH_IMAGEN);
        ModeloDTO modeloDTO = modeloMapper.toDto(updatedModelo);

        restModeloMockMvc
            .perform(
                put(ENTITY_API_URL_ID, modeloDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(modeloDTO))
            )
            .andExpect(status().isOk());

        // Validate the Modelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedModeloToMatchAllProperties(updatedModelo);
    }

    @Test
    @Transactional
    void putNonExistingModelo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        modelo.setId(longCount.incrementAndGet());

        // Create the Modelo
        ModeloDTO modeloDTO = modeloMapper.toDto(modelo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModeloMockMvc
            .perform(
                put(ENTITY_API_URL_ID, modeloDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(modeloDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Modelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchModelo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        modelo.setId(longCount.incrementAndGet());

        // Create the Modelo
        ModeloDTO modeloDTO = modeloMapper.toDto(modelo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModeloMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(modeloDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Modelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamModelo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        modelo.setId(longCount.incrementAndGet());

        // Create the Modelo
        ModeloDTO modeloDTO = modeloMapper.toDto(modelo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModeloMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modeloDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Modelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateModeloWithPatch() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the modelo using partial update
        Modelo partialUpdatedModelo = new Modelo();
        partialUpdatedModelo.setId(modelo.getId());

        restModeloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModelo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedModelo))
            )
            .andExpect(status().isOk());

        // Validate the Modelo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertModeloUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedModelo, modelo), getPersistedModelo(modelo));
    }

    @Test
    @Transactional
    void fullUpdateModeloWithPatch() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the modelo using partial update
        Modelo partialUpdatedModelo = new Modelo();
        partialUpdatedModelo.setId(modelo.getId());

        partialUpdatedModelo.noCia(UPDATED_NO_CIA).nombre(UPDATED_NOMBRE).pathImagen(UPDATED_PATH_IMAGEN);

        restModeloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModelo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedModelo))
            )
            .andExpect(status().isOk());

        // Validate the Modelo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertModeloUpdatableFieldsEquals(partialUpdatedModelo, getPersistedModelo(partialUpdatedModelo));
    }

    @Test
    @Transactional
    void patchNonExistingModelo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        modelo.setId(longCount.incrementAndGet());

        // Create the Modelo
        ModeloDTO modeloDTO = modeloMapper.toDto(modelo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModeloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, modeloDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(modeloDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Modelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchModelo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        modelo.setId(longCount.incrementAndGet());

        // Create the Modelo
        ModeloDTO modeloDTO = modeloMapper.toDto(modelo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModeloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(modeloDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Modelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamModelo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        modelo.setId(longCount.incrementAndGet());

        // Create the Modelo
        ModeloDTO modeloDTO = modeloMapper.toDto(modelo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModeloMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(modeloDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Modelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteModelo() throws Exception {
        // Initialize the database
        insertedModelo = modeloRepository.saveAndFlush(modelo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the modelo
        restModeloMockMvc
            .perform(delete(ENTITY_API_URL_ID, modelo.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return modeloRepository.count();
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

    protected Modelo getPersistedModelo(Modelo modelo) {
        return modeloRepository.findById(modelo.getId()).orElseThrow();
    }

    protected void assertPersistedModeloToMatchAllProperties(Modelo expectedModelo) {
        assertModeloAllPropertiesEquals(expectedModelo, getPersistedModelo(expectedModelo));
    }

    protected void assertPersistedModeloToMatchUpdatableProperties(Modelo expectedModelo) {
        assertModeloAllUpdatablePropertiesEquals(expectedModelo, getPersistedModelo(expectedModelo));
    }
}
