package com.devix.service.mapper;

import com.devix.domain.FacturaLog;
import com.devix.service.dto.FacturaLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FacturaLog} and its DTO {@link FacturaLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface FacturaLogMapper extends EntityMapper<FacturaLogDTO, FacturaLog> {}
