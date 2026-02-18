package com.devix.service.mapper;

import com.devix.domain.Ciudad;
import com.devix.domain.Provincia;
import com.devix.service.dto.CiudadDTO;
import com.devix.service.dto.ProvinciaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ciudad} and its DTO {@link CiudadDTO}.
 */
@Mapper(componentModel = "spring")
public interface CiudadMapper extends EntityMapper<CiudadDTO, Ciudad> {
    @Mapping(target = "provincia", source = "provincia", qualifiedByName = "provinciaId")
    CiudadDTO toDto(Ciudad s);

    @Named("provinciaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProvinciaDTO toDtoProvinciaId(Provincia provincia);
}
