package com.devix.service;

import java.nio.file.Path;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing file storage operations.
 */
public interface FileStorageService {
    /**
     * Store a file and return the stored filename.
     *
     * @param file the file to store
     * @return the stored filename
     */
    String store(MultipartFile file);

    /**
     * Store a file in a custom root location and return the stored filename.
     *
     * @param file the file to store
     * @param rootLocation the target root directory
     * @return the stored filename
     */
    String store(MultipartFile file, Path rootLocation);
}
