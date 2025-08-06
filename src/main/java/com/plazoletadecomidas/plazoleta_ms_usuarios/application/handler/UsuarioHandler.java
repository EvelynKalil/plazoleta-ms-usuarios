package com.plazoletadecomidas.plazoleta_ms_usuarios.application.handler;

import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioResponseDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.mapper.UsuarioMapper;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.api.UsuarioServicePort;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Usuario;

public class UsuarioHandler {

    private final UsuarioServicePort usuarioServicePort;
    private final UsuarioMapper mapper;

    public UsuarioHandler(UsuarioServicePort usuarioServicePort, UsuarioMapper mapper) {
        this.usuarioServicePort = usuarioServicePort;
        this.mapper = mapper;
    }

    public UsuarioResponseDto crearPropietario(UsuarioRequestDto dto) {
        Usuario usuario = mapper.toModel(dto);
        Usuario propietarioCreado = usuarioServicePort.crearPropietario(usuario);
        return mapper.toResponseDto(propietarioCreado);
    }
}
