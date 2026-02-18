package com.devix.service;

import com.devix.domain.Catalogo;
import com.devix.repository.CatalogoRepository;
import com.devix.service.dto.CatalogoDTO;
import com.devix.service.mapper.CatalogoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devix.domain.Catalogo}.
 */
@Service
@Transactional
public class CatalogoService {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogoService.class);

    private final CatalogoRepository catalogoRepository;

    private final CatalogoMapper catalogoMapper;

    public CatalogoService(CatalogoRepository catalogoRepository, CatalogoMapper catalogoMapper) {
        this.catalogoRepository = catalogoRepository;
        this.catalogoMapper = catalogoMapper;
    }

    /**
     * Save a catalogo.
     *
     * @param catalogoDTO the entity to save.
     * @return the persisted entity.
     */
    public CatalogoDTO save(CatalogoDTO catalogoDTO) {
        LOG.debug("Request to save Catalogo : {}", catalogoDTO);
        Catalogo catalogo = catalogoMapper.toEntity(catalogoDTO);
        catalogo = catalogoRepository.save(catalogo);
        return catalogoMapper.toDto(catalogo);
    }

    /**
     * Update a catalogo.
     *
     * @param catalogoDTO the entity to save.
     * @return the persisted entity.
     */
    public CatalogoDTO update(CatalogoDTO catalogoDTO) {
        LOG.debug("Request to update Catalogo : {}", catalogoDTO);
        Catalogo catalogo = catalogoMapper.toEntity(catalogoDTO);
        catalogo = catalogoRepository.save(catalogo);
        return catalogoMapper.toDto(catalogo);
    }

    /**
     * Partially update a catalogo.
     *
     * @param catalogoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CatalogoDTO> partialUpdate(CatalogoDTO catalogoDTO) {
        LOG.debug("Request to partially update Catalogo : {}", catalogoDTO);

        return catalogoRepository
            .findById(catalogoDTO.getId())
            .map(existingCatalogo -> {
                catalogoMapper.partialUpdate(existingCatalogo, catalogoDTO);

                return existingCatalogo;
            })
            .map(catalogoRepository::save)
            .map(catalogoMapper::toDto);
    }

    /**
     * Get one catalogo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CatalogoDTO> findOne(Long id) {
        LOG.debug("Request to get Catalogo : {}", id);
        return catalogoRepository.findById(id).map(catalogoMapper::toDto);
    }

    /**
     * Delete the catalogo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Catalogo : {}", id);
        catalogoRepository.deleteById(id);
    }
}
