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
    @Mapping(target = "tipoProducto", source = "tipoProducto", qualifiedByName = "tipoProductoResumen")
    @Mapping(target = "proveedor", source = "proveedor", qualifiedByName = "proveedorResumen")
    ProductoDTO toDto(Producto s);

    @Named("modeloId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ModeloDTO toDtoModeloId(Modelo modelo);

    @Named("tipoProductoResumen")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    TipoProductoDTO toDtoTipoProductoResumen(TipoProducto tipoProducto);

    @Named("proveedorResumen")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "dni", source = "dni")
    ProveedorDTO toDtoProveedorResumen(Proveedor proveedor);
}
