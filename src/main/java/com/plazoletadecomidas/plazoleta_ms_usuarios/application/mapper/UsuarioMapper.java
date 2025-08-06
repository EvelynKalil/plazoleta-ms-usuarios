package com.plazoletadecomidas.plazoleta_ms_usuarios.application.mapper;

import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioResponseDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Usuario;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Rol;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toModel(UsuarioRequestDto dto) {
        return new Usuario(
                null,
                dto.getFirstName(),
                dto.getLastName(),
                dto.getDocumentId(),
                dto.getPhone(),
                dto.getBirthDate(),
                dto.getEmail(),
                dto.getPassword(), // aún sin encriptar
                null
        );
    }

    public UsuarioResponseDto toResponseDto(Usuario usuario) {
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getFirstName(),
                usuario.getEmail(),
                usuario.getRole()
        );
    }
}
