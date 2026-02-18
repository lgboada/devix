package com.devix.web.rest;

import com.devix.repository.TipoCatalogoRepository;
import com.devix.service.TipoCatalogoQueryService;
import com.devix.service.TipoCatalogoService;
import com.devix.service.criteria.TipoCatalogoCriteria;
import com.devix.service.dto.TipoCatalogoDTO;
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
 * REST controller for managing {@link com.devix.domain.TipoCatalogo}.
 */
@RestController
@RequestMapping("/api/tipo-catalogos")
public class TipoCatalogoResource {

    private static final Logger LOG = LoggerFactory.getLogger(TipoCatalogoResource.class);

    private static final String ENTITY_NAME = "tipoCatalogo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoCatalogoService tipoCatalogoService;

    private final TipoCatalogoRepository tipoCatalogoRepository;

    private final TipoCatalogoQueryService tipoCatalogoQueryService;

    public TipoCatalogoResource(
        TipoCatalogoService tipoCatalogoService,
        TipoCatalogoRepository tipoCatalogoRepository,
        TipoCatalogoQueryService tipoCatalogoQueryService
    ) {
        this.tipoCatalogoService = tipoCatalogoService;
        this.tipoCatalogoRepository = tipoCatalogoRepository;
        this.tipoCatalogoQueryService = tipoCatalogoQueryService;
    }

    /**
     * {@code POST  /tipo-catalogos} : Create a new tipoCatalogo.
     *
     * @param tipoCatalogoDTO the tipoCatalogoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoCatalogoDTO, or with status {@code 400 (Bad Request)} if the tipoCatalogo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TipoCatalogoDTO> createTipoCatalogo(@Valid @RequestBody TipoCatalogoDTO tipoCatalogoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TipoCatalogo : {}", tipoCatalogoDTO);
        if (tipoCatalogoDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoCatalogo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tipoCatalogoDTO = tipoCatalogoService.save(tipoCatalogoDTO);
        return ResponseEntity.created(new URI("/api/tipo-catalogos/" + tipoCatalogoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tipoCatalogoDTO.getId().toString()))
            .body(tipoCatalogoDTO);
    }

    /**
     * {@code PUT  /tipo-catalogos/:id} : Updates an existing tipoCatalogo.
     *
     * @param id the id of the tipoCatalogoDTO to save.
     * @param tipoCatalogoDTO the tipoCatalogoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoCatalogoDTO,
     * or with status {@code 400 (Bad Request)} if the tipoCatalogoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoCatalogoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoCatalogoDTO> updateTipoCatalogo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TipoCatalogoDTO tipoCatalogoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TipoCatalogo : {}, {}", id, tipoCatalogoDTO);
        if (tipoCatalogoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoCatalogoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoCatalogoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tipoCatalogoDTO = tipoCatalogoService.update(tipoCatalogoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoCatalogoDTO.getId().toString()))
            .body(tipoCatalogoDTO);
    }

    /**
     * {@code PATCH  /tipo-catalogos/:id} : Partial updates given fields of an existing tipoCatalogo, field will ignore if it is null
     *
     * @param id the id of the tipoCatalogoDTO to save.
     * @param tipoCatalogoDTO the tipoCatalogoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoCatalogoDTO,
     * or with status {@code 400 (Bad Request)} if the tipoCatalogoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoCatalogoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoCatalogoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoCatalogoDTO> partialUpdateTipoCatalogo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TipoCatalogoDTO tipoCatalogoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TipoCatalogo partially : {}, {}", id, tipoCatalogoDTO);
        if (tipoCatalogoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoCatalogoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoCatalogoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoCatalogoDTO> result = tipoCatalogoService.partialUpdate(tipoCatalogoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoCatalogoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-catalogos} : get all the tipoCatalogos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoCatalogos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TipoCatalogoDTO>> getAllTipoCatalogos(TipoCatalogoCriteria criteria) {
        LOG.debug("REST request to get TipoCatalogos by criteria: {}", criteria);

        List<TipoCatalogoDTO> entityList = tipoCatalogoQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /tipo-catalogos/count} : count all the tipoCatalogos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTipoCatalogos(TipoCatalogoCriteria criteria) {
        LOG.debug("REST request to count TipoCatalogos by criteria: {}", criteria);
        return ResponseEntity.ok().body(tipoCatalogoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tipo-catalogos/:id} : get the "id" tipoCatalogo.
     *
     * @param id the id of the tipoCatalogoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoCatalogoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoCatalogoDTO> getTipoCatalogo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TipoCatalogo : {}", id);
        Optional<TipoCatalogoDTO> tipoCatalogoDTO = tipoCatalogoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoCatalogoDTO);
    }

    /**
     * {@code DELETE  /tipo-catalogos/:id} : delete the "id" tipoCatalogo.
     *
     * @param id the id of the tipoCatalogoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoCatalogo(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TipoCatalogo : {}", id);
        tipoCatalogoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
