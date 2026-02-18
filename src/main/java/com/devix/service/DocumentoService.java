package com.devix.service;

import com.devix.domain.Documento;
import com.devix.repository.DocumentoRepository;
import com.devix.service.dto.DocumentoDTO;
import com.devix.service.mapper.DocumentoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devix.domain.Documento}.
 */
@Service
@Transactional
public class DocumentoService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentoService.class);

    private final DocumentoRepository documentoRepository;

    private final DocumentoMapper documentoMapper;

    public DocumentoService(DocumentoRepository documentoRepository, DocumentoMapper documentoMapper) {
        this.documentoRepository = documentoRepository;
        this.documentoMapper = documentoMapper;
    }

    /**
     * Save a documento.
     *
     * @param documentoDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentoDTO save(DocumentoDTO documentoDTO) {
        LOG.debug("Request to save Documento : {}", documentoDTO);
        Documento documento = documentoMapper.toEntity(documentoDTO);
        documento = documentoRepository.save(documento);
        return documentoMapper.toDto(documento);
    }

    /**
     * Update a documento.
     *
     * @param documentoDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentoDTO update(DocumentoDTO documentoDTO) {
        LOG.debug("Request to update Documento : {}", documentoDTO);
        Documento documento = documentoMapper.toEntity(documentoDTO);
        documento = documentoRepository.save(documento);
        return documentoMapper.toDto(documento);
    }

    /**
     * Partially update a documento.
     *
     * @param documentoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentoDTO> partialUpdate(DocumentoDTO documentoDTO) {
        LOG.debug("Request to partially update Documento : {}", documentoDTO);

        return documentoRepository
            .findById(documentoDTO.getId())
            .map(existingDocumento -> {
                documentoMapper.partialUpdate(existingDocumento, documentoDTO);

                return existingDocumento;
            })
            .map(documentoRepository::save)
            .map(documentoMapper::toDto);
    }

    /**
     * Get one documento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentoDTO> findOne(Long id) {
        LOG.debug("Request to get Documento : {}", id);
        return documentoRepository.findById(id).map(documentoMapper::toDto);
    }

    /**
     * Delete the documento by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Documento : {}", id);
        documentoRepository.deleteById(id);
    }
}
