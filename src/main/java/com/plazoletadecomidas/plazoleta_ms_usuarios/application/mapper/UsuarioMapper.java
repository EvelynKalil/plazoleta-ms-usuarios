package com.plazoletadecomidas.plazoleta_ms_usuarios.application.mapper;

import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioClientRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioOwnerRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioEmployeeRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioResponseDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Role;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    // Para empleados
    public Usuario toModel(UsuarioEmployeeRequestDto dto, Role role) {
        return Usuario.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .documentId(dto.getDocumentId())
                .phone(dto.getPhone())
                .birthDate(dto.getBirthDate())
                .email(dto.getEmail())
                .passwordHash(dto.getPassword()) // Se cifra después en el UseCase
                .role(role)
                .build();
    }

    // Para clientes
    public Usuario toModel(UsuarioClientRequestDto dto, Role role) {
        return Usuario.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .documentId(dto.getDocumentId())
                .phone(dto.getPhone())
                .birthDate(dto.getBirthDate())
                .email(dto.getEmail())
                .passwordHash(dto.getPassword()) // Se cifra después en el UseCase
                .role(role)
                .build();
    }

    //Para propietarios
    public Usuario toModel(UsuarioOwnerRequestDto dto) {
        return Usuario.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .documentId(dto.getDocumentId())
                .phone(dto.getPhone())
                .birthDate(dto.getBirthDate())
                .email(dto.getEmail())
                .passwordHash(dto.getPassword()) // se cifra después
                .role(Role.PROPIETARIO)
                .build();
    }

    // Convierte un dominio a DTO de respuesta
    public UsuarioResponseDto toResponseDto(Usuario usuario) {
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getFirstName(),
                usuario.getEmail(),
                usuario.getRole()
        );
    }

    // Método rápido para crear un cliente directamente con rol CLIENTE
    public Usuario toModel(UsuarioClientRequestDto dto) {
        return toModel(dto, Role.CLIENTE);
    }
}
