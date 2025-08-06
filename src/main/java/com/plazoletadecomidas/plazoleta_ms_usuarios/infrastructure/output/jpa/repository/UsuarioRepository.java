package com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.output.jpa.repository;

import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.output.jpa.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    boolean existsByEmail(String email);
}
