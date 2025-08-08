package com.plazoletadecomidas.plazoleta_ms_usuarios.application.mapper;

import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioResponseDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Role;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toModel(UsuarioRequestDto dto, Role role) {
        return Usuario.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .documentId(dto.getDocumentId())
                .phone(dto.getPhone())
                .birthDate(dto.getBirthDate())
                .email(dto.getEmail())
                .passwordHash(dto.getPassword()) // correcto: se cifra después
                .role(role)
                .build();
    }

    public UsuarioResponseDto toResponseDto(Usuario usuario) {
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getFirstName(),
                usuario.getEmail(),
                usuario.getRole()
        );
    }

    public Usuario toModel(UsuarioRequestDto dto) {
        return toModel(dto, Role.CLIENTE);
    }
}
