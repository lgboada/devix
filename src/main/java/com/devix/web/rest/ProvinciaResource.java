package com.devix.web.rest;

import com.devix.repository.ProvinciaRepository;
import com.devix.service.ProvinciaQueryService;
import com.devix.service.ProvinciaService;
import com.devix.service.criteria.ProvinciaCriteria;
import com.devix.service.dto.ProvinciaDTO;
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
 * REST controller for managing {@link com.devix.domain.Provincia}.
 */
@RestController
@RequestMapping("/api/provincias")
public class ProvinciaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProvinciaResource.class);

    private static final String ENTITY_NAME = "provincia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProvinciaService provinciaService;

    private final ProvinciaRepository provinciaRepository;

    private final ProvinciaQueryService provinciaQueryService;

    public ProvinciaResource(
        ProvinciaService provinciaService,
        ProvinciaRepository provinciaRepository,
        ProvinciaQueryService provinciaQueryService
    ) {
        this.provinciaService = provinciaService;
        this.provinciaRepository = provinciaRepository;
        this.provinciaQueryService = provinciaQueryService;
    }

    /**
     * {@code POST  /provincias} : Create a new provincia.
     *
     * @param provinciaDTO the provinciaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new provinciaDTO, or with status {@code 400 (Bad Request)} if the provincia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProvinciaDTO> createProvincia(@Valid @RequestBody ProvinciaDTO provinciaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Provincia : {}", provinciaDTO);
        if (provinciaDTO.getId() != null) {
            throw new BadRequestAlertException("A new provincia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        provinciaDTO = provinciaService.save(provinciaDTO);
        return ResponseEntity.created(new URI("/api/provincias/" + provinciaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, provinciaDTO.getId().toString()))
            .body(provinciaDTO);
    }

    /**
     * {@code PUT  /provincias/:id} : Updates an existing provincia.
     *
     * @param id the id of the provinciaDTO to save.
     * @param provinciaDTO the provinciaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated provinciaDTO,
     * or with status {@code 400 (Bad Request)} if the provinciaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the provinciaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProvinciaDTO> updateProvincia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProvinciaDTO provinciaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Provincia : {}, {}", id, provinciaDTO);
        if (provinciaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, provinciaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!provinciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        provinciaDTO = provinciaService.update(provinciaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, provinciaDTO.getId().toString()))
            .body(provinciaDTO);
    }

    /**
     * {@code PATCH  /provincias/:id} : Partial updates given fields of an existing provincia, field will ignore if it is null
     *
     * @param id the id of the provinciaDTO to save.
     * @param provinciaDTO the provinciaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated provinciaDTO,
     * or with status {@code 400 (Bad Request)} if the provinciaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the provinciaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the provinciaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProvinciaDTO> partialUpdateProvincia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProvinciaDTO provinciaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Provincia partially : {}, {}", id, provinciaDTO);
        if (provinciaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, provinciaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!provinciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProvinciaDTO> result = provinciaService.partialUpdate(provinciaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, provinciaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /provincias} : get all the provincias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of provincias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProvinciaDTO>> getAllProvincias(ProvinciaCriteria criteria) {
        LOG.debug("REST request to get Provincias by criteria: {}", criteria);

        List<ProvinciaDTO> entityList = provinciaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /provincias/count} : count all the provincias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProvincias(ProvinciaCriteria criteria) {
        LOG.debug("REST request to count Provincias by criteria: {}", criteria);
        return ResponseEntity.ok().body(provinciaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /provincias/:id} : get the "id" provincia.
     *
     * @param id the id of the provinciaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the provinciaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProvinciaDTO> getProvincia(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Provincia : {}", id);
        Optional<ProvinciaDTO> provinciaDTO = provinciaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(provinciaDTO);
    }

    /**
     * {@code DELETE  /provincias/:id} : delete the "id" provincia.
     *
     * @param id the id of the provinciaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvincia(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Provincia : {}", id);
        provinciaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
