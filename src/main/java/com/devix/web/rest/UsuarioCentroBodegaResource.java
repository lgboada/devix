package com.devix.web.rest;

import com.devix.repository.UsuarioCentroBodegaRepository;
import com.devix.service.UsuarioCentroBodegaQueryService;
import com.devix.service.UsuarioCentroBodegaService;
import com.devix.service.criteria.UsuarioCentroBodegaCriteria;
import com.devix.service.dto.UsuarioCentroBodegaDTO;
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
 * REST controller for managing {@link com.devix.domain.UsuarioCentroBodega}.
 */
@RestController
@RequestMapping("/api/usuario-centro-bodegas")
public class UsuarioCentroBodegaResource {

    private static final Logger LOG = LoggerFactory.getLogger(UsuarioCentroBodegaResource.class);

    private static final String ENTITY_NAME = "usuarioCentroBodega";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UsuarioCentroBodegaService service;
    private final UsuarioCentroBodegaRepository repository;
    private final UsuarioCentroBodegaQueryService queryService;

    public UsuarioCentroBodegaResource(
        UsuarioCentroBodegaService service,
        UsuarioCentroBodegaRepository repository,
        UsuarioCentroBodegaQueryService queryService
    ) {
        this.service = service;
        this.repository = repository;
        this.queryService = queryService;
    }

    @PostMapping("")
    public ResponseEntity<UsuarioCentroBodegaDTO> createUsuarioCentroBodega(@Valid @RequestBody UsuarioCentroBodegaDTO dto)
        throws URISyntaxException {
        LOG.debug("REST request to save UsuarioCentroBodega : {}", dto);
        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new usuarioCentroBodega cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dto = service.save(dto);
        return ResponseEntity.created(new URI("/api/usuario-centro-bodegas/" + dto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, dto.getId().toString()))
            .body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioCentroBodegaDTO> updateUsuarioCentroBodega(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UsuarioCentroBodegaDTO dto
    ) throws URISyntaxException {
        LOG.debug("REST request to update UsuarioCentroBodega : {}, {}", id, dto);
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        dto = service.update(dto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString()))
            .body(dto);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UsuarioCentroBodegaDTO> partialUpdateUsuarioCentroBodega(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UsuarioCentroBodegaDTO dto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UsuarioCentroBodega : {}, {}", id, dto);
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<UsuarioCentroBodegaDTO> result = service.partialUpdate(dto);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<UsuarioCentroBodegaDTO>> getAllUsuarioCentroBodegas(UsuarioCentroBodegaCriteria criteria) {
        LOG.debug("REST request to get UsuarioCentroBodegas by criteria: {}", criteria);
        List<UsuarioCentroBodegaDTO> list = queryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countUsuarioCentroBodegas(UsuarioCentroBodegaCriteria criteria) {
        LOG.debug("REST request to count UsuarioCentroBodegas by criteria: {}", criteria);
        return ResponseEntity.ok().body(queryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioCentroBodegaDTO> getUsuarioCentroBodega(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UsuarioCentroBodega : {}", id);
        Optional<UsuarioCentroBodegaDTO> dto = service.findOne(id);
        return ResponseUtil.wrapOrNotFound(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuarioCentroBodega(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UsuarioCentroBodega : {}", id);
        service.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
