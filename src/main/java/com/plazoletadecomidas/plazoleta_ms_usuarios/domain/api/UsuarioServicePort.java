package com.plazoletadecomidas.plazoleta_ms_usuarios.domain.api;

import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Usuario;
import java.util.UUID;

public interface UsuarioServicePort {

    Usuario createOwner(Usuario usuario);
    Usuario saveUsuario(Usuario usuario);
    Usuario findByEmail(String email);
    Usuario createClient(Usuario model);
    Usuario createEmployee(Usuario usuario, UUID restaurantId);
    Usuario findById(UUID id);
    String getPhone(UUID id);
    String getRoleById(UUID id);
}
