package com.devix.web.rest;

import com.devix.repository.CompaniaRepository;
import com.devix.service.CompaniaQueryService;
import com.devix.service.CompaniaService;
import com.devix.service.criteria.CompaniaCriteria;
import com.devix.service.dto.CompaniaDTO;
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
 * REST controller for managing {@link com.devix.domain.Compania}.
 */
@RestController
@RequestMapping("/api/companias")
public class CompaniaResource {

    private static final Logger LOG = LoggerFactory.getLogger(CompaniaResource.class);

    private static final String ENTITY_NAME = "compania";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompaniaService companiaService;

    private final CompaniaRepository companiaRepository;

    private final CompaniaQueryService companiaQueryService;

    public CompaniaResource(
        CompaniaService companiaService,
        CompaniaRepository companiaRepository,
        CompaniaQueryService companiaQueryService
    ) {
        this.companiaService = companiaService;
        this.companiaRepository = companiaRepository;
        this.companiaQueryService = companiaQueryService;
    }

    /**
     * {@code POST  /companias} : Create a new compania.
     *
     * @param companiaDTO the companiaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companiaDTO, or with status {@code 400 (Bad Request)} if the compania has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CompaniaDTO> createCompania(@Valid @RequestBody CompaniaDTO companiaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Compania : {}", companiaDTO);
        if (companiaDTO.getId() != null) {
            throw new BadRequestAlertException("A new compania cannot already have an ID", ENTITY_NAME, "idexists");
        }
        companiaDTO = companiaService.save(companiaDTO);
        return ResponseEntity.created(new URI("/api/companias/" + companiaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, companiaDTO.getId().toString()))
            .body(companiaDTO);
    }

    /**
     * {@code PUT  /companias/:id} : Updates an existing compania.
     *
     * @param id the id of the companiaDTO to save.
     * @param companiaDTO the companiaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companiaDTO,
     * or with status {@code 400 (Bad Request)} if the companiaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companiaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompaniaDTO> updateCompania(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CompaniaDTO companiaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Compania : {}, {}", id, companiaDTO);
        if (companiaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companiaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companiaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        companiaDTO = companiaService.update(companiaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companiaDTO.getId().toString()))
            .body(companiaDTO);
    }

    /**
     * {@code PATCH  /companias/:id} : Partial updates given fields of an existing compania, field will ignore if it is null
     *
     * @param id the id of the companiaDTO to save.
     * @param companiaDTO the companiaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companiaDTO,
     * or with status {@code 400 (Bad Request)} if the companiaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the companiaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the companiaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompaniaDTO> partialUpdateCompania(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CompaniaDTO companiaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Compania partially : {}, {}", id, companiaDTO);
        if (companiaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companiaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companiaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompaniaDTO> result = companiaService.partialUpdate(companiaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companiaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /companias} : get all the companias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CompaniaDTO>> getAllCompanias(CompaniaCriteria criteria) {
        LOG.debug("REST request to get Companias by criteria: {}", criteria);

        List<CompaniaDTO> entityList = companiaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /companias/count} : count all the companias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCompanias(CompaniaCriteria criteria) {
        LOG.debug("REST request to count Companias by criteria: {}", criteria);
        return ResponseEntity.ok().body(companiaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /companias/:id} : get the "id" compania.
     *
     * @param id the id of the companiaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companiaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompaniaDTO> getCompania(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Compania : {}", id);
        Optional<CompaniaDTO> companiaDTO = companiaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companiaDTO);
    }

    /**
     * {@code DELETE  /companias/:id} : delete the "id" compania.
     *
     * @param id the id of the companiaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompania(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Compania : {}", id);
        companiaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
