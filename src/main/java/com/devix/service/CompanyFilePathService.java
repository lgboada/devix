package com.devix.service;

import com.devix.domain.Compania;
import com.devix.repository.CompaniaRepository;
import com.devix.security.company.CompanyContextService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class CompanyFilePathService {

    private final CompanyContextService companyContextService;
    private final CompaniaRepository companiaRepository;
    private final String fallbackUploadDir;

    public CompanyFilePathService(
        CompanyContextService companyContextService,
        CompaniaRepository companiaRepository,
        @Value("${app.files.upload-dir:uploads}") String fallbackUploadDir
    ) {
        this.companyContextService = companyContextService;
        this.companiaRepository = companiaRepository;
        this.fallbackUploadDir = fallbackUploadDir;
    }

    /**
     * Resuelve el directorio raíz de archivos de la compañía activa.
     *
     * Reglas:
     * - Si {@code compania.pathFileServer} está configurado, se usa ese path (debe ser absoluto).
     * - Caso contrario, usa el fallback {@code app.files.upload-dir} + subcarpeta por compañía.
     */
    public Path resolveCurrentCompanyRootLocationOrThrow() {
        Long noCia = companyContextService.getCurrentCompanyIdOrThrow();
        Compania compania = companiaRepository
            .findByNoCia(noCia)
            .orElseThrow(() -> new AccessDeniedException("No existe la compania activa"));
        return resolveCompanyRootLocationOrThrow(compania);
    }

    /**
     * Directorio raíz de archivos de una compañía (misma regla que {@link #resolveCurrentCompanyRootLocationOrThrow()}).
     */
    public Path resolveCompanyRootLocationOrThrow(Compania compania) {
        String configured = compania.getPathFileServer();
        Path rootLocation;
        if (configured != null && !configured.isBlank()) {
            rootLocation = Paths.get(configured.trim());
            if (!rootLocation.isAbsolute()) {
                throw new IllegalArgumentException("El pathFileServer debe ser una ruta absoluta");
            }
        } else {
            rootLocation = Paths.get(fallbackUploadDir).resolve("cia_" + compania.getNoCia());
        }

        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de archivos de la compania: " + rootLocation, e);
        }

        return rootLocation;
    }
}
