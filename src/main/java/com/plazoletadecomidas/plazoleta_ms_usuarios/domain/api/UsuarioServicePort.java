package com.plazoletadecomidas.plazoleta_ms_usuarios.domain.api;

import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Usuario;

public interface UsuarioServicePort {

    Usuario createOwner(Usuario usuario);
    Usuario saveUsuario(Usuario usuario);
    Usuario findByEmail(String email);
    Usuario createEmployee(Usuario model);
    Usuario createClient(Usuario model);
}
