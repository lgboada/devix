package com.devix.service.mapper;

import com.devix.domain.Centro;
import com.devix.service.dto.CentroDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Centro} and its DTO {@link CentroDTO}.
 */
@Mapper(componentModel = "spring")
public interface CentroMapper extends EntityMapper<CentroDTO, Centro> {}
