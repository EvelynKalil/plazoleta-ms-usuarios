package com.plazoletadecomidas.plazoleta_ms_usuarios.domain.spi;

import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Usuario;

public interface UsuarioPersistencePort {
    Usuario guardarUsuario(Usuario usuario);
    boolean existsEmail(String email);
}
