package com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.configuration;

import com.plazoletadecomidas.plazoleta_ms_usuarios.application.handler.UsuarioHandler;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.mapper.UsuarioMapper;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.api.UsuarioServicePort;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.spi.UsuarioPersistencePort;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.usecase.UsuarioUseCase;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.client.RestaurantClient;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.output.jpa.adapter.UsuarioJpaAdapter;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.output.jpa.mapper.UsuarioEntityMapper;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.output.jpa.repository.UsuarioRepository;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.security.AuthValidator;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public UsuarioPersistencePort usuarioPersistencePort(UsuarioRepository repository, UsuarioEntityMapper mapper) {
        return new UsuarioJpaAdapter(repository, mapper);
    }

    @Bean
    public UsuarioServicePort usuarioServicePort(UsuarioPersistencePort port) {
        return new UsuarioUseCase(port);
    }

    @Bean
    public UsuarioHandler usuarioHandler(UsuarioServicePort servicePort, UsuarioMapper mapper, AuthValidator authValidator, JwtUtil jwtUtil, RestaurantClient restaurantClient) {
        return new UsuarioHandler(servicePort, mapper, authValidator, jwtUtil, restaurantClient);
    }
}
