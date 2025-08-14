package com.plazoletadecomidas.plazoleta_ms_usuarios.domain.spi;

import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Usuario;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioPersistencePort {
    Usuario saveUsuario(Usuario usuario);
    boolean existsEmail(String email);
    Usuario findByEmail(String email);
    Usuario createEmployee(Usuario usuario);
    Optional<Usuario> findById(UUID id);
}
