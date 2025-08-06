package com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioResponseDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.handler.UsuarioHandler;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Rol;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioHandler usuarioHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearPropietario_retorna201YDtoCorrecto() throws Exception {
        // Arrange
        UsuarioRequestDto requestDto = new UsuarioRequestDto();
        requestDto.setFirstName("Evelyn");
        requestDto.setLastName("Kalil");
        requestDto.setDocumentId("123456789");
        requestDto.setPhone("+573005678910");
        requestDto.setBirthDate(LocalDate.of(2000, 8, 5));
        requestDto.setEmail("evelyn@correo.com");
        requestDto.setPassword("1234");

        UsuarioResponseDto responseDto = new UsuarioResponseDto(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "Evelyn",
                "evelyn@correo.com",
                Rol.PROPIETARIO
        );

        Mockito.when(usuarioHandler.crearPropietario(Mockito.any()))
                .thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/users/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("11111111-1111-1111-1111-111111111111"))
                .andExpect(jsonPath("$.firstName").value("Evelyn"))
                .andExpect(jsonPath("$.email").value("evelyn@correo.com"))
                .andExpect(jsonPath("$.rol").value("PROPIETARIO"));
    }
}
