package com.devix.web.rest;

import com.devix.repository.DocumentoRepository;
import com.devix.service.DocumentoQueryService;
import com.devix.service.DocumentoService;
import com.devix.service.criteria.DocumentoCriteria;
import com.devix.service.dto.DocumentoDTO;
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
 * REST controller for managing {@link com.devix.domain.Documento}.
 */
@RestController
@RequestMapping("/api/documentos")
public class DocumentoResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentoResource.class);

    private static final String ENTITY_NAME = "documento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentoService documentoService;

    private final DocumentoRepository documentoRepository;

    private final DocumentoQueryService documentoQueryService;

    public DocumentoResource(
        DocumentoService documentoService,
        DocumentoRepository documentoRepository,
        DocumentoQueryService documentoQueryService
    ) {
        this.documentoService = documentoService;
        this.documentoRepository = documentoRepository;
        this.documentoQueryService = documentoQueryService;
    }

    /**
     * {@code POST  /documentos} : Create a new documento.
     *
     * @param documentoDTO the documentoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentoDTO, or with status {@code 400 (Bad Request)} if the documento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentoDTO> createDocumento(@Valid @RequestBody DocumentoDTO documentoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Documento : {}", documentoDTO);
        if (documentoDTO.getId() != null) {
            throw new BadRequestAlertException("A new documento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentoDTO = documentoService.save(documentoDTO);
        return ResponseEntity.created(new URI("/api/documentos/" + documentoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentoDTO.getId().toString()))
            .body(documentoDTO);
    }

    /**
     * {@code PUT  /documentos/:id} : Updates an existing documento.
     *
     * @param id the id of the documentoDTO to save.
     * @param documentoDTO the documentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentoDTO,
     * or with status {@code 400 (Bad Request)} if the documentoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentoDTO> updateDocumento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentoDTO documentoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Documento : {}, {}", id, documentoDTO);
        if (documentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentoDTO = documentoService.update(documentoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentoDTO.getId().toString()))
            .body(documentoDTO);
    }

    /**
     * {@code PATCH  /documentos/:id} : Partial updates given fields of an existing documento, field will ignore if it is null
     *
     * @param id the id of the documentoDTO to save.
     * @param documentoDTO the documentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentoDTO,
     * or with status {@code 400 (Bad Request)} if the documentoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentoDTO> partialUpdateDocumento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentoDTO documentoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Documento partially : {}, {}", id, documentoDTO);
        if (documentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentoDTO> result = documentoService.partialUpdate(documentoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /documentos} : get all the documentos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentoDTO>> getAllDocumentos(DocumentoCriteria criteria) {
        LOG.debug("REST request to get Documentos by criteria: {}", criteria);

        List<DocumentoDTO> entityList = documentoQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /documentos/count} : count all the documentos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDocumentos(DocumentoCriteria criteria) {
        LOG.debug("REST request to count Documentos by criteria: {}", criteria);
        return ResponseEntity.ok().body(documentoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /documentos/:id} : get the "id" documento.
     *
     * @param id the id of the documentoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoDTO> getDocumento(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Documento : {}", id);
        Optional<DocumentoDTO> documentoDTO = documentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentoDTO);
    }

    /**
     * {@code DELETE  /documentos/:id} : delete the "id" documento.
     *
     * @param id the id of the documentoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumento(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Documento : {}", id);
        documentoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
