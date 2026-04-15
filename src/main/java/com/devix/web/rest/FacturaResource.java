package com.devix.web.rest;

import com.devix.repository.FacturaRepository;
import com.devix.repository.TipoDocumentoRepository;
import com.devix.service.FacturaLogService;
import com.devix.service.FacturaQueryService;
import com.devix.service.FacturaService;
import com.devix.service.criteria.FacturaCriteria;
import com.devix.service.dto.FacturaDTO;
import com.devix.service.dto.FacturaLogDTO;
import com.devix.service.sri.SriEnvioService;
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
 * REST controller for managing {@link com.devix.domain.Factura}.
 */
@RestController
@RequestMapping("/api/facturas")
public class FacturaResource {

    private static final Logger LOG = LoggerFactory.getLogger(FacturaResource.class);

    private static final String ENTITY_NAME = "factura";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacturaService facturaService;
    private final FacturaRepository facturaRepository;
    private final FacturaQueryService facturaQueryService;
    private final SriEnvioService sriEnvioService;
    private final FacturaLogService facturaLogService;
    private final TipoDocumentoRepository tipoDocumentoRepository;

    public FacturaResource(
        FacturaService facturaService,
        FacturaRepository facturaRepository,
        FacturaQueryService facturaQueryService,
        SriEnvioService sriEnvioService,
        FacturaLogService facturaLogService,
        TipoDocumentoRepository tipoDocumentoRepository
    ) {
        this.facturaService = facturaService;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.facturaRepository = facturaRepository;
        this.facturaQueryService = facturaQueryService;
        this.sriEnvioService = sriEnvioService;
        this.facturaLogService = facturaLogService;
    }

    /**
     * {@code POST  /facturas} : Create a new factura.
     *
     * @param facturaDTO the facturaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facturaDTO, or with status {@code 400 (Bad Request)} if the factura has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FacturaDTO> createFactura(@Valid @RequestBody FacturaDTO facturaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Factura : {}", facturaDTO);
        if (facturaDTO.getId() != null) {
            throw new BadRequestAlertException("A new factura cannot already have an ID", ENTITY_NAME, "idexists");
        }
        facturaDTO = facturaService.save(facturaDTO);
        return ResponseEntity.created(new URI("/api/facturas/" + facturaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, facturaDTO.getId().toString()))
            .body(facturaDTO);
    }

    /**
     * {@code PUT  /facturas/:id} : Updates an existing factura.
     *
     * @param id the id of the facturaDTO to save.
     * @param facturaDTO the facturaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facturaDTO,
     * or with status {@code 400 (Bad Request)} if the facturaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facturaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FacturaDTO> updateFactura(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FacturaDTO facturaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Factura : {}, {}", id, facturaDTO);
        if (facturaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facturaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facturaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        facturaDTO = facturaService.update(facturaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facturaDTO.getId().toString()))
            .body(facturaDTO);
    }

    /**
     * {@code PATCH  /facturas/:id} : Partial updates given fields of an existing factura, field will ignore if it is null
     *
     * @param id the id of the facturaDTO to save.
     * @param facturaDTO the facturaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facturaDTO,
     * or with status {@code 400 (Bad Request)} if the facturaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the facturaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the facturaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FacturaDTO> partialUpdateFactura(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FacturaDTO facturaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Factura partially : {}, {}", id, facturaDTO);
        if (facturaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facturaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facturaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacturaDTO> result = facturaService.partialUpdate(facturaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facturaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /facturas} : get all the facturas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facturas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FacturaDTO>> getAllFacturas(
        FacturaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Facturas by criteria: {}", criteria);

        Page<FacturaDTO> page = facturaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /facturas/count} : count all the facturas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFacturas(FacturaCriteria criteria) {
        LOG.debug("REST request to count Facturas by criteria: {}", criteria);
        return ResponseEntity.ok().body(facturaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /facturas/:id} : get the "id" factura.
     *
     * @param id the id of the facturaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facturaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FacturaDTO> getFactura(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Factura : {}", id);
        Optional<FacturaDTO> facturaDTO = facturaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facturaDTO);
    }

    /**
     * {@code POST /facturas/:id/enviar-sri} : Envía la factura al SRI Ecuador.
     * Genera el XML, lo firma con XAdES-BES y lo envía al WebService del SRI.
     * Registra cada intento en factura_log.
     *
     * @param id el id de la factura a enviar
     * @return el log del último intento (recepción o autorización)
     */
    @PostMapping("/{id}/enviar-sri")
    public ResponseEntity<FacturaLogDTO> enviarAlSri(@PathVariable("id") Long id) {
        LOG.debug("REST request to enviar Factura al SRI : {}", id);
        FacturaDTO factura = facturaService
            .findOne(id)
            .orElseThrow(() -> new BadRequestAlertException("Factura no encontrada", ENTITY_NAME, "idnotfound"));
        String tipoGenerador = resolverTipoGenerador(factura.getNoCia(), factura.getTipoDocumento());
        FacturaLogDTO resultado = sriEnvioService.enviar(id, tipoGenerador, factura.getNoCia());
        return ResponseEntity.ok(resultado);
    }

    /**
     * {@code GET /facturas/:id/logs-sri} : Obtiene el historial de intentos de envío al SRI.
     *
     * @param id el id de la factura
     * @return lista de logs ordenados por fecha descendente
     */
    @GetMapping("/{id}/logs-sri")
    public ResponseEntity<List<FacturaLogDTO>> getLogsSri(@PathVariable("id") Long id) {
        LOG.debug("REST request to get logs SRI de Factura : {}", id);
        FacturaDTO factura = facturaService
            .findOne(id)
            .orElseThrow(() -> new BadRequestAlertException("Factura no encontrada", ENTITY_NAME, "idnotfound"));
        String tipoGenerador = resolverTipoGenerador(factura.getNoCia(), factura.getTipoDocumento());
        List<FacturaLogDTO> logs = facturaLogService.findByDocumentoId(id, tipoGenerador);
        return ResponseEntity.ok(logs);
    }

    /**
     * Resuelve el tipo de generador SRI a partir del tipo_documento configurado en la compañía.
     * Consulta el campo codigo_sri del catálogo tipo_documento y lo mapea al identificador del generador.
     */
    private String resolverTipoGenerador(Long noCia, String tipoDocumentoCod) {
        if (tipoDocumentoCod == null) return "FACTURA";
        String codigoSri = tipoDocumentoRepository
            .findByNoCiaAndTipoDocumento(noCia, tipoDocumentoCod)
            .map(td -> td.getCodigoSri())
            .orElse("01");
        return switch (codigoSri) {
            case "04" -> "NOTA_CREDITO";
            case "05" -> "NOTA_DEBITO";
            case "06" -> "GUIA_REMISION";
            case "07" -> "RETENCION";
            case "03" -> "LIQUIDACION_COMPRA";
            default -> "FACTURA";
        };
    }

    /**
     * {@code DELETE  /facturas/:id} : delete the "id" factura.
     *
     * @param id the id of the facturaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactura(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Factura : {}", id);
        facturaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
