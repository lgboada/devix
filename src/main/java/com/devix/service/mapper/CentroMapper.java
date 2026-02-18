package com.devix.service.mapper;

import com.devix.domain.Centro;
import com.devix.domain.Compania;
import com.devix.service.dto.CentroDTO;
import com.devix.service.dto.CompaniaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Centro} and its DTO {@link CentroDTO}.
 */
@Mapper(componentModel = "spring")
public interface CentroMapper extends EntityMapper<CentroDTO, Centro> {
    @Mapping(target = "compania", source = "compania", qualifiedByName = "companiaId")
    CentroDTO toDto(Centro s);

    @Named("companiaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompaniaDTO toDtoCompaniaId(Compania compania);
}
