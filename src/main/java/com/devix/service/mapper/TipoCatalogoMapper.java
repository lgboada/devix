package com.devix.service.mapper;

import com.devix.domain.TipoCatalogo;
import com.devix.service.dto.TipoCatalogoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoCatalogo} and its DTO {@link TipoCatalogoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoCatalogoMapper extends EntityMapper<TipoCatalogoDTO, TipoCatalogo> {}
