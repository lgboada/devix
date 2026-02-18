package com.devix.web.rest;

import com.devix.repository.CentroRepository;
import com.devix.service.CentroQueryService;
import com.devix.service.CentroService;
import com.devix.service.criteria.CentroCriteria;
import com.devix.service.dto.CentroDTO;
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
 * REST controller for managing {@link com.devix.domain.Centro}.
 */
@RestController
@RequestMapping("/api/centros")
public class CentroResource {

    private static final Logger LOG = LoggerFactory.getLogger(CentroResource.class);

    private static final String ENTITY_NAME = "centro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CentroService centroService;

    private final CentroRepository centroRepository;

    private final CentroQueryService centroQueryService;

    public CentroResource(CentroService centroService, CentroRepository centroRepository, CentroQueryService centroQueryService) {
        this.centroService = centroService;
        this.centroRepository = centroRepository;
        this.centroQueryService = centroQueryService;
    }

    /**
     * {@code POST  /centros} : Create a new centro.
     *
     * @param centroDTO the centroDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new centroDTO, or with status {@code 400 (Bad Request)} if the centro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CentroDTO> createCentro(@Valid @RequestBody CentroDTO centroDTO) throws URISyntaxException {
        LOG.debug("REST request to save Centro : {}", centroDTO);
        if (centroDTO.getId() != null) {
            throw new BadRequestAlertException("A new centro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        centroDTO = centroService.save(centroDTO);
        return ResponseEntity.created(new URI("/api/centros/" + centroDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, centroDTO.getId().toString()))
            .body(centroDTO);
    }

    /**
     * {@code PUT  /centros/:id} : Updates an existing centro.
     *
     * @param id the id of the centroDTO to save.
     * @param centroDTO the centroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated centroDTO,
     * or with status {@code 400 (Bad Request)} if the centroDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the centroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CentroDTO> updateCentro(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CentroDTO centroDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Centro : {}, {}", id, centroDTO);
        if (centroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, centroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!centroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        centroDTO = centroService.update(centroDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, centroDTO.getId().toString()))
            .body(centroDTO);
    }

    /**
     * {@code PATCH  /centros/:id} : Partial updates given fields of an existing centro, field will ignore if it is null
     *
     * @param id the id of the centroDTO to save.
     * @param centroDTO the centroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated centroDTO,
     * or with status {@code 400 (Bad Request)} if the centroDTO is not valid,
     * or with status {@code 404 (Not Found)} if the centroDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the centroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CentroDTO> partialUpdateCentro(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CentroDTO centroDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Centro partially : {}, {}", id, centroDTO);
        if (centroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, centroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!centroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CentroDTO> result = centroService.partialUpdate(centroDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, centroDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /centros} : get all the centros.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of centros in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CentroDTO>> getAllCentros(CentroCriteria criteria) {
        LOG.debug("REST request to get Centros by criteria: {}", criteria);

        List<CentroDTO> entityList = centroQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /centros/count} : count all the centros.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCentros(CentroCriteria criteria) {
        LOG.debug("REST request to count Centros by criteria: {}", criteria);
        return ResponseEntity.ok().body(centroQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /centros/:id} : get the "id" centro.
     *
     * @param id the id of the centroDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the centroDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CentroDTO> getCentro(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Centro : {}", id);
        Optional<CentroDTO> centroDTO = centroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(centroDTO);
    }

    /**
     * {@code DELETE  /centros/:id} : delete the "id" centro.
     *
     * @param id the id of the centroDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCentro(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Centro : {}", id);
        centroService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
