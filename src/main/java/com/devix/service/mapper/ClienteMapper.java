package com.devix.service.mapper;

import com.devix.domain.Ciudad;
import com.devix.domain.Cliente;
import com.devix.service.dto.CiudadDTO;
import com.devix.service.dto.ClienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cliente} and its DTO {@link ClienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClienteMapper extends EntityMapper<ClienteDTO, Cliente> {
    @Mapping(target = "ciudad", source = "ciudad", qualifiedByName = "ciudadId")
    ClienteDTO toDto(Cliente s);

    @Named("ciudadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CiudadDTO toDtoCiudadId(Ciudad ciudad);
}
