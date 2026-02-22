package com.devix.web.rest;

import static com.devix.domain.UsuarioCentroAsserts.*;
import static com.devix.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.Centro;
import com.devix.domain.User;
import com.devix.domain.UsuarioCentro;
import com.devix.repository.UserRepository;
import com.devix.repository.UsuarioCentroRepository;
import com.devix.service.UsuarioCentroService;
import com.devix.service.dto.UsuarioCentroDTO;
import com.devix.service.mapper.UsuarioCentroMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UsuarioCentroResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UsuarioCentroResourceIT {

    private static final Long DEFAULT_NO_CIA = 1L;
    private static final Long UPDATED_NO_CIA = 2L;
    private static final Long SMALLER_NO_CIA = 1L - 1L;

    private static final Boolean DEFAULT_PRINCIPAL = false;
    private static final Boolean UPDATED_PRINCIPAL = true;

    private static final String ENTITY_API_URL = "/api/usuario-centros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UsuarioCentroRepository usuarioCentroRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private UsuarioCentroRepository usuarioCentroRepositoryMock;

    @Autowired
    private UsuarioCentroMapper usuarioCentroMapper;

    @Mock
    private UsuarioCentroService usuarioCentroServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsuarioCentroMockMvc;

    private UsuarioCentro usuarioCentro;

    private UsuarioCentro insertedUsuarioCentro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UsuarioCentro createEntity() {
        return new UsuarioCentro().noCia(DEFAULT_NO_CIA).principal(DEFAULT_PRINCIPAL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UsuarioCentro createUpdatedEntity() {
        return new UsuarioCentro().noCia(UPDATED_NO_CIA).principal(UPDATED_PRINCIPAL);
    }

    @BeforeEach
    void initTest() {
        usuarioCentro = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUsuarioCentro != null) {
            usuarioCentroRepository.delete(insertedUsuarioCentro);
            insertedUsuarioCentro = null;
        }
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void createUsuarioCentro() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UsuarioCentro
        UsuarioCentroDTO usuarioCentroDTO = usuarioCentroMapper.toDto(usuarioCentro);
        var returnedUsuarioCentroDTO = om.readValue(
            restUsuarioCentroMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(usuarioCentroDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UsuarioCentroDTO.class
        );

        // Validate the UsuarioCentro in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUsuarioCentro = usuarioCentroMapper.toEntity(returnedUsuarioCentroDTO);
        assertUsuarioCentroUpdatableFieldsEquals(returnedUsuarioCentro, getPersistedUsuarioCentro(returnedUsuarioCentro));

        insertedUsuarioCentro = returnedUsuarioCentro;
    }

    @Test
    @Transactional
    void createUsuarioCentroWithExistingId() throws Exception {
        // Create the UsuarioCentro with an existing ID
        usuarioCentro.setId(1L);
        UsuarioCentroDTO usuarioCentroDTO = usuarioCentroMapper.toDto(usuarioCentro);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsuarioCentroMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuarioCentroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioCentro in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoCiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        usuarioCentro.setNoCia(null);

        // Create the UsuarioCentro, which fails.
        UsuarioCentroDTO usuarioCentroDTO = usuarioCentroMapper.toDto(usuarioCentro);

        restUsuarioCentroMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuarioCentroDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrincipalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        usuarioCentro.setPrincipal(null);

        // Create the UsuarioCentro, which fails.
        UsuarioCentroDTO usuarioCentroDTO = usuarioCentroMapper.toDto(usuarioCentro);

        restUsuarioCentroMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuarioCentroDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUsuarioCentros() throws Exception {
        // Initialize the database
        insertedUsuarioCentro = usuarioCentroRepository.saveAndFlush(usuarioCentro);

        // Get all the usuarioCentroList
        restUsuarioCentroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuarioCentro.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].principal").value(hasItem(DEFAULT_PRINCIPAL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUsuarioCentrosWithEagerRelationshipsIsEnabled() throws Exception {
        when(usuarioCentroServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUsuarioCentroMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(usuarioCentroServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUsuarioCentrosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(usuarioCentroServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUsuarioCentroMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(usuarioCentroRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUsuarioCentro() throws Exception {
        // Initialize the database
        insertedUsuarioCentro = usuarioCentroRepository.saveAndFlush(usuarioCentro);

        // Get the usuarioCentro
        restUsuarioCentroMockMvc
            .perform(get(ENTITY_API_URL_ID, usuarioCentro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(usuarioCentro.getId().intValue()))
            .andExpect(jsonPath("$.noCia").value(DEFAULT_NO_CIA.intValue()))
            .andExpect(jsonPath("$.principal").value(DEFAULT_PRINCIPAL));
    }

    @Test
    @Transactional
    void getUsuarioCentrosByIdFiltering() throws Exception {
        // Initialize the database
        insertedUsuarioCentro = usuarioCentroRepository.saveAndFlush(usuarioCentro);

        Long id = usuarioCentro.getId();

        defaultUsuarioCentroFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUsuarioCentroFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUsuarioCentroFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUsuarioCentrosByNoCiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUsuarioCentro = usuarioCentroRepository.saveAndFlush(usuarioCentro);

        // Get all the usuarioCentroList where noCia equals to
        defaultUsuarioCentroFiltering("noCia.equals=" + DEFAULT_NO_CIA, "noCia.equals=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllUsuarioCentrosByNoCiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUsuarioCentro = usuarioCentroRepository.saveAndFlush(usuarioCentro);

        // Get all the usuarioCentroList where noCia in
        defaultUsuarioCentroFiltering("noCia.in=" + DEFAULT_NO_CIA + "," + UPDATED_NO_CIA, "noCia.in=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllUsuarioCentrosByNoCiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUsuarioCentro = usuarioCentroRepository.saveAndFlush(usuarioCentro);

        // Get all the usuarioCentroList where noCia is not null
        defaultUsuarioCentroFiltering("noCia.specified=true", "noCia.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuarioCentrosByNoCiaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUsuarioCentro = usuarioCentroRepository.saveAndFlush(usuarioCentro);

        // Get all the usuarioCentroList where noCia is greater than or equal to
        defaultUsuarioCentroFiltering("noCia.greaterThanOrEqual=" + DEFAULT_NO_CIA, "noCia.greaterThanOrEqual=" + UPDATED_NO_CIA);
    }

    @Test
    @Transactional
    void getAllUsuarioCentrosByNoCiaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUsuarioCentro = usuarioCentroRepository.saveAndFlush(usuarioCentro);

        // Get all the usuarioCentroList where noCia is less than or equal to
        defaultUsuarioCentroFiltering("noCia.lessThanOrEqual=" + DEFAULT_NO_CIA, "noCia.lessThanOrEqual=" + SMALLER_NO_CIA);
    }

    @Test
    @Transactional
    void getAllUsuarioCentrosByNoCiaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedUsuarioCentro = usuarioCentroRepository.saveAndFlush(usuarioCentro);

        // Get all the usuarioCentroList where noCia is less than
        defaultUsuarioCentroFiltering("noCia.lessThan=" + UPDATED_NO_CIA, "noCia.lessThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllUsuarioCentrosByNoCiaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedUsuarioCentro = usuarioCentroRepository.saveAndFlush(usuarioCentro);

        // Get all the usuarioCentroList where noCia is greater than
        defaultUsuarioCentroFiltering("noCia.greaterThan=" + SMALLER_NO_CIA, "noCia.greaterThan=" + DEFAULT_NO_CIA);
    }

    @Test
    @Transactional
    void getAllUsuarioCentrosByPrincipalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUsuarioCentro = usuarioCentroRepository.saveAndFlush(usuarioCentro);

        // Get all the usuarioCentroList where principal equals to
        defaultUsuarioCentroFiltering("principal.equals=" + DEFAULT_PRINCIPAL, "principal.equals=" + UPDATED_PRINCIPAL);
    }

    @Test
    @Transactional
    void getAllUsuarioCentrosByPrincipalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUsuarioCentro = usuarioCentroRepository.saveAndFlush(usuarioCentro);

        // Get all the usuarioCentroList where principal in
        defaultUsuarioCentroFiltering("principal.in=" + DEFAULT_PRINCIPAL + "," + UPDATED_PRINCIPAL, "principal.in=" + UPDATED_PRINCIPAL);
    }

    @Test
    @Transactional
    void getAllUsuarioCentrosByPrincipalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUsuarioCentro = usuarioCentroRepository.saveAndFlush(usuarioCentro);

        // Get all the usuarioCentroList where principal is not null
        defaultUsuarioCentroFiltering("principal.specified=true", "principal.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuarioCentrosByCentroIsEqualToSomething() throws Exception {
        Centro centro;
        if (TestUtil.findAll(em, Centro.class).isEmpty()) {
            usuarioCentroRepository.saveAndFlush(usuarioCentro);
            centro = CentroResourceIT.createEntity();
        } else {
            centro = TestUtil.findAll(em, Centro.class).get(0);
        }
        em.persist(centro);
        em.flush();
        usuarioCentro.setCentro(centro);
        usuarioCentroRepository.saveAndFlush(usuarioCentro);
        Long centroId = centro.getId();
        // Get all the usuarioCentroList where centro equals to centroId
        defaultUsuarioCentroShouldBeFound("centroId.equals=" + centroId);

        // Get all the usuarioCentroList where centro equals to (centroId + 1)
        defaultUsuarioCentroShouldNotBeFound("centroId.equals=" + (centroId + 1));
    }

    @Test
    @Transactional
    void getAllUsuarioCentrosByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            usuarioCentroRepository.saveAndFlush(usuarioCentro);
            user = UserResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        usuarioCentro.setUser(user);
        usuarioCentroRepository.saveAndFlush(usuarioCentro);
        String userId = user.getId();
        // Get all the usuarioCentroList where user equals to userId
        defaultUsuarioCentroShouldBeFound("userId.equals=" + userId);

        // Get all the usuarioCentroList where user equals to "invalid-id"
        defaultUsuarioCentroShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    private void defaultUsuarioCentroFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUsuarioCentroShouldBeFound(shouldBeFound);
        defaultUsuarioCentroShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsuarioCentroShouldBeFound(String filter) throws Exception {
        restUsuarioCentroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuarioCentro.getId().intValue())))
            .andExpect(jsonPath("$.[*].noCia").value(hasItem(DEFAULT_NO_CIA.intValue())))
            .andExpect(jsonPath("$.[*].principal").value(hasItem(DEFAULT_PRINCIPAL)));

        // Check, that the count call also returns 1
        restUsuarioCentroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsuarioCentroShouldNotBeFound(String filter) throws Exception {
        restUsuarioCentroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsuarioCentroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUsuarioCentro() throws Exception {
        // Get the usuarioCentro
        restUsuarioCentroMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUsuarioCentro() throws Exception {
        // Initialize the database
        insertedUsuarioCentro = usuarioCentroRepository.saveAndFlush(usuarioCentro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the usuarioCentro
        UsuarioCentro updatedUsuarioCentro = usuarioCentroRepository.findById(usuarioCentro.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUsuarioCentro are not directly saved in db
        em.detach(updatedUsuarioCentro);
        updatedUsuarioCentro.noCia(UPDATED_NO_CIA).principal(UPDATED_PRINCIPAL);
        UsuarioCentroDTO usuarioCentroDTO = usuarioCentroMapper.toDto(updatedUsuarioCentro);

        restUsuarioCentroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuarioCentroDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(usuarioCentroDTO))
            )
            .andExpect(status().isOk());

        // Validate the UsuarioCentro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUsuarioCentroToMatchAllProperties(updatedUsuarioCentro);
    }

    @Test
    @Transactional
    void putNonExistingUsuarioCentro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuarioCentro.setId(longCount.incrementAndGet());

        // Create the UsuarioCentro
        UsuarioCentroDTO usuarioCentroDTO = usuarioCentroMapper.toDto(usuarioCentro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioCentroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuarioCentroDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(usuarioCentroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioCentro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsuarioCentro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuarioCentro.setId(longCount.incrementAndGet());

        // Create the UsuarioCentro
        UsuarioCentroDTO usuarioCentroDTO = usuarioCentroMapper.toDto(usuarioCentro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioCentroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(usuarioCentroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioCentro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsuarioCentro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuarioCentro.setId(longCount.incrementAndGet());

        // Create the UsuarioCentro
        UsuarioCentroDTO usuarioCentroDTO = usuarioCentroMapper.toDto(usuarioCentro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioCentroMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuarioCentroDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UsuarioCentro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsuarioCentroWithPatch() throws Exception {
        // Initialize the database
        insertedUsuarioCentro = usuarioCentroRepository.saveAndFlush(usuarioCentro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the usuarioCentro using partial update
        UsuarioCentro partialUpdatedUsuarioCentro = new UsuarioCentro();
        partialUpdatedUsuarioCentro.setId(usuarioCentro.getId());

        restUsuarioCentroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuarioCentro.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUsuarioCentro))
            )
            .andExpect(status().isOk());

        // Validate the UsuarioCentro in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUsuarioCentroUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUsuarioCentro, usuarioCentro),
            getPersistedUsuarioCentro(usuarioCentro)
        );
    }

    @Test
    @Transactional
    void fullUpdateUsuarioCentroWithPatch() throws Exception {
        // Initialize the database
        insertedUsuarioCentro = usuarioCentroRepository.saveAndFlush(usuarioCentro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the usuarioCentro using partial update
        UsuarioCentro partialUpdatedUsuarioCentro = new UsuarioCentro();
        partialUpdatedUsuarioCentro.setId(usuarioCentro.getId());

        partialUpdatedUsuarioCentro.noCia(UPDATED_NO_CIA).principal(UPDATED_PRINCIPAL);

        restUsuarioCentroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuarioCentro.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUsuarioCentro))
            )
            .andExpect(status().isOk());

        // Validate the UsuarioCentro in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUsuarioCentroUpdatableFieldsEquals(partialUpdatedUsuarioCentro, getPersistedUsuarioCentro(partialUpdatedUsuarioCentro));
    }

    @Test
    @Transactional
    void patchNonExistingUsuarioCentro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuarioCentro.setId(longCount.incrementAndGet());

        // Create the UsuarioCentro
        UsuarioCentroDTO usuarioCentroDTO = usuarioCentroMapper.toDto(usuarioCentro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioCentroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usuarioCentroDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(usuarioCentroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioCentro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsuarioCentro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuarioCentro.setId(longCount.incrementAndGet());

        // Create the UsuarioCentro
        UsuarioCentroDTO usuarioCentroDTO = usuarioCentroMapper.toDto(usuarioCentro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioCentroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(usuarioCentroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioCentro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsuarioCentro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuarioCentro.setId(longCount.incrementAndGet());

        // Create the UsuarioCentro
        UsuarioCentroDTO usuarioCentroDTO = usuarioCentroMapper.toDto(usuarioCentro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioCentroMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(usuarioCentroDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UsuarioCentro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsuarioCentro() throws Exception {
        // Initialize the database
        insertedUsuarioCentro = usuarioCentroRepository.saveAndFlush(usuarioCentro);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the usuarioCentro
        restUsuarioCentroMockMvc
            .perform(delete(ENTITY_API_URL_ID, usuarioCentro.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return usuarioCentroRepository.count();
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

    protected UsuarioCentro getPersistedUsuarioCentro(UsuarioCentro usuarioCentro) {
        return usuarioCentroRepository.findById(usuarioCentro.getId()).orElseThrow();
    }

    protected void assertPersistedUsuarioCentroToMatchAllProperties(UsuarioCentro expectedUsuarioCentro) {
        assertUsuarioCentroAllPropertiesEquals(expectedUsuarioCentro, getPersistedUsuarioCentro(expectedUsuarioCentro));
    }

    protected void assertPersistedUsuarioCentroToMatchUpdatableProperties(UsuarioCentro expectedUsuarioCentro) {
        assertUsuarioCentroAllUpdatablePropertiesEquals(expectedUsuarioCentro, getPersistedUsuarioCentro(expectedUsuarioCentro));
    }
}
