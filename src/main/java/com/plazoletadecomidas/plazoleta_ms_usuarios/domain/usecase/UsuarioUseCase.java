package com.plazoletadecomidas.plazoleta_ms_usuarios.domain.usecase;

import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.api.UsuarioServicePort;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Role;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Usuario;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.spi.UsuarioPersistencePort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.webjars.NotFoundException;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public class UsuarioUseCase implements UsuarioServicePort {

    private final UsuarioPersistencePort persistencePort;

    public UsuarioUseCase(UsuarioPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public Usuario createOwner(Usuario usuario) {
        if (!esMayorDeEdad(usuario.getBirthDate())) {
            throw new IllegalArgumentException("El usuario debe ser mayor de edad.");
        }

        if (persistencePort.existsEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }

        usuario.setRole(com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Role.PROPIETARIO);
        usuario.setPasswordHash(new BCryptPasswordEncoder().encode(usuario.getPasswordHash()));

        usuario.setId(UUID.randomUUID());
        return persistencePort.saveUsuario(usuario);
    }

    private boolean esMayorDeEdad(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears() >= 18;
    }

    @Override
    public Usuario findByEmail(String email) {
        return persistencePort.findByEmail(email);
    }

    @Override
    public Usuario saveUsuario(Usuario usuario) {
        return persistencePort.saveUsuario(usuario);
    }



    @Override
    public Usuario createEmployee(Usuario usuario, UUID restaurantId) {
        if (persistencePort.existsEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }

        if (!esMayorDeEdad(usuario.getBirthDate())) {
            throw new IllegalArgumentException("El usuario debe ser mayor de edad.");
        }

        usuario.setRole(Role.EMPLEADO);
        usuario.setPasswordHash(new BCryptPasswordEncoder().encode(usuario.getPasswordHash()));

        return persistencePort.saveUsuario(usuario);
    }


    @Override
    public Usuario createClient(Usuario usuario) {
        if (persistencePort.existsEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }

        if (!esMayorDeEdad(usuario.getBirthDate())) {
            throw new IllegalArgumentException("El usuario debe ser mayor de edad.");
        }

        usuario.setRole(Role.CLIENTE);
        usuario.setPasswordHash(new BCryptPasswordEncoder().encode(usuario.getPasswordHash()));
        return persistencePort.saveUsuario(usuario);
    }

    @Override
    public Usuario findById(UUID id) {
        return persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con id " + id));
    }

    @Override
    public String getPhone(UUID id) {
        return findById(id).getPhone();
    }
}
