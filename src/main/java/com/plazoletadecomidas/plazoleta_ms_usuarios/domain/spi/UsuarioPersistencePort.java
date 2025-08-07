package com.plazoletadecomidas.plazoleta_ms_usuarios.domain.spi;

import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Usuario;

public interface UsuarioPersistencePort {
    Usuario saveUsuario(Usuario usuario);
    boolean existsEmail(String email);
    Usuario findByEmail(String email);
}
