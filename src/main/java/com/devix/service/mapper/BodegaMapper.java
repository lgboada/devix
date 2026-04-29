package com.devix.service.mapper;

import com.devix.domain.Bodega;
import com.devix.domain.Centro;
import com.devix.service.dto.BodegaDTO;
import com.devix.service.dto.CentroDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Bodega} and its DTO {@link BodegaDTO}.
 */
@Mapper(componentModel = "spring")
public interface BodegaMapper extends EntityMapper<BodegaDTO, Bodega> {
    @Override
    @Mapping(target = "centro", source = "centro", qualifiedByName = "centroFromId")
    Bodega toEntity(BodegaDTO dto);

    @Override
    @Mapping(target = "centro", source = "centro", qualifiedByName = "centroId")
    BodegaDTO toDto(Bodega entity);

    @Named("centroId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CentroDTO toDtoCentroId(Centro centro);

    @Named("centroFromId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Centro centroFromCentroDTO(CentroDTO centroDto);
}
