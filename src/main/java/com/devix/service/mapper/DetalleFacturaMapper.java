package com.devix.service.mapper;

import com.devix.domain.DetalleFactura;
import com.devix.domain.Factura;
import com.devix.domain.Producto;
import com.devix.service.dto.DetalleFacturaDTO;
import com.devix.service.dto.FacturaDTO;
import com.devix.service.dto.ProductoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DetalleFactura} and its DTO {@link DetalleFacturaDTO}.
 */
@Mapper(componentModel = "spring")
public interface DetalleFacturaMapper extends EntityMapper<DetalleFacturaDTO, DetalleFactura> {
    @Mapping(target = "factura", source = "factura", qualifiedByName = "facturaId")
    @Mapping(target = "producto", source = "producto", qualifiedByName = "productoId")
    DetalleFacturaDTO toDto(DetalleFactura s);

    @Named("facturaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FacturaDTO toDtoFacturaId(Factura factura);

    @Named("productoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductoDTO toDtoProductoId(Producto producto);
}
