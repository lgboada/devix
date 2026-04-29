package com.devix.service.mapper;

import com.devix.domain.Bodega;
import com.devix.domain.Centro;
import com.devix.domain.User;
import com.devix.domain.UsuarioCentroBodega;
import com.devix.service.dto.BodegaDTO;
import com.devix.service.dto.CentroDTO;
import com.devix.service.dto.UserDTO;
import com.devix.service.dto.UsuarioCentroBodegaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UsuarioCentroBodega} and its DTO {@link UsuarioCentroBodegaDTO}.
 */
@Mapper(componentModel = "spring")
public interface UsuarioCentroBodegaMapper extends EntityMapper<UsuarioCentroBodegaDTO, UsuarioCentroBodega> {
    @Override
    @Mapping(target = "centro", source = "centro", qualifiedByName = "centroDescripcion")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "bodega", source = "bodega", qualifiedByName = "bodegaCodigoNombre")
    UsuarioCentroBodegaDTO toDto(UsuarioCentroBodega entity);

    @Override
    @Mapping(target = "centro", source = "centro", qualifiedByName = "centroFromId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userFromId")
    @Mapping(target = "bodega", source = "bodega", qualifiedByName = "bodegaFromId")
    UsuarioCentroBodega toEntity(UsuarioCentroBodegaDTO dto);

    @Named("centroDescripcion")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "descripcion", source = "descripcion")
    CentroDTO toDtoCentroDescripcion(Centro centro);

    @Named("centroFromId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Centro centroFromCentroDTO(CentroDTO centroDto);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("userFromId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    User userFromUserDTO(UserDTO userDto);

    @Named("bodegaCodigoNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codigo", source = "codigo")
    @Mapping(target = "nombre", source = "nombre")
    BodegaDTO toDtoBodegaCodigoNombre(Bodega bodega);

    @Named("bodegaFromId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Bodega bodegaFromBodegaDTO(BodegaDTO bodegaDto);
}
