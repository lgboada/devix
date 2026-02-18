package com.devix.web.rest;

import com.devix.repository.TipoClienteRepository;
import com.devix.service.TipoClienteQueryService;
import com.devix.service.TipoClienteService;
import com.devix.service.criteria.TipoClienteCriteria;
import com.devix.service.dto.TipoClienteDTO;
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
 * REST controller for managing {@link com.devix.domain.TipoCliente}.
 */
@RestController
@RequestMapping("/api/tipo-clientes")
public class TipoClienteResource {

    private static final Logger LOG = LoggerFactory.getLogger(TipoClienteResource.class);

    private static final String ENTITY_NAME = "tipoCliente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoClienteService tipoClienteService;

    private final TipoClienteRepository tipoClienteRepository;

    private final TipoClienteQueryService tipoClienteQueryService;

    public TipoClienteResource(
        TipoClienteService tipoClienteService,
        TipoClienteRepository tipoClienteRepository,
        TipoClienteQueryService tipoClienteQueryService
    ) {
        this.tipoClienteService = tipoClienteService;
        this.tipoClienteRepository = tipoClienteRepository;
        this.tipoClienteQueryService = tipoClienteQueryService;
    }

    /**
     * {@code POST  /tipo-clientes} : Create a new tipoCliente.
     *
     * @param tipoClienteDTO the tipoClienteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoClienteDTO, or with status {@code 400 (Bad Request)} if the tipoCliente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TipoClienteDTO> createTipoCliente(@Valid @RequestBody TipoClienteDTO tipoClienteDTO) throws URISyntaxException {
        LOG.debug("REST request to save TipoCliente : {}", tipoClienteDTO);
        if (tipoClienteDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoCliente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tipoClienteDTO = tipoClienteService.save(tipoClienteDTO);
        return ResponseEntity.created(new URI("/api/tipo-clientes/" + tipoClienteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tipoClienteDTO.getId().toString()))
            .body(tipoClienteDTO);
    }

    /**
     * {@code PUT  /tipo-clientes/:id} : Updates an existing tipoCliente.
     *
     * @param id the id of the tipoClienteDTO to save.
     * @param tipoClienteDTO the tipoClienteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoClienteDTO,
     * or with status {@code 400 (Bad Request)} if the tipoClienteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoClienteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoClienteDTO> updateTipoCliente(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TipoClienteDTO tipoClienteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TipoCliente : {}, {}", id, tipoClienteDTO);
        if (tipoClienteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoClienteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoClienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tipoClienteDTO = tipoClienteService.update(tipoClienteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoClienteDTO.getId().toString()))
            .body(tipoClienteDTO);
    }

    /**
     * {@code PATCH  /tipo-clientes/:id} : Partial updates given fields of an existing tipoCliente, field will ignore if it is null
     *
     * @param id the id of the tipoClienteDTO to save.
     * @param tipoClienteDTO the tipoClienteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoClienteDTO,
     * or with status {@code 400 (Bad Request)} if the tipoClienteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoClienteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoClienteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoClienteDTO> partialUpdateTipoCliente(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TipoClienteDTO tipoClienteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TipoCliente partially : {}, {}", id, tipoClienteDTO);
        if (tipoClienteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoClienteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoClienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoClienteDTO> result = tipoClienteService.partialUpdate(tipoClienteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoClienteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-clientes} : get all the tipoClientes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoClientes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TipoClienteDTO>> getAllTipoClientes(TipoClienteCriteria criteria) {
        LOG.debug("REST request to get TipoClientes by criteria: {}", criteria);

        List<TipoClienteDTO> entityList = tipoClienteQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /tipo-clientes/count} : count all the tipoClientes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTipoClientes(TipoClienteCriteria criteria) {
        LOG.debug("REST request to count TipoClientes by criteria: {}", criteria);
        return ResponseEntity.ok().body(tipoClienteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tipo-clientes/:id} : get the "id" tipoCliente.
     *
     * @param id the id of the tipoClienteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoClienteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoClienteDTO> getTipoCliente(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TipoCliente : {}", id);
        Optional<TipoClienteDTO> tipoClienteDTO = tipoClienteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoClienteDTO);
    }

    /**
     * {@code DELETE  /tipo-clientes/:id} : delete the "id" tipoCliente.
     *
     * @param id the id of the tipoClienteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoCliente(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TipoCliente : {}", id);
        tipoClienteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
