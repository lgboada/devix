package com.devix.service.mapper;

import com.devix.domain.Centro;
import com.devix.domain.User;
import com.devix.domain.UsuarioCentro;
import com.devix.service.dto.CentroDTO;
import com.devix.service.dto.UserDTO;
import com.devix.service.dto.UsuarioCentroDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UsuarioCentro} and its DTO {@link UsuarioCentroDTO}.
 */
@Mapper(componentModel = "spring")
public interface UsuarioCentroMapper extends EntityMapper<UsuarioCentroDTO, UsuarioCentro> {
    @Mapping(target = "centro", source = "centro", qualifiedByName = "centroDescripcion")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    UsuarioCentroDTO toDto(UsuarioCentro s);

    @Named("centroDescripcion")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "descripcion", source = "descripcion")
    CentroDTO toDtoCentroDescripcion(Centro centro);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
