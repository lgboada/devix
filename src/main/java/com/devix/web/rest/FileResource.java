package com.devix.web.rest;

import com.devix.service.FileStorageService;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing file uploads.
 */
@RestController
@RequestMapping("/api/files")
public class FileResource {

    private static final Logger LOG = LoggerFactory.getLogger(FileResource.class);

    private static final String ENTITY_NAME = "file";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Value("${app.files.upload-dir:uploads}")
    private String uploadDir;

    private final FileStorageService fileStorageService;

    public FileResource(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    /**
     * {@code GET  /files/:filename} : Get a file by filename.
     *
     * @param filename the filename of the file to retrieve
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the file in body,
     * or with status {@code 404 (Not Found)} if the file doesn't exist.
     */
    @GetMapping("/{filename:.+}")
    public ResponseEntity<org.springframework.core.io.Resource> getFile(@PathVariable String filename) {
        LOG.debug("REST request to get file : {}", filename);
        try {
            java.nio.file.Path rootLocation = java.nio.file.Paths.get(uploadDir);
            java.nio.file.Path filePath = rootLocation.resolve(filename).normalize();

            // Validar que el archivo no salga del directorio raíz (seguridad)
            if (!filePath.startsWith(rootLocation.toAbsolutePath())) {
                LOG.warn("Intento de acceso a archivo fuera del directorio permitido: {}", filename);
                return ResponseEntity.notFound().build();
            }

            org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
                if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
                    contentType = "image/jpeg";
                } else if (filename.toLowerCase().endsWith(".png")) {
                    contentType = "image/png";
                } else if (filename.toLowerCase().endsWith(".gif")) {
                    contentType = "image/gif";
                } else if (filename.toLowerCase().endsWith(".webp")) {
                    contentType = "image/webp";
                }

                return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (java.net.MalformedURLException e) {
            LOG.error("Error al obtener archivo: {}", filename, e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * {@code POST  /files} : Upload a new file.
     *
     * @param file the file to upload
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body containing the stored filename,
     * or with status {@code 400 (Bad Request)} if the file is empty or invalid.
     */
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        LOG.debug("REST request to upload file : {}", file.getOriginalFilename());

        Map<String, String> response = new HashMap<>();

        if (file.isEmpty()) {
            response.put("error", "El archivo está vacío");
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createAlert(applicationName, "El archivo está vacío", ENTITY_NAME))
                .body(response);
        }

        try {
            String storedFilename = fileStorageService.store(file);
            response.put("filename", storedFilename);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert(applicationName, "Archivo subido exitosamente", ENTITY_NAME))
                .body(response);
        } catch (RuntimeException e) {
            LOG.error("Error al subir archivo: {}", file.getOriginalFilename(), e);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().headers(HeaderUtil.createAlert(applicationName, e.getMessage(), ENTITY_NAME)).body(response);
        }
    }
}
