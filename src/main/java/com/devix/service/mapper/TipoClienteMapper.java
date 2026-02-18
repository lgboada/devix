package com.devix.service.mapper;

import com.devix.domain.TipoCliente;
import com.devix.service.dto.TipoClienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoCliente} and its DTO {@link TipoClienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoClienteMapper extends EntityMapper<TipoClienteDTO, TipoCliente> {}
