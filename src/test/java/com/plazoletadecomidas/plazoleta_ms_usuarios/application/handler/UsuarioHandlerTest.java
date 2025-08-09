package com.plazoletadecomidas.plazoleta_ms_usuarios.application.handler;

import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.mapper.UsuarioMapper;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.api.UsuarioServicePort;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Role;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Usuario;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.client.RestaurantClient;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.security.AuthValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioHandlerTest {

    @Mock
    private UsuarioServicePort usuarioServicePort;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private AuthValidator authValidator;

    @InjectMocks
    private UsuarioHandler usuarioHandler;

    private UsuarioRequestDto requestDto;
    private Usuario usuarioMock;

    @Mock
    private RestaurantClient restaurantClient;

    @BeforeEach
    void setUp() {
        requestDto = new UsuarioRequestDto();
        requestDto.setFirstName("Evelyn");
        requestDto.setLastName("Kalil");
        requestDto.setDocumentId("123456789");
        requestDto.setPhone("+573005678910");
        requestDto.setBirthDate(LocalDate.of(2000, 8, 5));
        requestDto.setEmail("evelyn@correo.com");
        requestDto.setPassword("1234");

        usuarioMock = new Usuario(
                null,
                "Evelyn",
                "Kalil",
                "123456789",
                "+573005678910",
                LocalDate.of(2000, 8, 5),
                "evelyn@correo.com",
                "1234",
                Role.PROPIETARIO
        );
    }

    @Test
    void createOwner_deberiaLlamarCasoDeUsoConDatosCorrectos() {
        // Arrange
        UUID fakeAdminId = UUID.randomUUID();
        when(authValidator.validate("fake-token", Role.ADMINISTRADOR))
                .thenReturn(fakeAdminId);

        when(usuarioMapper.toModel(requestDto, Role.PROPIETARIO)).thenReturn(usuarioMock);
        when(usuarioServicePort.createOwner(any(Usuario.class))).thenReturn(usuarioMock);

        // Act
        usuarioHandler.createOwner(requestDto, "fake-token");

        // Assert
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioServicePort).createOwner(captor.capture());

        Usuario capturado = captor.getValue();

        assertAll("Validar datos del propietario",
                () -> assertEquals("Evelyn", capturado.getFirstName()),
                () -> assertEquals("Kalil", capturado.getLastName()),
                () -> assertEquals("123456789", capturado.getDocumentId()),
                () -> assertEquals("+573005678910", capturado.getPhone()),
                () -> assertEquals("evelyn@correo.com", capturado.getEmail()),
                () -> assertEquals("1234", capturado.getPasswordHash()),
                () -> assertEquals(LocalDate.of(2000, 8, 5), capturado.getBirthDate()),
                () -> assertEquals(Role.PROPIETARIO, capturado.getRole())
        );

        verify(authValidator).validate("fake-token", Role.ADMINISTRADOR);
        verify(usuarioMapper).toModel(requestDto, Role.PROPIETARIO);
    }

    @Test
    void createEmployee_deberiaLlamarCasoDeUsoConRolEmpleado() {
        // Arrange
        UUID restaurantId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        UUID empleadoId = UUID.randomUUID();

        UsuarioRequestDto empleadoRequest = new UsuarioRequestDto();
        empleadoRequest.setFirstName("Empleado");
        empleadoRequest.setLastName("Uno");
        empleadoRequest.setDocumentId("987654321");
        empleadoRequest.setPhone("+573001112233");
        empleadoRequest.setBirthDate(LocalDate.of(1995, 5, 5));
        empleadoRequest.setEmail("empleado@correo.com");
        empleadoRequest.setPassword("1234");
        empleadoRequest.setRestaurantId(restaurantId);

        Usuario empleadoModel = new Usuario(
                empleadoId,
                "Empleado",
                "Uno",
                "987654321",
                "+573001112233",
                LocalDate.of(1995, 5, 5),
                "empleado@correo.com",
                "1234",
                Role.EMPLEADO
        );

        when(authValidator.validate("fake-token", Role.PROPIETARIO)).thenReturn(ownerId);
        when(restaurantClient.isOwnerOfRestaurant(restaurantId, ownerId)).thenReturn(true);
        when(usuarioMapper.toModel(empleadoRequest, Role.EMPLEADO)).thenReturn(empleadoModel);
        when(usuarioServicePort.createEmployee(any(Usuario.class), eq(restaurantId))).thenReturn(empleadoModel);

        // Act
        usuarioHandler.createEmployee(empleadoRequest, "fake-token");

        // Assert
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioServicePort).createEmployee(captor.capture(), eq(restaurantId));

        Usuario creado = captor.getValue();
        assertEquals("Empleado", creado.getFirstName());
        assertEquals(Role.EMPLEADO, creado.getRole());
        verify(authValidator).validate("fake-token", Role.PROPIETARIO);
        verify(restaurantClient).addEmployeeToRestaurant(
                eq(restaurantId),
                argThat(req -> req.employeeId.equals(creado.getId())),
                anyString()
        );

    }


    @Test
    void createClient_deberiaLlamarCasoDeUsoConRolCliente() {
        // Arrange
        UsuarioRequestDto clienteRequest = new UsuarioRequestDto();
        clienteRequest.setFirstName("Cliente");
        clienteRequest.setLastName("Final");
        clienteRequest.setDocumentId("111222333");
        clienteRequest.setPhone("+573001112244");
        clienteRequest.setBirthDate(LocalDate.of(1990, 3, 10));
        clienteRequest.setEmail("cliente@correo.com");
        clienteRequest.setPassword("1234");

        Usuario clienteModel = new Usuario(
                null,
                "Cliente",
                "Final",
                "111222333",
                "+573001112244",
                LocalDate.of(1990, 3, 10),
                "cliente@correo.com",
                "1234",
                Role.CLIENTE
        );

        when(usuarioMapper.toModel(clienteRequest)).thenReturn(clienteModel);
        when(usuarioServicePort.createClient(any(Usuario.class))).thenReturn(clienteModel);

        // Act
        usuarioHandler.createClient(clienteRequest);

        // Assert
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioServicePort).createClient(captor.capture());

        Usuario creado = captor.getValue();
        assertEquals("Cliente", creado.getFirstName());
        assertEquals(Role.CLIENTE, creado.getRole());

        verify(usuarioMapper).toModel(clienteRequest);
    }


}
