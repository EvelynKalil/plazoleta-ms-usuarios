package com.plazoletadecomidas.plazoleta_ms_usuarios.application.handler;

import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.LoginRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioResponseDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.mapper.UsuarioMapper;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.api.UsuarioServicePort;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Role;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Usuario;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.client.RestaurantClient;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.exception.UnauthorizedException;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.security.AuthValidator;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

public class UsuarioHandler{

    private final UsuarioServicePort usuarioServicePort;
    private final UsuarioMapper mapper;
    private final AuthValidator authValidator;
    private final JwtUtil jwtUtil;
    private final RestaurantClient restaurantClient;

    public UsuarioHandler(UsuarioServicePort usuarioServicePort, UsuarioMapper mapper, AuthValidator authValidator, JwtUtil jwtUtil, RestaurantClient restaurantClient) {
        this.usuarioServicePort = usuarioServicePort;
        this.mapper = mapper;
        this.authValidator = authValidator;
        this.jwtUtil = jwtUtil;
        this.restaurantClient = restaurantClient;
    }

    public String login(LoginRequestDto request) {
        Usuario usuario = usuarioServicePort.findByEmail(request.getEmail());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            throw new UnauthorizedException("Contraseña incorrecta");
        }

        return jwtUtil.generateToken(usuario.getId(), usuario.getRole());
    }

    public UsuarioResponseDto createOwner(UsuarioRequestDto dto, String token) {
        authValidator.validate(token, Role.ADMINISTRADOR);
        Usuario model = mapper.toModel(dto, Role.PROPIETARIO);
        return mapper.toResponseDto(usuarioServicePort.createOwner(model));
    }

    public UsuarioResponseDto createEmployee(UsuarioRequestDto dto, String token) {
        // 1) Solo propietarios pueden crear empleados y de paso obtienes su ownerId
        UUID ownerId = authValidator.validate(token, Role.PROPIETARIO);

        // 2) restaurantId obligatorio
        UUID restaurantId = dto.getRestaurantId();
        if (restaurantId == null) throw new IllegalArgumentException("El id del restaurante es obligatorio.");

        // 3) Valida ownership en plazoleta-ms
        boolean esDueno = Boolean.TRUE.equals(restaurantClient.isOwnerOfRestaurant(restaurantId, ownerId));
        if (!esDueno) throw new UnauthorizedException("No puedes asignar empleados a un restaurante que no te pertenece.");

        // 4) Crea el usuario con rol EMPLEADO
        Usuario model = mapper.toModel(dto, Role.EMPLEADO);
        Usuario creado = usuarioServicePort.createEmployee(model, restaurantId); // tu ServicePort pide 2 args

        // 5) Registra la relación en plazoleta-ms (persistencia real de la relación)
        String bearer = token.startsWith("Bearer ") ? token : "Bearer " + token;
        restaurantClient.addEmployeeToRestaurant(restaurantId,
                new RestaurantClient.AddEmployeeRequest(creado.getId()),
                bearer);

        return mapper.toResponseDto(creado);
    }

    public UsuarioResponseDto createClient(UsuarioRequestDto dto) {
        Usuario model = mapper.toModel(dto); // este método ya debe ser public
        Usuario created = usuarioServicePort.createClient(model);
        return mapper.toResponseDto(created);
    }



}
