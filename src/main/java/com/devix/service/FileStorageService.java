package com.devix.service;

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
}
