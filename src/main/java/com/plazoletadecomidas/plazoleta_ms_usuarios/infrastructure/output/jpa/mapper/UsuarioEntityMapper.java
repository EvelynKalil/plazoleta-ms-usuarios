package com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.output.jpa.mapper;

import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Rol;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Usuario;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.output.jpa.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioEntityMapper {

    public UsuarioEntity toEntity(Usuario usuario) {
        return new UsuarioEntity(
                usuario.getId(),
                usuario.getFirstName(),
                usuario.getLastName(),
                usuario.getDocumentId(),
                usuario.getPhone(),
                usuario.getBirthDate(),
                usuario.getEmail(),
                usuario.getPasswordHash(),
                usuario.getRole()
        );
    }

    public Usuario toModel(UsuarioEntity entity) {
        return new Usuario(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getDocumentId(),
                entity.getPhone(),
                entity.getBirthDate(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getRol()
        );
    }
}
