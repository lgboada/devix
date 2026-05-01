package com.devix.web.rest.historiaclinica;

import com.devix.repository.historiaclinica.CategoriaPacienteRepository;
import com.devix.service.historiaclinica.CategoriaPacienteService;
import com.devix.service.historiaclinica.dto.CategoriaPacienteDTO;
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

@RestController
@RequestMapping("/api/categoria-pacientes")
public class CategoriaPacienteResource {

    private static final Logger LOG = LoggerFactory.getLogger(CategoriaPacienteResource.class);

    private static final String ENTITY_NAME = "categoriaPaciente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoriaPacienteService categoriaPacienteService;
    private final CategoriaPacienteRepository categoriaPacienteRepository;

    public CategoriaPacienteResource(
        CategoriaPacienteService categoriaPacienteService,
        CategoriaPacienteRepository categoriaPacienteRepository
    ) {
        this.categoriaPacienteService = categoriaPacienteService;
        this.categoriaPacienteRepository = categoriaPacienteRepository;
    }

    @PostMapping("")
    public ResponseEntity<CategoriaPacienteDTO> createCategoriaPaciente(@Valid @RequestBody CategoriaPacienteDTO categoriaPacienteDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CategoriaPaciente : {}", categoriaPacienteDTO);
        if (categoriaPacienteDTO.getId() != null) {
            throw new BadRequestAlertException("A new categoriaPaciente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        categoriaPacienteDTO = categoriaPacienteService.save(categoriaPacienteDTO);
        return ResponseEntity.created(new URI("/api/categoria-pacientes/" + categoriaPacienteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, categoriaPacienteDTO.getId().toString()))
            .body(categoriaPacienteDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaPacienteDTO> updateCategoriaPaciente(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategoriaPacienteDTO categoriaPacienteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CategoriaPaciente : {}, {}", id, categoriaPacienteDTO);
        if (categoriaPacienteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoriaPacienteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!categoriaPacienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        categoriaPacienteDTO = categoriaPacienteService.update(categoriaPacienteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoriaPacienteDTO.getId().toString()))
            .body(categoriaPacienteDTO);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategoriaPacienteDTO> partialUpdateCategoriaPaciente(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategoriaPacienteDTO categoriaPacienteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CategoriaPaciente : {}, {}", id, categoriaPacienteDTO);
        if (categoriaPacienteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoriaPacienteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!categoriaPacienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<CategoriaPacienteDTO> result = categoriaPacienteService.partialUpdate(categoriaPacienteDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoriaPacienteDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<CategoriaPacienteDTO>> getAllCategoriaPacientes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get all CategoriaPacientes");
        Page<CategoriaPacienteDTO> page = categoriaPacienteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaPacienteDTO> getCategoriaPaciente(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CategoriaPaciente : {}", id);
        Optional<CategoriaPacienteDTO> categoriaPacienteDTO = categoriaPacienteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoriaPacienteDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoriaPaciente(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CategoriaPaciente : {}", id);
        categoriaPacienteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
