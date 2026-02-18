package com.devix.web.rest;

import com.devix.repository.TipoEventoRepository;
import com.devix.service.TipoEventoQueryService;
import com.devix.service.TipoEventoService;
import com.devix.service.criteria.TipoEventoCriteria;
import com.devix.service.dto.TipoEventoDTO;
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
 * REST controller for managing {@link com.devix.domain.TipoEvento}.
 */
@RestController
@RequestMapping("/api/tipo-eventos")
public class TipoEventoResource {

    private static final Logger LOG = LoggerFactory.getLogger(TipoEventoResource.class);

    private static final String ENTITY_NAME = "tipoEvento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoEventoService tipoEventoService;

    private final TipoEventoRepository tipoEventoRepository;

    private final TipoEventoQueryService tipoEventoQueryService;

    public TipoEventoResource(
        TipoEventoService tipoEventoService,
        TipoEventoRepository tipoEventoRepository,
        TipoEventoQueryService tipoEventoQueryService
    ) {
        this.tipoEventoService = tipoEventoService;
        this.tipoEventoRepository = tipoEventoRepository;
        this.tipoEventoQueryService = tipoEventoQueryService;
    }

    /**
     * {@code POST  /tipo-eventos} : Create a new tipoEvento.
     *
     * @param tipoEventoDTO the tipoEventoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoEventoDTO, or with status {@code 400 (Bad Request)} if the tipoEvento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TipoEventoDTO> createTipoEvento(@Valid @RequestBody TipoEventoDTO tipoEventoDTO) throws URISyntaxException {
        LOG.debug("REST request to save TipoEvento : {}", tipoEventoDTO);
        if (tipoEventoDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoEvento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tipoEventoDTO = tipoEventoService.save(tipoEventoDTO);
        return ResponseEntity.created(new URI("/api/tipo-eventos/" + tipoEventoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tipoEventoDTO.getId().toString()))
            .body(tipoEventoDTO);
    }

    /**
     * {@code PUT  /tipo-eventos/:id} : Updates an existing tipoEvento.
     *
     * @param id the id of the tipoEventoDTO to save.
     * @param tipoEventoDTO the tipoEventoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoEventoDTO,
     * or with status {@code 400 (Bad Request)} if the tipoEventoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoEventoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoEventoDTO> updateTipoEvento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TipoEventoDTO tipoEventoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TipoEvento : {}, {}", id, tipoEventoDTO);
        if (tipoEventoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoEventoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoEventoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tipoEventoDTO = tipoEventoService.update(tipoEventoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoEventoDTO.getId().toString()))
            .body(tipoEventoDTO);
    }

    /**
     * {@code PATCH  /tipo-eventos/:id} : Partial updates given fields of an existing tipoEvento, field will ignore if it is null
     *
     * @param id the id of the tipoEventoDTO to save.
     * @param tipoEventoDTO the tipoEventoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoEventoDTO,
     * or with status {@code 400 (Bad Request)} if the tipoEventoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoEventoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoEventoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoEventoDTO> partialUpdateTipoEvento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TipoEventoDTO tipoEventoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TipoEvento partially : {}, {}", id, tipoEventoDTO);
        if (tipoEventoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoEventoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoEventoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoEventoDTO> result = tipoEventoService.partialUpdate(tipoEventoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoEventoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-eventos} : get all the tipoEventos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoEventos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TipoEventoDTO>> getAllTipoEventos(TipoEventoCriteria criteria) {
        LOG.debug("REST request to get TipoEventos by criteria: {}", criteria);

        List<TipoEventoDTO> entityList = tipoEventoQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /tipo-eventos/count} : count all the tipoEventos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTipoEventos(TipoEventoCriteria criteria) {
        LOG.debug("REST request to count TipoEventos by criteria: {}", criteria);
        return ResponseEntity.ok().body(tipoEventoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tipo-eventos/:id} : get the "id" tipoEvento.
     *
     * @param id the id of the tipoEventoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoEventoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoEventoDTO> getTipoEvento(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TipoEvento : {}", id);
        Optional<TipoEventoDTO> tipoEventoDTO = tipoEventoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoEventoDTO);
    }

    /**
     * {@code DELETE  /tipo-eventos/:id} : delete the "id" tipoEvento.
     *
     * @param id the id of the tipoEventoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoEvento(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TipoEvento : {}", id);
        tipoEventoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
