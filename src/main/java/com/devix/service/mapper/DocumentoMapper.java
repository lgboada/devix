package com.devix.service.mapper;

import com.devix.domain.Cliente;
import com.devix.domain.Documento;
import com.devix.domain.Evento;
import com.devix.service.dto.ClienteDTO;
import com.devix.service.dto.DocumentoDTO;
import com.devix.service.dto.EventoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Documento} and its DTO {@link DocumentoDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentoMapper extends EntityMapper<DocumentoDTO, Documento> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteId")
    @Mapping(target = "evento", source = "evento", qualifiedByName = "eventoId")
    DocumentoDTO toDto(Documento s);

    @Named("clienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClienteDTO toDtoClienteId(Cliente cliente);

    @Named("eventoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventoDTO toDtoEventoId(Evento evento);
}
