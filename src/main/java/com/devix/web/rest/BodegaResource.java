package com.devix.web.rest;

import com.devix.repository.BodegaRepository;
import com.devix.service.BodegaQueryService;
import com.devix.service.BodegaService;
import com.devix.service.criteria.BodegaCriteria;
import com.devix.service.dto.BodegaDTO;
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
 * REST controller for managing {@link com.devix.domain.Bodega}.
 */
@RestController
@RequestMapping("/api/bodegas")
public class BodegaResource {

    private static final Logger LOG = LoggerFactory.getLogger(BodegaResource.class);

    private static final String ENTITY_NAME = "bodega";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BodegaService bodegaService;

    private final BodegaRepository bodegaRepository;

    private final BodegaQueryService bodegaQueryService;

    public BodegaResource(BodegaService bodegaService, BodegaRepository bodegaRepository, BodegaQueryService bodegaQueryService) {
        this.bodegaService = bodegaService;
        this.bodegaRepository = bodegaRepository;
        this.bodegaQueryService = bodegaQueryService;
    }

    @PostMapping("")
    public ResponseEntity<BodegaDTO> createBodega(@Valid @RequestBody BodegaDTO bodegaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Bodega : {}", bodegaDTO);
        if (bodegaDTO.getId() != null) {
            throw new BadRequestAlertException("A new bodega cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bodegaDTO = bodegaService.save(bodegaDTO);
        return ResponseEntity.created(new URI("/api/bodegas/" + bodegaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bodegaDTO.getId().toString()))
            .body(bodegaDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BodegaDTO> updateBodega(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BodegaDTO bodegaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Bodega : {}, {}", id, bodegaDTO);
        if (bodegaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bodegaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bodegaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bodegaDTO = bodegaService.update(bodegaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bodegaDTO.getId().toString()))
            .body(bodegaDTO);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BodegaDTO> partialUpdateBodega(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BodegaDTO bodegaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Bodega partially : {}, {}", id, bodegaDTO);
        if (bodegaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bodegaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bodegaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BodegaDTO> result = bodegaService.partialUpdate(bodegaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bodegaDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<BodegaDTO>> getAllBodegas(BodegaCriteria criteria) {
        LOG.debug("REST request to get Bodegas by criteria: {}", criteria);

        List<BodegaDTO> entityList = bodegaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countBodegas(BodegaCriteria criteria) {
        LOG.debug("REST request to count Bodegas by criteria: {}", criteria);
        return ResponseEntity.ok().body(bodegaQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BodegaDTO> getBodega(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Bodega : {}", id);
        Optional<BodegaDTO> bodegaDTO = bodegaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bodegaDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBodega(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Bodega : {}", id);
        bodegaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
