package com.devix.service;

import com.devix.domain.Producto;
import com.devix.domain.ProductoImagen;
import com.devix.repository.ProductoImagenRepository;
import com.devix.repository.ProductoRepository;
import com.devix.service.dto.ProductoImagenDTO;
import com.devix.service.mapper.ProductoImagenMapper;
import com.devix.web.rest.errors.BadRequestAlertException;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductoImagenService {

    private final ProductoImagenRepository productoImagenRepository;
    private final ProductoRepository productoRepository;
    private final ProductoImagenMapper productoImagenMapper;

    public ProductoImagenService(
        ProductoImagenRepository productoImagenRepository,
        ProductoRepository productoRepository,
        ProductoImagenMapper productoImagenMapper
    ) {
        this.productoImagenRepository = productoImagenRepository;
        this.productoRepository = productoRepository;
        this.productoImagenMapper = productoImagenMapper;
    }

    @Transactional(readOnly = true)
    public List<ProductoImagenDTO> findByProducto(Long productoId, Long noCia) {
        validateProductoOwnership(productoId, noCia);
        return productoImagenMapper.toDto(productoImagenRepository.findByProductoIdOrderByOrdenAscIdAsc(productoId));
    }

    public ProductoImagenDTO create(Long productoId, Long noCia, ProductoImagenDTO dto) {
        if (dto.getId() != null) {
            throw new BadRequestAlertException("Una imagen nueva no puede tener ID", "productoImagen", "idexists");
        }
        Producto producto = validateProductoOwnership(productoId, noCia);
        ProductoImagen entity = productoImagenMapper.toEntity(dto);
        entity.setId(null);
        entity.setProducto(producto);
        entity.setNoCia(noCia);
        applyDefaults(entity);
        clearPrincipalIfNeeded(productoId, entity.getPrincipal(), null);
        return productoImagenMapper.toDto(productoImagenRepository.save(entity));
    }

    public ProductoImagenDTO update(Long productoId, Long id, Long noCia, ProductoImagenDTO dto) {
        if (dto.getId() == null || !Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("ID invalido para actualizar imagen", "productoImagen", "idinvalid");
        }
        validateProductoOwnership(productoId, noCia);
        ProductoImagen existing = productoImagenRepository
            .findByIdAndProductoId(id, productoId)
            .orElseThrow(() -> new BadRequestAlertException("Imagen no encontrada", "productoImagen", "idnotfound"));

        existing.setPathImagen(dto.getPathImagen());
        existing.setOrden(dto.getOrden());
        existing.setPrincipal(dto.getPrincipal());
        existing.setVisible(dto.getVisible());
        existing.setNoCia(noCia);
        applyDefaults(existing);
        clearPrincipalIfNeeded(productoId, existing.getPrincipal(), existing.getId());
        return productoImagenMapper.toDto(productoImagenRepository.save(existing));
    }

    public void delete(Long productoId, Long id, Long noCia) {
        validateProductoOwnership(productoId, noCia);
        productoImagenRepository.deleteByIdAndProductoId(id, productoId);
    }

    private void clearPrincipalIfNeeded(Long productoId, Boolean principal, Long currentId) {
        if (!Boolean.TRUE.equals(principal)) {
            return;
        }
        if (currentId == null) {
            productoImagenRepository.clearPrincipalForProducto(productoId);
            return;
        }
        productoImagenRepository.clearPrincipalForProductoExcept(productoId, currentId);
    }

    private void applyDefaults(ProductoImagen entity) {
        if (entity.getOrden() == null) {
            entity.setOrden(0);
        }
        if (entity.getPrincipal() == null) {
            entity.setPrincipal(false);
        }
        if (entity.getVisible() == null) {
            entity.setVisible(true);
        }
    }

    private Producto validateProductoOwnership(Long productoId, Long noCia) {
        Producto producto = productoRepository
            .findById(productoId)
            .orElseThrow(() -> new BadRequestAlertException("Producto no encontrado", "productoImagen", "productnotfound"));
        if (!Objects.equals(producto.getNoCia(), noCia)) {
            throw new BadRequestAlertException("Producto no pertenece a la compania activa", "productoImagen", "companymismatch");
        }
        return producto;
    }
}
