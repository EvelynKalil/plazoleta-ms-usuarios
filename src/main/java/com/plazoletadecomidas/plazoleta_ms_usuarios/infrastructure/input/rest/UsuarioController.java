package com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.input.rest;

import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.LoginRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.PhoneResponseDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioClientRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioOwnerRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioEmployeeRequestDto;
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
            @RequestBody @Valid UsuarioOwnerRequestDto dto
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
            @RequestBody @Valid UsuarioEmployeeRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioHandler.createEmployee(dto, token));
    }

    @PostMapping("/clients")
    public ResponseEntity<UsuarioResponseDto> createClient(@RequestBody @Valid UsuarioClientRequestDto dto) {
        UsuarioResponseDto created = usuarioHandler.createClient(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/phone")
    public ResponseEntity<PhoneResponseDto> getPhone(
            @PathVariable java.util.UUID id,
            @RequestHeader(value = "Authorization", required = false) String token // por si validas
    ) {
        return ResponseEntity.ok(usuarioHandler.getPhoneById(id));
    }

}
