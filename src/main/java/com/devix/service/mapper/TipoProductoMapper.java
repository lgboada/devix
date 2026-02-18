package com.devix.service.mapper;

import com.devix.domain.TipoProducto;
import com.devix.service.dto.TipoProductoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoProducto} and its DTO {@link TipoProductoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoProductoMapper extends EntityMapper<TipoProductoDTO, TipoProducto> {}
