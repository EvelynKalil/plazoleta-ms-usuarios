package com.plazoletadecomidas.plazoleta_ms_usuarios.domain.usecase;

import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Role;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Usuario;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.spi.UsuarioPersistencePort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    @Mock
    private UsuarioPersistencePort usuarioPersistencePort;

    @InjectMocks
    private UsuarioUseCase usuarioUseCase;

    private final String EMAIL = "evelyn@correo.com";
    private final String PASSWORD = "1234";

    private Usuario buildValidUsuario() {
        return new Usuario(
                null,
                "Evelyn",
                "Kalil",
                "123456789",
                "+573005678910",
                LocalDate.of(2000, 8, 5),
                EMAIL,
                PASSWORD,
                null
        );
    }

    @Test
    void createOwner_deberiaAsignarRolYEncriptarContraseña() {
        // Arrange
        Usuario usuario = buildValidUsuario();

        when(usuarioPersistencePort.existsEmail(usuario.getEmail())).thenReturn(false);
        when(usuarioPersistencePort.saveUsuario(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario creado = usuarioUseCase.createOwner(usuario);

        // Assert
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        assertAll("Validar usuario creado",
                () -> assertEquals(Role.PROPIETARIO, creado.getRole()),
                () -> assertNotEquals(PASSWORD, creado.getPasswordHash()),
                () -> assertTrue(encoder.matches(PASSWORD, creado.getPasswordHash()))
        );

        verify(usuarioPersistencePort).saveUsuario(creado);
    }

    @Test
    void createOwner_conCorreoExistente_deberiaLanzarExcepcion() {
        // Arrange
        Usuario usuario = buildValidUsuario();
        when(usuarioPersistencePort.existsEmail(usuario.getEmail())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioUseCase.createOwner(usuario);
        });

        assertEquals("El correo ya está registrado.", exception.getMessage());
        verify(usuarioPersistencePort, never()).saveUsuario(any());
    }

    @Test
    void createOwner_conUsuarioMenorDeEdad_deberiaLanzarExcepcion() {
        // Arrange
        Usuario usuario = buildValidUsuario();
        usuario.setBirthDate(LocalDate.now().minusYears(10)); // menor de edad

        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            usuarioUseCase.createOwner(usuario);
        });

        assertEquals("El usuario debe ser mayor de edad.", ex.getMessage());
        verify(usuarioPersistencePort, never()).saveUsuario(any());
    }
}
