package com.devix.web.rest;

import com.devix.repository.TipoDireccionRepository;
import com.devix.service.TipoDireccionQueryService;
import com.devix.service.TipoDireccionService;
import com.devix.service.criteria.TipoDireccionCriteria;
import com.devix.service.dto.TipoDireccionDTO;
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
 * REST controller for managing {@link com.devix.domain.TipoDireccion}.
 */
@RestController
@RequestMapping("/api/tipo-direccions")
public class TipoDireccionResource {

    private static final Logger LOG = LoggerFactory.getLogger(TipoDireccionResource.class);

    private static final String ENTITY_NAME = "tipoDireccion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoDireccionService tipoDireccionService;

    private final TipoDireccionRepository tipoDireccionRepository;

    private final TipoDireccionQueryService tipoDireccionQueryService;

    public TipoDireccionResource(
        TipoDireccionService tipoDireccionService,
        TipoDireccionRepository tipoDireccionRepository,
        TipoDireccionQueryService tipoDireccionQueryService
    ) {
        this.tipoDireccionService = tipoDireccionService;
        this.tipoDireccionRepository = tipoDireccionRepository;
        this.tipoDireccionQueryService = tipoDireccionQueryService;
    }

    /**
     * {@code POST  /tipo-direccions} : Create a new tipoDireccion.
     *
     * @param tipoDireccionDTO the tipoDireccionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoDireccionDTO, or with status {@code 400 (Bad Request)} if the tipoDireccion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TipoDireccionDTO> createTipoDireccion(@Valid @RequestBody TipoDireccionDTO tipoDireccionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TipoDireccion : {}", tipoDireccionDTO);
        if (tipoDireccionDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoDireccion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tipoDireccionDTO = tipoDireccionService.save(tipoDireccionDTO);
        return ResponseEntity.created(new URI("/api/tipo-direccions/" + tipoDireccionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tipoDireccionDTO.getId().toString()))
            .body(tipoDireccionDTO);
    }

    /**
     * {@code PUT  /tipo-direccions/:id} : Updates an existing tipoDireccion.
     *
     * @param id the id of the tipoDireccionDTO to save.
     * @param tipoDireccionDTO the tipoDireccionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoDireccionDTO,
     * or with status {@code 400 (Bad Request)} if the tipoDireccionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoDireccionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoDireccionDTO> updateTipoDireccion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TipoDireccionDTO tipoDireccionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TipoDireccion : {}, {}", id, tipoDireccionDTO);
        if (tipoDireccionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoDireccionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoDireccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tipoDireccionDTO = tipoDireccionService.update(tipoDireccionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoDireccionDTO.getId().toString()))
            .body(tipoDireccionDTO);
    }

    /**
     * {@code PATCH  /tipo-direccions/:id} : Partial updates given fields of an existing tipoDireccion, field will ignore if it is null
     *
     * @param id the id of the tipoDireccionDTO to save.
     * @param tipoDireccionDTO the tipoDireccionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoDireccionDTO,
     * or with status {@code 400 (Bad Request)} if the tipoDireccionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoDireccionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoDireccionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoDireccionDTO> partialUpdateTipoDireccion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TipoDireccionDTO tipoDireccionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TipoDireccion partially : {}, {}", id, tipoDireccionDTO);
        if (tipoDireccionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoDireccionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoDireccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoDireccionDTO> result = tipoDireccionService.partialUpdate(tipoDireccionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoDireccionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-direccions} : get all the tipoDireccions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoDireccions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TipoDireccionDTO>> getAllTipoDireccions(TipoDireccionCriteria criteria) {
        LOG.debug("REST request to get TipoDireccions by criteria: {}", criteria);

        List<TipoDireccionDTO> entityList = tipoDireccionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /tipo-direccions/count} : count all the tipoDireccions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTipoDireccions(TipoDireccionCriteria criteria) {
        LOG.debug("REST request to count TipoDireccions by criteria: {}", criteria);
        return ResponseEntity.ok().body(tipoDireccionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tipo-direccions/:id} : get the "id" tipoDireccion.
     *
     * @param id the id of the tipoDireccionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoDireccionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoDireccionDTO> getTipoDireccion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TipoDireccion : {}", id);
        Optional<TipoDireccionDTO> tipoDireccionDTO = tipoDireccionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoDireccionDTO);
    }

    /**
     * {@code DELETE  /tipo-direccions/:id} : delete the "id" tipoDireccion.
     *
     * @param id the id of the tipoDireccionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoDireccion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TipoDireccion : {}", id);
        tipoDireccionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
