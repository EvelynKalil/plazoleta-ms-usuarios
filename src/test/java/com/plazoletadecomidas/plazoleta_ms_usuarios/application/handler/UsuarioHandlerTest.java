package com.plazoletadecomidas.plazoleta_ms_usuarios.application.handler;

import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.api.UsuarioServicePort;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Rol;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Usuario;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.mapper.UsuarioMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UsuarioHandlerTest {

    private UsuarioServicePort usuarioServicePort;
    private UsuarioMapper usuarioMapper;
    private UsuarioHandler usuarioHandler;

    @BeforeEach
    void setUp() {
        usuarioServicePort = mock(UsuarioServicePort.class);
        usuarioMapper = mock(UsuarioMapper.class);
        usuarioHandler = new UsuarioHandler(usuarioServicePort, usuarioMapper);
    }

    @Test
    void crearPropietario_deberiaLlamarCasoDeUsoConDatosCorrectos() {
        // Arrange
        UsuarioRequestDto requestDto = new UsuarioRequestDto();
        requestDto.setFirstName("Evelyn");
        requestDto.setLastName("Kalil");
        requestDto.setDocumentId("123456789");
        requestDto.setPhone("+573005678910");
        requestDto.setBirthDate(LocalDate.of(2000, 8, 5));
        requestDto.setEmail("evelyn@correo.com");
        requestDto.setPassword("1234");

        Usuario usuarioMock = new Usuario(
                null,
                "Evelyn",
                "Kalil",
                "123456789",
                "+573005678910",
                LocalDate.of(2000, 8, 5),
                "evelyn@correo.com",
                "1234",
                Rol.PROPIETARIO // o Rol.OWNER según lo tengas
        );

        when(usuarioMapper.toModel(requestDto)).thenReturn(usuarioMock);

        // Act
        usuarioHandler.crearPropietario(requestDto);

        // Assert
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioServicePort, times(1)).crearPropietario(captor.capture());

        Usuario capturedUser = captor.getValue();
        assertEquals("Evelyn", capturedUser.getFirstName());
        assertEquals("Kalil", capturedUser.getLastName());
        assertEquals("123456789", capturedUser.getDocumentId());
        assertEquals("+573005678910", capturedUser.getPhone());
        assertEquals("evelyn@correo.com", capturedUser.getEmail());
        assertEquals("1234", capturedUser.getPasswordHash());
        assertEquals(LocalDate.of(2000, 8, 5), capturedUser.getBirthDate());
        assertEquals(Rol.PROPIETARIO, capturedUser.getRole());
    }
}
