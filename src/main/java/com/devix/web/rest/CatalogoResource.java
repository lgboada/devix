package com.devix.web.rest;

import com.devix.repository.CatalogoRepository;
import com.devix.service.CatalogoQueryService;
import com.devix.service.CatalogoService;
import com.devix.service.criteria.CatalogoCriteria;
import com.devix.service.dto.CatalogoDTO;
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
 * REST controller for managing {@link com.devix.domain.Catalogo}.
 */
@RestController
@RequestMapping("/api/catalogos")
public class CatalogoResource {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogoResource.class);

    private static final String ENTITY_NAME = "catalogo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalogoService catalogoService;

    private final CatalogoRepository catalogoRepository;

    private final CatalogoQueryService catalogoQueryService;

    public CatalogoResource(
        CatalogoService catalogoService,
        CatalogoRepository catalogoRepository,
        CatalogoQueryService catalogoQueryService
    ) {
        this.catalogoService = catalogoService;
        this.catalogoRepository = catalogoRepository;
        this.catalogoQueryService = catalogoQueryService;
    }

    /**
     * {@code POST  /catalogos} : Create a new catalogo.
     *
     * @param catalogoDTO the catalogoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogoDTO, or with status {@code 400 (Bad Request)} if the catalogo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CatalogoDTO> createCatalogo(@Valid @RequestBody CatalogoDTO catalogoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Catalogo : {}", catalogoDTO);
        if (catalogoDTO.getId() != null) {
            throw new BadRequestAlertException("A new catalogo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        catalogoDTO = catalogoService.save(catalogoDTO);
        return ResponseEntity.created(new URI("/api/catalogos/" + catalogoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, catalogoDTO.getId().toString()))
            .body(catalogoDTO);
    }

    /**
     * {@code PUT  /catalogos/:id} : Updates an existing catalogo.
     *
     * @param id the id of the catalogoDTO to save.
     * @param catalogoDTO the catalogoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogoDTO,
     * or with status {@code 400 (Bad Request)} if the catalogoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CatalogoDTO> updateCatalogo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CatalogoDTO catalogoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Catalogo : {}, {}", id, catalogoDTO);
        if (catalogoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        catalogoDTO = catalogoService.update(catalogoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogoDTO.getId().toString()))
            .body(catalogoDTO);
    }

    /**
     * {@code PATCH  /catalogos/:id} : Partial updates given fields of an existing catalogo, field will ignore if it is null
     *
     * @param id the id of the catalogoDTO to save.
     * @param catalogoDTO the catalogoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogoDTO,
     * or with status {@code 400 (Bad Request)} if the catalogoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the catalogoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalogoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CatalogoDTO> partialUpdateCatalogo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CatalogoDTO catalogoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Catalogo partially : {}, {}", id, catalogoDTO);
        if (catalogoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CatalogoDTO> result = catalogoService.partialUpdate(catalogoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /catalogos} : get all the catalogos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CatalogoDTO>> getAllCatalogos(CatalogoCriteria criteria) {
        LOG.debug("REST request to get Catalogos by criteria: {}", criteria);

        List<CatalogoDTO> entityList = catalogoQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /catalogos/count} : count all the catalogos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCatalogos(CatalogoCriteria criteria) {
        LOG.debug("REST request to count Catalogos by criteria: {}", criteria);
        return ResponseEntity.ok().body(catalogoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /catalogos/:id} : get the "id" catalogo.
     *
     * @param id the id of the catalogoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CatalogoDTO> getCatalogo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Catalogo : {}", id);
        Optional<CatalogoDTO> catalogoDTO = catalogoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogoDTO);
    }

    /**
     * {@code DELETE  /catalogos/:id} : delete the "id" catalogo.
     *
     * @param id the id of the catalogoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCatalogo(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Catalogo : {}", id);
        catalogoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
