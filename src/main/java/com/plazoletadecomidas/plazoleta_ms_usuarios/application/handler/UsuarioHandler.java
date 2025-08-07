package com.plazoletadecomidas.plazoleta_ms_usuarios.application.handler;

import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.LoginRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioResponseDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.mapper.UsuarioMapper;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.api.UsuarioServicePort;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Role;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Usuario;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.exception.UnauthorizedException;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.security.AuthValidator;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UsuarioHandler{

    private final UsuarioServicePort usuarioServicePort;
    private final UsuarioMapper mapper;
    private final AuthValidator authValidator;
    private final JwtUtil jwtUtil;

    public UsuarioHandler(UsuarioServicePort usuarioServicePort, UsuarioMapper mapper, AuthValidator authValidator, JwtUtil jwtUtil) {
        this.usuarioServicePort = usuarioServicePort;
        this.mapper = mapper;
        this.authValidator = authValidator;
        this.jwtUtil = jwtUtil;
    }

    public String login(LoginRequestDto request) {
        Usuario usuario = usuarioServicePort.findByEmail(request.getEmail());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            throw new UnauthorizedException("Contraseña incorrecta");
        }

        return jwtUtil.generateToken(usuario.getId(), usuario.getRole());
    }

//    private String generateFakeToken(Usuario usuario) {
//        return usuario.getId() + ":" + usuario.getRole().name(); // Simulación
//    }

    public UsuarioResponseDto createOwner(UsuarioRequestDto dto, String token) {
        authValidator.validate(token, Role.ADMINISTRADOR);
        Usuario model = mapper.toModel(dto, Role.PROPIETARIO);
        return mapper.toResponseDto(usuarioServicePort.createOwner(model));
    }
}
