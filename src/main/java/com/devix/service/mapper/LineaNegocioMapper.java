package com.devix.service.mapper;

import com.devix.domain.LineaNegocio;
import com.devix.service.dto.LineaNegocioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LineaNegocioMapper extends EntityMapper<LineaNegocioDTO, LineaNegocio> {
    @Mapping(target = "noCia", source = "id.noCia")
    @Mapping(target = "lineaNo", source = "id.lineaNo")
    LineaNegocioDTO toDto(LineaNegocio entity);

    @Mapping(target = "id.noCia", source = "noCia")
    @Mapping(target = "id.lineaNo", source = "lineaNo")
    LineaNegocio toEntity(LineaNegocioDTO dto);
}
