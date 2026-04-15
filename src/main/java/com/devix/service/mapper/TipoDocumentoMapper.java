package com.devix.service.mapper;

import com.devix.domain.TipoDocumento;
import com.devix.service.dto.TipoDocumentoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TipoDocumentoMapper extends EntityMapper<TipoDocumentoDTO, TipoDocumento> {
    @Mapping(target = "noCia", source = "id.noCia")
    @Mapping(target = "tipoDocumento", source = "id.tipoDocumento")
    TipoDocumentoDTO toDto(TipoDocumento entity);

    @Mapping(target = "id.noCia", source = "noCia")
    @Mapping(target = "id.tipoDocumento", source = "tipoDocumento")
    TipoDocumento toEntity(TipoDocumentoDTO dto);
}
