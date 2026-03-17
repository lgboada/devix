package com.devix.web.rest;

import com.devix.security.company.CompanyContextService;
import com.devix.service.ProductoImagenService;
import com.devix.service.dto.ProductoImagenDTO;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api/productos/{productoId}/imagenes")
public class ProductoImagenResource {

    private static final String ENTITY_NAME = "productoImagen";

    private final ProductoImagenService productoImagenService;
    private final CompanyContextService companyContextService;

    @org.springframework.beans.factory.annotation.Value("${jhipster.clientApp.name}")
    private String applicationName;

    public ProductoImagenResource(ProductoImagenService productoImagenService, CompanyContextService companyContextService) {
        this.productoImagenService = productoImagenService;
        this.companyContextService = companyContextService;
    }

    @GetMapping("")
    public ResponseEntity<List<ProductoImagenDTO>> getByProducto(@PathVariable("productoId") Long productoId) {
        Long noCia = companyContextService.getCurrentCompanyIdOrThrow();
        return ResponseEntity.ok(productoImagenService.findByProducto(productoId, noCia));
    }

    @PostMapping("")
    public ResponseEntity<ProductoImagenDTO> create(
        @PathVariable("productoId") Long productoId,
        @Valid @RequestBody ProductoImagenDTO productoImagenDTO
    ) throws URISyntaxException {
        Long noCia = companyContextService.getCurrentCompanyIdOrThrow();
        ProductoImagenDTO result = productoImagenService.create(productoId, noCia, productoImagenDTO);
        return ResponseEntity.created(new URI("/api/productos/" + productoId + "/imagenes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoImagenDTO> update(
        @PathVariable("productoId") Long productoId,
        @PathVariable("id") Long id,
        @Valid @RequestBody ProductoImagenDTO productoImagenDTO
    ) {
        Long noCia = companyContextService.getCurrentCompanyIdOrThrow();
        ProductoImagenDTO result = productoImagenService.update(productoId, id, noCia, productoImagenDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("productoId") Long productoId, @PathVariable("id") Long id) {
        Long noCia = companyContextService.getCurrentCompanyIdOrThrow();
        productoImagenService.delete(productoId, id, noCia);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
