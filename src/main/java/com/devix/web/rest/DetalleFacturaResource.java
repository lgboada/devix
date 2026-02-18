package com.devix.web.rest;

import com.devix.repository.DetalleFacturaRepository;
import com.devix.service.DetalleFacturaQueryService;
import com.devix.service.DetalleFacturaService;
import com.devix.service.criteria.DetalleFacturaCriteria;
import com.devix.service.dto.DetalleFacturaDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.devix.domain.DetalleFactura}.
 */
@RestController
@RequestMapping("/api/detalle-facturas")
public class DetalleFacturaResource {

    private static final Logger LOG = LoggerFactory.getLogger(DetalleFacturaResource.class);

    private static final String ENTITY_NAME = "detalleFactura";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetalleFacturaService detalleFacturaService;

    private final DetalleFacturaRepository detalleFacturaRepository;

    private final DetalleFacturaQueryService detalleFacturaQueryService;

    public DetalleFacturaResource(
        DetalleFacturaService detalleFacturaService,
        DetalleFacturaRepository detalleFacturaRepository,
        DetalleFacturaQueryService detalleFacturaQueryService
    ) {
        this.detalleFacturaService = detalleFacturaService;
        this.detalleFacturaRepository = detalleFacturaRepository;
        this.detalleFacturaQueryService = detalleFacturaQueryService;
    }

    /**
     * {@code POST  /detalle-facturas} : Create a new detalleFactura.
     *
     * @param detalleFacturaDTO the detalleFacturaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detalleFacturaDTO, or with status {@code 400 (Bad Request)} if the detalleFactura has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DetalleFacturaDTO> createDetalleFactura(@Valid @RequestBody DetalleFacturaDTO detalleFacturaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DetalleFactura : {}", detalleFacturaDTO);
        if (detalleFacturaDTO.getId() != null) {
            throw new BadRequestAlertException("A new detalleFactura cannot already have an ID", ENTITY_NAME, "idexists");
        }
        detalleFacturaDTO = detalleFacturaService.save(detalleFacturaDTO);
        return ResponseEntity.created(new URI("/api/detalle-facturas/" + detalleFacturaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, detalleFacturaDTO.getId().toString()))
            .body(detalleFacturaDTO);
    }

    /**
     * {@code PUT  /detalle-facturas/:id} : Updates an existing detalleFactura.
     *
     * @param id the id of the detalleFacturaDTO to save.
     * @param detalleFacturaDTO the detalleFacturaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detalleFacturaDTO,
     * or with status {@code 400 (Bad Request)} if the detalleFacturaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detalleFacturaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DetalleFacturaDTO> updateDetalleFactura(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DetalleFacturaDTO detalleFacturaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DetalleFactura : {}, {}", id, detalleFacturaDTO);
        if (detalleFacturaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detalleFacturaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detalleFacturaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        detalleFacturaDTO = detalleFacturaService.update(detalleFacturaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detalleFacturaDTO.getId().toString()))
            .body(detalleFacturaDTO);
    }

    /**
     * {@code PATCH  /detalle-facturas/:id} : Partial updates given fields of an existing detalleFactura, field will ignore if it is null
     *
     * @param id the id of the detalleFacturaDTO to save.
     * @param detalleFacturaDTO the detalleFacturaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detalleFacturaDTO,
     * or with status {@code 400 (Bad Request)} if the detalleFacturaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the detalleFacturaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the detalleFacturaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DetalleFacturaDTO> partialUpdateDetalleFactura(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DetalleFacturaDTO detalleFacturaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DetalleFactura partially : {}, {}", id, detalleFacturaDTO);
        if (detalleFacturaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detalleFacturaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detalleFacturaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DetalleFacturaDTO> result = detalleFacturaService.partialUpdate(detalleFacturaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detalleFacturaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /detalle-facturas} : get all the detalleFacturas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detalleFacturas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DetalleFacturaDTO>> getAllDetalleFacturas(
        DetalleFacturaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DetalleFacturas by criteria: {}", criteria);

        Page<DetalleFacturaDTO> page = detalleFacturaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /detalle-facturas/count} : count all the detalleFacturas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDetalleFacturas(DetalleFacturaCriteria criteria) {
        LOG.debug("REST request to count DetalleFacturas by criteria: {}", criteria);
        return ResponseEntity.ok().body(detalleFacturaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /detalle-facturas/:id} : get the "id" detalleFactura.
     *
     * @param id the id of the detalleFacturaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detalleFacturaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DetalleFacturaDTO> getDetalleFactura(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DetalleFactura : {}", id);
        Optional<DetalleFacturaDTO> detalleFacturaDTO = detalleFacturaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(detalleFacturaDTO);
    }

    /**
     * {@code DELETE  /detalle-facturas/:id} : delete the "id" detalleFactura.
     *
     * @param id the id of the detalleFacturaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetalleFactura(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DetalleFactura : {}", id);
        detalleFacturaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
