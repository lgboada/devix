package com.devix.service.historiaclinica.mapper;

import com.devix.domain.Ciudad;
import com.devix.domain.historiaclinica.CategoriaPaciente;
import com.devix.domain.historiaclinica.Paciente;
import com.devix.service.historiaclinica.dto.CategoriaPacienteDTO;
import com.devix.service.historiaclinica.dto.CiudadRefDTO;
import com.devix.service.historiaclinica.dto.PacienteDTO;
import com.devix.service.mapper.EntityMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PacienteMapper extends EntityMapper<PacienteDTO, Paciente> {
    @Mapping(target = "ciudad", source = "ciudad", qualifiedByName = "ciudadId")
    @Mapping(target = "categoriaPaciente", source = "categoriaPaciente", qualifiedByName = "categoriaPacienteId")
    PacienteDTO toDto(Paciente s);

    @Named("ciudadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CiudadRefDTO toDtoCiudadId(Ciudad ciudad);

    @Named("categoriaPacienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    CategoriaPacienteDTO toDtoCategoriaPacienteId(CategoriaPaciente categoriaPaciente);
}
