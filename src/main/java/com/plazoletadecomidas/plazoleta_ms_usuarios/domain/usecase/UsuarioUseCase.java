package com.plazoletadecomidas.plazoleta_ms_usuarios.domain.usecase;

import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.api.UsuarioServicePort;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Usuario;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.spi.UsuarioPersistencePort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public class UsuarioUseCase implements UsuarioServicePort {

    private final UsuarioPersistencePort persistencePort;

    public UsuarioUseCase(UsuarioPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public Usuario crearPropietario(Usuario usuario) {
        if (!esMayorDeEdad(usuario.getBirthDate())) {
            throw new IllegalArgumentException("El usuario debe ser mayor de edad.");
        }

        if (persistencePort.existsEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }

        usuario.setRole(com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Rol.PROPIETARIO);
        usuario.setPasswordHash(new BCryptPasswordEncoder().encode(usuario.getPasswordHash()));

        usuario.setId(UUID.randomUUID());

        return persistencePort.guardarUsuario(usuario);
    }

    private boolean esMayorDeEdad(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears() >= 18;
    }
}
