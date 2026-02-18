package com.devix.service.mapper;

import com.devix.domain.Modelo;
import com.devix.domain.Producto;
import com.devix.domain.Proveedor;
import com.devix.domain.TipoProducto;
import com.devix.service.dto.ModeloDTO;
import com.devix.service.dto.ProductoDTO;
import com.devix.service.dto.ProveedorDTO;
import com.devix.service.dto.TipoProductoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Producto} and its DTO {@link ProductoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductoMapper extends EntityMapper<ProductoDTO, Producto> {
    @Mapping(target = "modelo", source = "modelo", qualifiedByName = "modeloId")
    @Mapping(target = "tipoProducto", source = "tipoProducto", qualifiedByName = "tipoProductoId")
    @Mapping(target = "proveedor", source = "proveedor", qualifiedByName = "proveedorId")
    ProductoDTO toDto(Producto s);

    @Named("modeloId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ModeloDTO toDtoModeloId(Modelo modelo);

    @Named("tipoProductoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TipoProductoDTO toDtoTipoProductoId(TipoProducto tipoProducto);

    @Named("proveedorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProveedorDTO toDtoProveedorId(Proveedor proveedor);
}
