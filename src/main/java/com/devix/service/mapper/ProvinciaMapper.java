package com.devix.service.mapper;

import com.devix.domain.Pais;
import com.devix.domain.Provincia;
import com.devix.service.dto.PaisDTO;
import com.devix.service.dto.ProvinciaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Provincia} and its DTO {@link ProvinciaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProvinciaMapper extends EntityMapper<ProvinciaDTO, Provincia> {
    @Mapping(target = "pais", source = "pais", qualifiedByName = "paisId")
    ProvinciaDTO toDto(Provincia s);

    @Named("paisId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaisDTO toDtoPaisId(Pais pais);
}
