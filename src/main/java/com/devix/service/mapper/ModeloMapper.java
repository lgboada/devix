package com.devix.service.mapper;

import com.devix.domain.Marca;
import com.devix.domain.Modelo;
import com.devix.service.dto.MarcaDTO;
import com.devix.service.dto.ModeloDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Modelo} and its DTO {@link ModeloDTO}.
 */
@Mapper(componentModel = "spring")
public interface ModeloMapper extends EntityMapper<ModeloDTO, Modelo> {
    @Mapping(target = "marca", source = "marca", qualifiedByName = "marcaResumen")
    ModeloDTO toDto(Modelo s);

    @Named("marcaResumen")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    MarcaDTO toDtoMarcaResumen(Marca marca);
}
