package com.devix.web.rest;

import com.devix.repository.CiudadRepository;
import com.devix.service.CiudadQueryService;
import com.devix.service.CiudadService;
import com.devix.service.criteria.CiudadCriteria;
import com.devix.service.dto.CiudadDTO;
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
 * REST controller for managing {@link com.devix.domain.Ciudad}.
 */
@RestController
@RequestMapping("/api/ciudads")
public class CiudadResource {

    private static final Logger LOG = LoggerFactory.getLogger(CiudadResource.class);

    private static final String ENTITY_NAME = "ciudad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CiudadService ciudadService;

    private final CiudadRepository ciudadRepository;

    private final CiudadQueryService ciudadQueryService;

    public CiudadResource(CiudadService ciudadService, CiudadRepository ciudadRepository, CiudadQueryService ciudadQueryService) {
        this.ciudadService = ciudadService;
        this.ciudadRepository = ciudadRepository;
        this.ciudadQueryService = ciudadQueryService;
    }

    /**
     * {@code POST  /ciudads} : Create a new ciudad.
     *
     * @param ciudadDTO the ciudadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ciudadDTO, or with status {@code 400 (Bad Request)} if the ciudad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CiudadDTO> createCiudad(@Valid @RequestBody CiudadDTO ciudadDTO) throws URISyntaxException {
        LOG.debug("REST request to save Ciudad : {}", ciudadDTO);
        if (ciudadDTO.getId() != null) {
            throw new BadRequestAlertException("A new ciudad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ciudadDTO = ciudadService.save(ciudadDTO);
        return ResponseEntity.created(new URI("/api/ciudads/" + ciudadDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ciudadDTO.getId().toString()))
            .body(ciudadDTO);
    }

    /**
     * {@code PUT  /ciudads/:id} : Updates an existing ciudad.
     *
     * @param id the id of the ciudadDTO to save.
     * @param ciudadDTO the ciudadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ciudadDTO,
     * or with status {@code 400 (Bad Request)} if the ciudadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ciudadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CiudadDTO> updateCiudad(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CiudadDTO ciudadDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Ciudad : {}, {}", id, ciudadDTO);
        if (ciudadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ciudadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ciudadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ciudadDTO = ciudadService.update(ciudadDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ciudadDTO.getId().toString()))
            .body(ciudadDTO);
    }

    /**
     * {@code PATCH  /ciudads/:id} : Partial updates given fields of an existing ciudad, field will ignore if it is null
     *
     * @param id the id of the ciudadDTO to save.
     * @param ciudadDTO the ciudadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ciudadDTO,
     * or with status {@code 400 (Bad Request)} if the ciudadDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ciudadDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ciudadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CiudadDTO> partialUpdateCiudad(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CiudadDTO ciudadDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Ciudad partially : {}, {}", id, ciudadDTO);
        if (ciudadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ciudadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ciudadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CiudadDTO> result = ciudadService.partialUpdate(ciudadDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ciudadDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ciudads} : get all the ciudads.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ciudads in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CiudadDTO>> getAllCiudads(CiudadCriteria criteria) {
        LOG.debug("REST request to get Ciudads by criteria: {}", criteria);

        List<CiudadDTO> entityList = ciudadQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /ciudads/count} : count all the ciudads.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCiudads(CiudadCriteria criteria) {
        LOG.debug("REST request to count Ciudads by criteria: {}", criteria);
        return ResponseEntity.ok().body(ciudadQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ciudads/:id} : get the "id" ciudad.
     *
     * @param id the id of the ciudadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ciudadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CiudadDTO> getCiudad(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Ciudad : {}", id);
        Optional<CiudadDTO> ciudadDTO = ciudadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ciudadDTO);
    }

    /**
     * {@code DELETE  /ciudads/:id} : delete the "id" ciudad.
     *
     * @param id the id of the ciudadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCiudad(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Ciudad : {}", id);
        ciudadService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
