package com.devix.service.mapper;

import com.devix.domain.Centro;
import com.devix.domain.Cliente;
import com.devix.domain.Factura;
import com.devix.service.dto.CentroDTO;
import com.devix.service.dto.ClienteDTO;
import com.devix.service.dto.FacturaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Factura} and its DTO {@link FacturaDTO}.
 */
@Mapper(componentModel = "spring")
public interface FacturaMapper extends EntityMapper<FacturaDTO, Factura> {
    @Mapping(target = "centro", source = "centro", qualifiedByName = "centroId")
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteId")
    FacturaDTO toDto(Factura s);

    @Named("centroId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CentroDTO toDtoCentroId(Centro centro);

    @Named("clienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClienteDTO toDtoClienteId(Cliente cliente);
}
