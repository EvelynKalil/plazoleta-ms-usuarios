package com.plazoletadecomidas.plazoleta_ms_usuarios.domain.usecase;

import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Rol;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Usuario;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.spi.UsuarioPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioUseCaseTest {

    private UsuarioPersistencePort usuarioPersistencePort;
    private UsuarioUseCase usuarioUseCase;

    @BeforeEach
    void setUp() {
        usuarioPersistencePort = mock(UsuarioPersistencePort.class);
        usuarioUseCase = new UsuarioUseCase(usuarioPersistencePort);
    }

    @Test
    void crearPropietario_deberiaAsignarRolYEncriptarContraseña() {
        // Arrange
        Usuario usuario = new Usuario(
                null,
                "Evelyn",
                "Kalil",
                "123456789",
                "+573005678910",
                LocalDate.of(2000, 8, 5), // mayor de edad
                "evelyn@correo.com",
                "1234",
                null
        );

        when(usuarioPersistencePort.existsEmail(usuario.getEmail())).thenReturn(false);
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Usuario creado = usuarioUseCase.crearPropietario(usuario);

        // Assert
        assertEquals(Rol.PROPIETARIO, creado.getRole());
        assertNotEquals("1234", creado.getPasswordHash());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches("1234", creado.getPasswordHash()));

        verify(usuarioPersistencePort, times(1)).guardarUsuario(creado);
    }

    @Test
    void crearPropietario_conCorreoExistente_deberiaLanzarExcepcion() {
        // Arrange
        Usuario usuario = new Usuario(
                null,
                "Evelyn",
                "Kalil",
                "123456789",
                "+573005678910",
                LocalDate.of(2000, 8, 5), // mayor de edad
                "existe@correo.com",
                "clave",
                null
        );

        when(usuarioPersistencePort.existsEmail(usuario.getEmail())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioUseCase.crearPropietario(usuario);
        });

        assertEquals("El correo ya está registrado.", exception.getMessage());
        verify(usuarioPersistencePort, never()).guardarUsuario(any());
    }

    @Test
    void crearPropietario_conUsuarioMenorDeEdad_deberiaLanzarExcepcion() {
        // Arrange
        Usuario usuario = new Usuario(
                null,
                "Eve",
                "Young",
                "123456789",
                "+573005678910",
                LocalDate.now().minusYears(10),
                "menor@correo.com",
                "clave",
                null
        );

        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            usuarioUseCase.crearPropietario(usuario);
        });

        assertEquals("El usuario debe ser mayor de edad.", ex.getMessage());
        verify(usuarioPersistencePort, never()).guardarUsuario(any());
    }
}
