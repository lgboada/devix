package com.devix.web.rest;

import com.devix.service.LineaNegocioService;
import com.devix.service.dto.LineaNegocioDTO;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/linea-negocios")
public class LineaNegocioResource {

    private static final Logger LOG = LoggerFactory.getLogger(LineaNegocioResource.class);

    private final LineaNegocioService service;

    public LineaNegocioResource(LineaNegocioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LineaNegocioDTO> create(@RequestBody LineaNegocioDTO dto) {
        LOG.debug("REST request to save LineaNegocio : {}", dto);
        LineaNegocioDTO result = service.save(dto);
        return ResponseEntity.created(URI.create("/api/linea-negocios/" + result.getNoCia() + "/" + result.getLineaNo())).body(result);
    }

    @PutMapping("/{noCia}/{lineaNo}")
    public ResponseEntity<LineaNegocioDTO> update(
        @PathVariable Long noCia,
        @PathVariable String lineaNo,
        @RequestBody LineaNegocioDTO dto
    ) {
        LOG.debug("REST request to update LineaNegocio : {}/{}", noCia, lineaNo);
        dto.setNoCia(noCia);
        dto.setLineaNo(lineaNo);
        return ResponseEntity.ok(service.save(dto));
    }

    @GetMapping
    public ResponseEntity<List<LineaNegocioDTO>> getAll(@RequestParam(required = false) Long noCia) {
        LOG.debug("REST request to get LineaNegocios, noCia={}", noCia);
        if (noCia != null) {
            return ResponseEntity.ok(service.findByNoCia(noCia));
        }
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/{noCia}/{lineaNo}")
    public ResponseEntity<LineaNegocioDTO> getOne(@PathVariable Long noCia, @PathVariable String lineaNo) {
        LOG.debug("REST request to get LineaNegocio : {}/{}", noCia, lineaNo);
        return service.findOne(noCia, lineaNo).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{noCia}/{lineaNo}")
    public ResponseEntity<Void> delete(@PathVariable Long noCia, @PathVariable String lineaNo) {
        LOG.debug("REST request to delete LineaNegocio : {}/{}", noCia, lineaNo);
        service.delete(noCia, lineaNo);
        return ResponseEntity.noContent().build();
    }
}
