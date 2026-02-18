package com.devix.service.mapper;

import com.devix.domain.TipoEvento;
import com.devix.service.dto.TipoEventoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoEvento} and its DTO {@link TipoEventoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoEventoMapper extends EntityMapper<TipoEventoDTO, TipoEvento> {}
