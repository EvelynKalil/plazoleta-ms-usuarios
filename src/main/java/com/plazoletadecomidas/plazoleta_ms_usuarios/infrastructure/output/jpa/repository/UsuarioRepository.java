package com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.output.jpa.repository;

import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.output.jpa.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, UUID> {
    boolean existsByEmail(String email);
    Optional<UsuarioEntity> findByEmail(String email);
}
