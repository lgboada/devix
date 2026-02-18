package com.devix.service.mapper;

import com.devix.domain.Centro;
import com.devix.domain.Cliente;
import com.devix.domain.Evento;
import com.devix.domain.TipoEvento;
import com.devix.service.dto.CentroDTO;
import com.devix.service.dto.ClienteDTO;
import com.devix.service.dto.EventoDTO;
import com.devix.service.dto.TipoEventoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Evento} and its DTO {@link EventoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventoMapper extends EntityMapper<EventoDTO, Evento> {
    @Mapping(target = "tipoEvento", source = "tipoEvento", qualifiedByName = "tipoEventoId")
    @Mapping(target = "centro", source = "centro", qualifiedByName = "centroId")
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteId")
    EventoDTO toDto(Evento s);

    @Named("tipoEventoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TipoEventoDTO toDtoTipoEventoId(TipoEvento tipoEvento);

    @Named("centroId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CentroDTO toDtoCentroId(Centro centro);

    @Named("clienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClienteDTO toDtoClienteId(Cliente cliente);
}
