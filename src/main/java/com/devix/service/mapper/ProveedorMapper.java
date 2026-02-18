package com.devix.service.mapper;

import com.devix.domain.Proveedor;
import com.devix.service.dto.ProveedorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Proveedor} and its DTO {@link ProveedorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProveedorMapper extends EntityMapper<ProveedorDTO, Proveedor> {}
