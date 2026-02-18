package com.devix.service.mapper;

import com.devix.domain.TipoDireccion;
import com.devix.service.dto.TipoDireccionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoDireccion} and its DTO {@link TipoDireccionDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoDireccionMapper extends EntityMapper<TipoDireccionDTO, TipoDireccion> {}
