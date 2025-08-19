package com.plazoletadecomidas.plazoleta_ms_usuarios.domain.usecase;

import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Role;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Usuario;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.spi.UsuarioPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UsuarioUseCaseTest {

    @Mock
    private UsuarioPersistencePort usuarioPersistencePort;

    @InjectMocks
    private UsuarioUseCase usuarioUseCase;

    private final String email = "evelyn@correo.com";
    private final String password = "1234";

    private Usuario buildValidUsuario() {
        return new Usuario(
                null,
                "Evelyn",
                "Kalil",
                "123456789",
                "+573005678910",
                LocalDate.of(2000, 8, 5),
                email,
                password,
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
                () -> assertNotEquals(password, creado.getPasswordHash()),
                () -> assertTrue(encoder.matches(password, creado.getPasswordHash()))
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

    @Test
    void createEmployee_conDatosValidos_deberiaGuardarConRolEmpleado() {
        // Arrange
        Usuario usuario = new Usuario(
                null,
                "Empleado",
                "Uno",
                "321321321",
                "+573001234567",
                LocalDate.of(1999, 1, 1),
                "empleado@correo.com",
                "clave123",
                null
        );

        UUID restaurantId = UUID.randomUUID();

        when(usuarioPersistencePort.existsEmail(usuario.getEmail())).thenReturn(false);
        when(usuarioPersistencePort.saveUsuario(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        Usuario creado = usuarioUseCase.createEmployee(usuario, restaurantId);

        // Assert
        assertEquals(Role.EMPLEADO, creado.getRole());
        assertTrue(new BCryptPasswordEncoder().matches("clave123", creado.getPasswordHash()));
        verify(usuarioPersistencePort).saveUsuario(any());
    }


    @Test
    void createClient_conDatosValidos_deberiaGuardarConRolCliente() {
        Usuario usuario = new Usuario(
                null,
                "Cliente",
                "Uno",
                "321321321",
                "+573001234567",
                LocalDate.of(1999, 1, 1),
                "cliente@correo.com",
                "clave123",
                null
        );

        when(usuarioPersistencePort.existsEmail(usuario.getEmail())).thenReturn(false);
        when(usuarioPersistencePort.saveUsuario(any())).thenAnswer(i -> i.getArgument(0));

        Usuario creado = usuarioUseCase.createClient(usuario);

        assertEquals(Role.CLIENTE, creado.getRole());
        assertTrue(new BCryptPasswordEncoder().matches("clave123", creado.getPasswordHash()));
        verify(usuarioPersistencePort).saveUsuario(any());
    }
}
