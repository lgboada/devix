package com.devix.web.rest;

import com.devix.repository.UsuarioCentroRepository;
import com.devix.service.UsuarioCentroQueryService;
import com.devix.service.UsuarioCentroService;
import com.devix.service.criteria.UsuarioCentroCriteria;
import com.devix.service.dto.UsuarioCentroDTO;
import com.devix.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.devix.domain.UsuarioCentro}.
 */
@RestController
@RequestMapping("/api/usuario-centros")
public class UsuarioCentroResource {

    private static final Logger LOG = LoggerFactory.getLogger(UsuarioCentroResource.class);

    private static final String ENTITY_NAME = "usuarioCentro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UsuarioCentroService usuarioCentroService;

    private final UsuarioCentroRepository usuarioCentroRepository;

    private final UsuarioCentroQueryService usuarioCentroQueryService;

    public UsuarioCentroResource(
        UsuarioCentroService usuarioCentroService,
        UsuarioCentroRepository usuarioCentroRepository,
        UsuarioCentroQueryService usuarioCentroQueryService
    ) {
        this.usuarioCentroService = usuarioCentroService;
        this.usuarioCentroRepository = usuarioCentroRepository;
        this.usuarioCentroQueryService = usuarioCentroQueryService;
    }

    /**
     * {@code POST  /usuario-centros} : Create a new usuarioCentro.
     *
     * @param usuarioCentroDTO the usuarioCentroDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new usuarioCentroDTO, or with status {@code 400 (Bad Request)} if the usuarioCentro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UsuarioCentroDTO> createUsuarioCentro(@Valid @RequestBody UsuarioCentroDTO usuarioCentroDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save UsuarioCentro : {}", usuarioCentroDTO);
        if (usuarioCentroDTO.getId() != null) {
            throw new BadRequestAlertException("A new usuarioCentro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        usuarioCentroDTO = usuarioCentroService.save(usuarioCentroDTO);
        return ResponseEntity.created(new URI("/api/usuario-centros/" + usuarioCentroDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, usuarioCentroDTO.getId().toString()))
            .body(usuarioCentroDTO);
    }

    /**
     * {@code PUT  /usuario-centros/:id} : Updates an existing usuarioCentro.
     *
     * @param id the id of the usuarioCentroDTO to save.
     * @param usuarioCentroDTO the usuarioCentroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuarioCentroDTO,
     * or with status {@code 400 (Bad Request)} if the usuarioCentroDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the usuarioCentroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioCentroDTO> updateUsuarioCentro(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UsuarioCentroDTO usuarioCentroDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UsuarioCentro : {}, {}", id, usuarioCentroDTO);
        if (usuarioCentroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuarioCentroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usuarioCentroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        usuarioCentroDTO = usuarioCentroService.update(usuarioCentroDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, usuarioCentroDTO.getId().toString()))
            .body(usuarioCentroDTO);
    }

    /**
     * {@code PATCH  /usuario-centros/:id} : Partial updates given fields of an existing usuarioCentro, field will ignore if it is null
     *
     * @param id the id of the usuarioCentroDTO to save.
     * @param usuarioCentroDTO the usuarioCentroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuarioCentroDTO,
     * or with status {@code 400 (Bad Request)} if the usuarioCentroDTO is not valid,
     * or with status {@code 404 (Not Found)} if the usuarioCentroDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the usuarioCentroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UsuarioCentroDTO> partialUpdateUsuarioCentro(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UsuarioCentroDTO usuarioCentroDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UsuarioCentro partially : {}, {}", id, usuarioCentroDTO);
        if (usuarioCentroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuarioCentroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usuarioCentroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UsuarioCentroDTO> result = usuarioCentroService.partialUpdate(usuarioCentroDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, usuarioCentroDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /usuario-centros} : get all the usuarioCentros.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of usuarioCentros in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UsuarioCentroDTO>> getAllUsuarioCentros(UsuarioCentroCriteria criteria) {
        LOG.debug("REST request to get UsuarioCentros by criteria: {}", criteria);

        List<UsuarioCentroDTO> entityList = usuarioCentroQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /usuario-centros/count} : count all the usuarioCentros.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUsuarioCentros(UsuarioCentroCriteria criteria) {
        LOG.debug("REST request to count UsuarioCentros by criteria: {}", criteria);
        return ResponseEntity.ok().body(usuarioCentroQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /usuario-centros/:id} : get the "id" usuarioCentro.
     *
     * @param id the id of the usuarioCentroDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the usuarioCentroDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioCentroDTO> getUsuarioCentro(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UsuarioCentro : {}", id);
        Optional<UsuarioCentroDTO> usuarioCentroDTO = usuarioCentroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(usuarioCentroDTO);
    }

    /**
     * {@code DELETE  /usuario-centros/:id} : delete the "id" usuarioCentro.
     *
     * @param id the id of the usuarioCentroDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuarioCentro(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UsuarioCentro : {}", id);
        usuarioCentroService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
