package com.devix.service.mapper;

import com.devix.domain.Ciudad;
import com.devix.domain.Cliente;
import com.devix.domain.Direccion;
import com.devix.domain.TipoDireccion;
import com.devix.service.dto.CiudadDTO;
import com.devix.service.dto.ClienteDTO;
import com.devix.service.dto.DireccionDTO;
import com.devix.service.dto.TipoDireccionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Direccion} and its DTO {@link DireccionDTO}.
 */
@Mapper(componentModel = "spring")
public interface DireccionMapper extends EntityMapper<DireccionDTO, Direccion> {
    @Mapping(target = "tipoDireccion", source = "tipoDireccion", qualifiedByName = "tipoDireccionFromDtoId")
    @Mapping(target = "ciudad", source = "ciudad", qualifiedByName = "ciudadFromDtoId")
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteFromDtoId")
    Direccion toEntity(DireccionDTO s);

    @Mapping(target = "tipoDireccion", source = "tipoDireccion", qualifiedByName = "tipoDireccionId")
    @Mapping(target = "ciudad", source = "ciudad", qualifiedByName = "ciudadId")
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteId")
    DireccionDTO toDto(Direccion s);

    @Named("tipoDireccionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TipoDireccionDTO toDtoTipoDireccionId(TipoDireccion tipoDireccion);

    @Named("clienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClienteDTO toDtoClienteId(Cliente cliente);

    @Named("tipoDireccionFromDtoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TipoDireccion toEntityTipoDireccionId(TipoDireccionDTO tipoDireccionDTO);

    @Named("clienteFromDtoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Cliente toEntityClienteId(ClienteDTO clienteDTO);

    @Named("ciudadFromDtoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Ciudad toEntityCiudadId(CiudadDTO ciudadDTO);

    @Named("ciudadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "descripcion", source = "descripcion")
    CiudadDTO toDtoCiudadId(Ciudad ciudad);
}
