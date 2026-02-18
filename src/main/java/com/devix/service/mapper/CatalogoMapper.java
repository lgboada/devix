package com.devix.service.mapper;

import com.devix.domain.Catalogo;
import com.devix.domain.TipoCatalogo;
import com.devix.service.dto.CatalogoDTO;
import com.devix.service.dto.TipoCatalogoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Catalogo} and its DTO {@link CatalogoDTO}.
 */
@Mapper(componentModel = "spring")
public interface CatalogoMapper extends EntityMapper<CatalogoDTO, Catalogo> {
    @Mapping(target = "tipoCatalogo", source = "tipoCatalogo", qualifiedByName = "tipoCatalogoId")
    CatalogoDTO toDto(Catalogo s);

    @Named("tipoCatalogoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TipoCatalogoDTO toDtoTipoCatalogoId(TipoCatalogo tipoCatalogo);
}
