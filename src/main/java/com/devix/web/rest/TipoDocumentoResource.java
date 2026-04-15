package com.devix.web.rest;

import com.devix.service.TipoDocumentoService;
import com.devix.service.dto.TipoDocumentoDTO;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tipo-documentos")
public class TipoDocumentoResource {

    private static final Logger LOG = LoggerFactory.getLogger(TipoDocumentoResource.class);

    private final TipoDocumentoService service;

    public TipoDocumentoResource(TipoDocumentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TipoDocumentoDTO> create(@RequestBody TipoDocumentoDTO dto) {
        LOG.debug("REST request to save TipoDocumento : {}", dto);
        TipoDocumentoDTO result = service.save(dto);
        return ResponseEntity.created(URI.create("/api/tipo-documentos/" + result.getNoCia() + "/" + result.getTipoDocumento())).body(
            result
        );
    }

    @PutMapping("/{noCia}/{codigo}")
    public ResponseEntity<TipoDocumentoDTO> update(
        @PathVariable Long noCia,
        @PathVariable String codigo,
        @RequestBody TipoDocumentoDTO dto
    ) {
        LOG.debug("REST request to update TipoDocumento : {}/{}", noCia, codigo);
        dto.setNoCia(noCia);
        dto.setTipoDocumento(codigo);
        return ResponseEntity.ok(service.save(dto));
    }

    @GetMapping
    public ResponseEntity<List<TipoDocumentoDTO>> getAll(@RequestParam(required = false) Long noCia) {
        LOG.debug("REST request to get TipoDocumentos, noCia={}", noCia);
        if (noCia != null) {
            return ResponseEntity.ok(service.findByNoCia(noCia));
        }
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/{noCia}/{codigo}")
    public ResponseEntity<TipoDocumentoDTO> getOne(@PathVariable Long noCia, @PathVariable String codigo) {
        LOG.debug("REST request to get TipoDocumento : {}/{}", noCia, codigo);
        return service.findOne(noCia, codigo).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{noCia}/{codigo}")
    public ResponseEntity<Void> delete(@PathVariable Long noCia, @PathVariable String codigo) {
        LOG.debug("REST request to delete TipoDocumento : {}/{}", noCia, codigo);
        service.delete(noCia, codigo);
        return ResponseEntity.noContent().build();
    }
}
