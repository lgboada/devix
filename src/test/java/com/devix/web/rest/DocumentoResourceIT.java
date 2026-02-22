package com.devix.web.rest;

import static com.devix.domain.DocumentoAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.Cliente;
import com.devix.domain.Documento;
import com.devix.domain.Evento;
import com.devix.repository.DocumentoRepository;
import com.devix.service.dto.DocumentoDTO;
import com.devix.service.mapper.DocumentoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link DocumentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentoResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_VENCIMIENTO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_VENCIMIENTO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/documentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private DocumentoMapper documentoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentoMockMvc;

    private Documento documento;

    private Documento insertedDocumento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Documento createEntity() {
        return new Documento()
            .noCia(DEFAULT_NO_CIA)
            .tipo(DEFAULT_TIPO)
            .observacion(DEFAULT_OBSERVACION)
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .fechaVencimiento(DEFAULT_FECHA_VENCIMIENTO)
            .path(DEFAULT_PATH);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Documento createUpdatedEntity() {
        return new Documento()
            .noCia(UPDATED_NO_CIA)
            .tipo(UPDATED_TIPO)
            .observacion(UPDATED_OBSERVACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .path(UPDATED_PATH);
    }

    @BeforeEach
    void initTest() {
        documento = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumento != null) {
            documentoRepository.delete(insertedDocumento);
            insertedDocumento = null;
        }
    }

    @Test
    @Transactional
    void createDocumento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);
        var returnedDocumentoDTO = om.readValue(
            restDocumentoMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentoDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentoDTO.class
        );

        // Validate the Documento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumento = documentoMapper.toEntity(returnedDocumentoDTO);
        assertDocumentoUpdatableFieldsEquals(returnedDocumento, getPersistedDocumento(returnedDocumento));

        insertedDocumento = returnedDocumento;
    }

    @Test
    @Transactional
    void createDocumentoWithExistingId() throws Exception {
        // Create the Documento with an existing ID
        documento.setId(1L);
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documento.setNoCia(null);

        // Create the Documento, which fails.
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        restDocumentoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPathIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documento.setPath(null);

        // Create the Documento, which fails.
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        restDocumentoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocumentos() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList
        restDocumentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documento.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].observacion").value(hasItem(DEFAULT_OBSERVACION)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].fechaVencimiento").value(hasItem(DEFAULT_FECHA_VENCIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)));
    }

    @Test
    @Transactional
    void getDocumento() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get the documento
        restDocumentoMockMvc
            .perform(get(ENTITY_API_URL_ID, documento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documento.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.observacion").value(DEFAULT_OBSERVACION))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.fechaVencimiento").value(DEFAULT_FECHA_VENCIMIENTO.toString()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH));
    }

    @Test
    @Transactional
    void getDocumentosByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        Long id = documento.getId();

        defaultDocumentoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentosByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where noCia equals to
        defaultDocumentoFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllDocumentosByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where noCia in
        defaultDocumentoFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllDocumentosByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where noCia is not null
        defaultDocumentoFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentosByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where noCia is greater than or equal to
        defaultDocumentoFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllDocumentosByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where noCia is less than or equal to
        defaultDocumentoFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllDocumentosByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where noCia is less than
        defaultDocumentoFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllDocumentosByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where noCia is greater than
        defaultDocumentoFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllDocumentosByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where tipo equals to
        defaultDocumentoFiltering("tipo.equals=" + DEFAULT_TIPO, "tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllDocumentosByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where tipo in
        defaultDocumentoFiltering("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO, "tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllDocumentosByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where tipo is not null
        defaultDocumentoFiltering("tipo.specified=true", "tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentosByTipoContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where tipo contains
        defaultDocumentoFiltering("tipo.contains=" + DEFAULT_TIPO, "tipo.contains=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllDocumentosByTipoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where tipo does not contain
        defaultDocumentoFiltering("tipo.doesNotContain=" + UPDATED_TIPO, "tipo.doesNotContain=" + DEFAULT_TIPO);
    }

    @Test
    @Transactional
    void getAllDocumentosByObservacionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where observacion equals to
        defaultDocumentoFiltering("observacion.equals=" + DEFAULT_OBSERVACION, "observacion.equals=" + UPDATED_OBSERVACION);
    }

    @Test
    @Transactional
    void getAllDocumentosByObservacionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where observacion in
        defaultDocumentoFiltering(
            "observacion.in=" + DEFAULT_OBSERVACION + "," + UPDATED_OBSERVACION,
            "observacion.in=" + UPDATED_OBSERVACION
        );
    }

    @Test
    @Transactional
    void getAllDocumentosByObservacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where observacion is not null
        defaultDocumentoFiltering("observacion.specified=true", "observacion.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentosByObservacionContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where observacion contains
        defaultDocumentoFiltering("observacion.contains=" + DEFAULT_OBSERVACION, "observacion.contains=" + UPDATED_OBSERVACION);
    }

    @Test
    @Transactional
    void getAllDocumentosByObservacionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where observacion does not contain
        defaultDocumentoFiltering("observacion.doesNotContain=" + UPDATED_OBSERVACION, "observacion.doesNotContain=" + DEFAULT_OBSERVACION);
    }

    @Test
    @Transactional
    void getAllDocumentosByFechaCreacionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where fechaCreacion equals to
        defaultDocumentoFiltering("fechaCreacion.equals=" + DEFAULT_FECHA_CREACION, "fechaCreacion.equals=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllDocumentosByFechaCreacionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where fechaCreacion in
        defaultDocumentoFiltering(
            "fechaCreacion.in=" + DEFAULT_FECHA_CREACION + "," + UPDATED_FECHA_CREACION,
            "fechaCreacion.in=" + UPDATED_FECHA_CREACION
        );
    }

    @Test
    @Transactional
    void getAllDocumentosByFechaCreacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where fechaCreacion is not null
        defaultDocumentoFiltering("fechaCreacion.specified=true", "fechaCreacion.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentosByFechaVencimientoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where fechaVencimiento equals to
        defaultDocumentoFiltering(
            "fechaVencimiento.equals=" + DEFAULT_FECHA_VENCIMIENTO,
            "fechaVencimiento.equals=" + UPDATED_FECHA_VENCIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllDocumentosByFechaVencimientoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where fechaVencimiento in
        defaultDocumentoFiltering(
            "fechaVencimiento.in=" + DEFAULT_FECHA_VENCIMIENTO + "," + UPDATED_FECHA_VENCIMIENTO,
            "fechaVencimiento.in=" + UPDATED_FECHA_VENCIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllDocumentosByFechaVencimientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where fechaVencimiento is not null
        defaultDocumentoFiltering("fechaVencimiento.specified=true", "fechaVencimiento.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentosByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where path equals to
        defaultDocumentoFiltering("path.equals=" + DEFAULT_PATH, "path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllDocumentosByPathIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where path in
        defaultDocumentoFiltering("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH, "path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllDocumentosByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where path is not null
        defaultDocumentoFiltering("path.specified=true", "path.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentosByPathContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where path contains
        defaultDocumentoFiltering("path.contains=" + DEFAULT_PATH, "path.contains=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllDocumentosByPathNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where path does not contain
        defaultDocumentoFiltering("path.doesNotContain=" + UPDATED_PATH, "path.doesNotContain=" + DEFAULT_PATH);
    }

    @Test
    @Transactional
    void getAllDocumentosByClienteIsEqualToSomething() throws Exception {
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            documentoRepository.saveAndFlush(documento);
            cliente = ClienteResourceIT.createEntity();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        em.persist(cliente);
        em.flush();
        documento.setCliente(cliente);
        documentoRepository.saveAndFlush(documento);
        Long clienteId = cliente.getId();
        // Get all the documentoList where cliente equals to clienteId
        defaultDocumentoShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the documentoList where cliente equals to (clienteId + 1)
        defaultDocumentoShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    @Test
    @Transactional
    void getAllDocumentosByEventoIsEqualToSomething() throws Exception {
        Evento evento;
        if (TestUtil.findAll(em, Evento.class).isEmpty()) {
            documentoRepository.saveAndFlush(documento);
            evento = EventoResourceIT.createEntity();
        } else {
            evento = TestUtil.findAll(em, Evento.class).get(0);
        }
        em.persist(evento);
        em.flush();
        documento.setEvento(evento);
        documentoRepository.saveAndFlush(documento);
        Long eventoId = evento.getId();
        // Get all the documentoList where evento equals to eventoId
        defaultDocumentoShouldBeFound("eventoId.equals=" + eventoId);

        // Get all the documentoList where evento equals to (eventoId + 1)
        defaultDocumentoShouldNotBeFound("eventoId.equals=" + (eventoId + 1));
    }

    private void defaultDocumentoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentoShouldBeFound(shouldBeFound);
        defaultDocumentoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentoShouldBeFound(String filter) throws Exception {
        restDocumentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documento.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].observacion").value(hasItem(DEFAULT_OBSERVACION)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].fechaVencimiento").value(hasItem(DEFAULT_FECHA_VENCIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)));

        // Check, that the count call also returns 1
        restDocumentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentoShouldNotBeFound(String filter) throws Exception {
        restDocumentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumento() throws Exception {
        // Get the documento
        restDocumentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumento() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documento
        Documento updatedDocumento = documentoRepository.findById(documento.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumento are not directly saved in db
        em.detach(updatedDocumento);
        updatedDocumento
            .noCia(UPDATED_NO_CIA)
            .tipo(UPDATED_TIPO)
            .observacion(UPDATED_OBSERVACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .path(UPDATED_PATH);
        DocumentoDTO documentoDTO = documentoMapper.toDto(updatedDocumento);

        restDocumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Documento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentoToMatchAllProperties(updatedDocumento);
    }

    @Test
    @Transactional
    void putNonExistingDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documento.setId(longCount.incrementAndGet());

        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documento.setId(longCount.incrementAndGet());

        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documento.setId(longCount.incrementAndGet());

        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Documento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentoWithPatch() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documento using partial update
        Documento partialUpdatedDocumento = new Documento();
        partialUpdatedDocumento.setId(documento.getId());

        partialUpdatedDocumento.noCia(UPDATED_NO_CIA).fechaVencimiento(UPDATED_FECHA_VENCIMIENTO);

        restDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumento.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumento))
            )
            .andExpect(status().isOk());

        // Validate the Documento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumento, documento),
            getPersistedDocumento(documento)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentoWithPatch() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documento using partial update
        Documento partialUpdatedDocumento = new Documento();
        partialUpdatedDocumento.setId(documento.getId());

        partialUpdatedDocumento
            .noCia(UPDATED_NO_CIA)
            .tipo(UPDATED_TIPO)
            .observacion(UPDATED_OBSERVACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .path(UPDATED_PATH);

        restDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumento.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumento))
            )
            .andExpect(status().isOk());

        // Validate the Documento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentoUpdatableFieldsEquals(partialUpdatedDocumento, getPersistedDocumento(partialUpdatedDocumento));
    }

    @Test
    @Transactional
    void patchNonExistingDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documento.setId(longCount.incrementAndGet());

        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documento.setId(longCount.incrementAndGet());

        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documento.setId(longCount.incrementAndGet());

        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Documento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumento() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documento
        restDocumentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, documento.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return documentoRepository.count();
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

    protected Documento getPersistedDocumento(Documento documento) {
        return documentoRepository.findById(documento.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentoToMatchAllProperties(Documento expectedDocumento) {
        assertDocumentoAllPropertiesEquals(expectedDocumento, getPersistedDocumento(expectedDocumento));
    }

    protected void assertPersistedDocumentoToMatchUpdatableProperties(Documento expectedDocumento) {
        assertDocumentoAllUpdatablePropertiesEquals(expectedDocumento, getPersistedDocumento(expectedDocumento));
    }
}
