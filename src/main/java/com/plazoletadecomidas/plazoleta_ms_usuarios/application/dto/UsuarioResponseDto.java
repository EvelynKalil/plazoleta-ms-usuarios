package com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto;

import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UsuarioResponseDto {
    private UUID id;
    private String firstName;
    private String email;
    private Rol rol;

}
