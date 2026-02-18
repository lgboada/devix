package com.devix.web.rest;

import com.devix.repository.ModeloRepository;
import com.devix.service.ModeloQueryService;
import com.devix.service.ModeloService;
import com.devix.service.criteria.ModeloCriteria;
import com.devix.service.dto.ModeloDTO;
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
 * REST controller for managing {@link com.devix.domain.Modelo}.
 */
@RestController
@RequestMapping("/api/modelos")
public class ModeloResource {

    private static final Logger LOG = LoggerFactory.getLogger(ModeloResource.class);

    private static final String ENTITY_NAME = "modelo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModeloService modeloService;

    private final ModeloRepository modeloRepository;

    private final ModeloQueryService modeloQueryService;

    public ModeloResource(ModeloService modeloService, ModeloRepository modeloRepository, ModeloQueryService modeloQueryService) {
        this.modeloService = modeloService;
        this.modeloRepository = modeloRepository;
        this.modeloQueryService = modeloQueryService;
    }

    /**
     * {@code POST  /modelos} : Create a new modelo.
     *
     * @param modeloDTO the modeloDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new modeloDTO, or with status {@code 400 (Bad Request)} if the modelo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ModeloDTO> createModelo(@Valid @RequestBody ModeloDTO modeloDTO) throws URISyntaxException {
        LOG.debug("REST request to save Modelo : {}", modeloDTO);
        if (modeloDTO.getId() != null) {
            throw new BadRequestAlertException("A new modelo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        modeloDTO = modeloService.save(modeloDTO);
        return ResponseEntity.created(new URI("/api/modelos/" + modeloDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, modeloDTO.getId().toString()))
            .body(modeloDTO);
    }

    /**
     * {@code PUT  /modelos/:id} : Updates an existing modelo.
     *
     * @param id the id of the modeloDTO to save.
     * @param modeloDTO the modeloDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modeloDTO,
     * or with status {@code 400 (Bad Request)} if the modeloDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the modeloDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ModeloDTO> updateModelo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ModeloDTO modeloDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Modelo : {}, {}", id, modeloDTO);
        if (modeloDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modeloDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!modeloRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        modeloDTO = modeloService.update(modeloDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, modeloDTO.getId().toString()))
            .body(modeloDTO);
    }

    /**
     * {@code PATCH  /modelos/:id} : Partial updates given fields of an existing modelo, field will ignore if it is null
     *
     * @param id the id of the modeloDTO to save.
     * @param modeloDTO the modeloDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modeloDTO,
     * or with status {@code 400 (Bad Request)} if the modeloDTO is not valid,
     * or with status {@code 404 (Not Found)} if the modeloDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the modeloDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ModeloDTO> partialUpdateModelo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ModeloDTO modeloDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Modelo partially : {}, {}", id, modeloDTO);
        if (modeloDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modeloDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!modeloRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ModeloDTO> result = modeloService.partialUpdate(modeloDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, modeloDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /modelos} : get all the modelos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of modelos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ModeloDTO>> getAllModelos(ModeloCriteria criteria) {
        LOG.debug("REST request to get Modelos by criteria: {}", criteria);

        List<ModeloDTO> entityList = modeloQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /modelos/count} : count all the modelos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countModelos(ModeloCriteria criteria) {
        LOG.debug("REST request to count Modelos by criteria: {}", criteria);
        return ResponseEntity.ok().body(modeloQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /modelos/:id} : get the "id" modelo.
     *
     * @param id the id of the modeloDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the modeloDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ModeloDTO> getModelo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Modelo : {}", id);
        Optional<ModeloDTO> modeloDTO = modeloService.findOne(id);
        return ResponseUtil.wrapOrNotFound(modeloDTO);
    }

    /**
     * {@code DELETE  /modelos/:id} : delete the "id" modelo.
     *
     * @param id the id of the modeloDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModelo(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Modelo : {}", id);
        modeloService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
