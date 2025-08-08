package com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioResponseDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.handler.UsuarioHandler;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Role;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
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
    @DisplayName("POST /users/owners - debería crear un propietario y retornar 201 con DTO correcto")
    void createOwner_deberiaRetornar201YUsuarioResponseDto() throws Exception {
        // Arrange
        UsuarioRequestDto requestDto = new UsuarioRequestDto();
        requestDto.setFirstName("Evelyn");
        requestDto.setLastName("Kalil");
        requestDto.setDocumentId("123456789");
        requestDto.setPhone("+573005678910");
        requestDto.setBirthDate(LocalDate.of(2000, 8, 5));
        requestDto.setEmail("evelyn@correo.com");
        requestDto.setPassword("1234");

        UUID expectedId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        UsuarioResponseDto responseDto = new UsuarioResponseDto(
                expectedId,
                "Evelyn",
                "evelyn@correo.com",
                Role.PROPIETARIO
        );

        BDDMockito.given(usuarioHandler.createOwner(
                any(UsuarioRequestDto.class),
                anyString()
        )).willReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/users/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer fake-token") // Simula token
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expectedId.toString()))
                .andExpect(jsonPath("$.firstName").value("Evelyn"))
                .andExpect(jsonPath("$.email").value("evelyn@correo.com"))
                .andExpect(jsonPath("$.role").value("PROPIETARIO"));
    }

    @Test
    void createEmployee_retorna201YDtoCorrecto() throws Exception {
        UsuarioRequestDto requestDto = new UsuarioRequestDto();
        requestDto.setFirstName("Empleado");
        requestDto.setLastName("Uno");
        requestDto.setDocumentId("123123");
        requestDto.setPhone("+573000000000");
        requestDto.setBirthDate(LocalDate.of(1995, 1, 1));
        requestDto.setEmail("empleado@correo.com");
        requestDto.setPassword("clave");

        UsuarioResponseDto responseDto = new UsuarioResponseDto(
                UUID.randomUUID(),
                "Empleado",
                "empleado@correo.com",
                Role.EMPLEADO
        );

        when(usuarioHandler.createEmployee(any(), anyString())).thenReturn(responseDto);

        mockMvc.perform(post("/users/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "fake-token")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Empleado"))
                .andExpect(jsonPath("$.email").value("empleado@correo.com"))
                .andExpect(jsonPath("$.role").value("EMPLEADO"));
    }

    @Test
    @DisplayName("POST /users/clients - debería crear un cliente y retornar 201 con DTO correcto")
    void createClient_retorna201YDtoCorrecto() throws Exception {
        UsuarioRequestDto requestDto = new UsuarioRequestDto();
        requestDto.setFirstName("Cliente");
        requestDto.setLastName("Uno");
        requestDto.setDocumentId("999999");
        requestDto.setPhone("+573001112233");
        requestDto.setBirthDate(LocalDate.of(1999, 1, 1));
        requestDto.setEmail("cliente@correo.com");
        requestDto.setPassword("clave123");

        UsuarioResponseDto responseDto = new UsuarioResponseDto(
                UUID.randomUUID(),
                "Cliente",
                "cliente@correo.com",
                Role.CLIENTE
        );

        when(usuarioHandler.createClient(any())).thenReturn(responseDto);

        mockMvc.perform(post("/users/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Cliente"))
                .andExpect(jsonPath("$.email").value("cliente@correo.com"))
                .andExpect(jsonPath("$.role").value("CLIENTE"));
    }
}
