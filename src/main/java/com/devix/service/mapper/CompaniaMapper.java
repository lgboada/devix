package com.devix.service.mapper;

import com.devix.domain.Compania;
import com.devix.service.dto.CompaniaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Compania} and its DTO {@link CompaniaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompaniaMapper extends EntityMapper<CompaniaDTO, Compania> {}
