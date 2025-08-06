package com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.input.rest;

import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioResponseDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.handler.UsuarioHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsuarioController {

    private final UsuarioHandler usuarioHandler;

    public UsuarioController(UsuarioHandler usuarioHandler) {
        this.usuarioHandler = usuarioHandler;
    }

    @PostMapping("/owners")
    public ResponseEntity<UsuarioResponseDto> crearPropietario(@Valid @RequestBody UsuarioRequestDto request) {
        UsuarioResponseDto response = usuarioHandler.crearPropietario(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
