package com.devix.service.mapper;

import com.devix.domain.Ciudad;
import com.devix.domain.Cliente;
import com.devix.domain.TipoCliente;
import com.devix.service.dto.CiudadDTO;
import com.devix.service.dto.ClienteDTO;
import com.devix.service.dto.TipoClienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cliente} and its DTO {@link ClienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClienteMapper extends EntityMapper<ClienteDTO, Cliente> {
    @Mapping(target = "tipoCliente", source = "tipoCliente", qualifiedByName = "tipoClienteId")
    @Mapping(target = "ciudad", source = "ciudad", qualifiedByName = "ciudadId")
    ClienteDTO toDto(Cliente s);

    @Named("tipoClienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TipoClienteDTO toDtoTipoClienteId(TipoCliente tipoCliente);

    @Named("ciudadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CiudadDTO toDtoCiudadId(Ciudad ciudad);
}
