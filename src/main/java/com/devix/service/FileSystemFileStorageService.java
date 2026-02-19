package com.devix.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing file storage operations using the file system.
 */
@Service
public class FileSystemFileStorageService implements FileStorageService {

    private static final Logger LOG = LoggerFactory.getLogger(FileSystemFileStorageService.class);

    private final Path rootLocation;

    public FileSystemFileStorageService(@Value("${app.files.upload-dir:uploads}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir);
        try {
            Files.createDirectories(this.rootLocation);
            LOG.info("Directorio de subida de archivos inicializado en: {}", this.rootLocation.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de subida de archivos: " + uploadDir, e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("No se puede almacenar un archivo vacío");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new RuntimeException("El nombre del archivo no puede estar vacío");
        }

        // Generar nombre único para evitar colisiones
        String filename = UUID.randomUUID() + "_" + originalFilename;

        try {
            Path destinationFile = rootLocation.resolve(filename).normalize().toAbsolutePath();

            // Validar que el archivo no salga del directorio raíz (seguridad)
            if (!destinationFile.startsWith(rootLocation.toAbsolutePath())) {
                throw new RuntimeException("No se puede almacenar el archivo fuera del directorio permitido");
            }

            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            LOG.debug("Archivo guardado exitosamente: {} -> {}", originalFilename, destinationFile);
            return filename;
        } catch (IOException e) {
            LOG.error("Error al almacenar el archivo: {}", originalFilename, e);
            throw new RuntimeException("No se pudo almacenar el archivo: " + originalFilename, e);
        }
    }
}
