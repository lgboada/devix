package com.devix.service.historiaclinica.mapper;

import com.devix.domain.historiaclinica.CategoriaPaciente;
import com.devix.service.historiaclinica.dto.CategoriaPacienteDTO;
import com.devix.service.mapper.EntityMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoriaPacienteMapper extends EntityMapper<CategoriaPacienteDTO, CategoriaPaciente> {}
