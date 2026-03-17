package com.devix.service.mapper;

import com.devix.domain.Producto;
import com.devix.domain.ProductoImagen;
import com.devix.service.dto.ProductoImagenDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductoImagenMapper extends EntityMapper<ProductoImagenDTO, ProductoImagen> {
    @Mapping(target = "productoId", source = "producto.id")
    ProductoImagenDTO toDto(ProductoImagen entity);

    @Mapping(target = "producto", source = "productoId", qualifiedByName = "productoFromId")
    ProductoImagen toEntity(ProductoImagenDTO dto);

    @Named("productoFromId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Producto toProductoFromId(Long id);
}
