package com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.input.rest;

import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.LoginRequestDto;
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
    public ResponseEntity<UsuarioResponseDto> createOwner(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody @Valid UsuarioRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioHandler.createOwner(dto, token));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequest) {
        String token = usuarioHandler.login(loginRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/employees")
    public ResponseEntity<UsuarioResponseDto> createEmployee(
            @RequestHeader(value = "Authorization" , required = false) String token,
            @RequestBody @Valid UsuarioRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioHandler.createEmployee(dto, token));
    }

}
