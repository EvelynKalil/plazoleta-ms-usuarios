package com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.handler.UsuarioHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioHandler usuarioHandler;

    @Test
    @DisplayName("Debería rechazar usuario menor de edad con error 400")
    void crearUsuario_menorDeEdad_retorna400() throws Exception {
        UsuarioRequestDto dto = new UsuarioRequestDto();
        dto.setFirstName("Menor");
        dto.setLastName("DeEdad");
        dto.setDocumentId("123456");
        dto.setPhone("3001234567");
        dto.setEmail("menor@correo.com");
        dto.setPassword("clave123");
        dto.setBirthDate(LocalDate.now().minusYears(10)); // menor de edad

        when(usuarioHandler.createOwner(any(UsuarioRequestDto.class), anyString()))
                .thenThrow(new IllegalArgumentException("El usuario debe ser mayor de edad."));

        mockMvc.perform(post("/users/owners")
                        .header("Authorization", "fake-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El usuario debe ser mayor de edad."));
    }


    @Test
    @DisplayName("Debería rechazar correo ya registrado con error 400")
    void crearUsuario_correoYaRegistrado() throws Exception {
        UsuarioRequestDto dto = new UsuarioRequestDto();
        dto.setFirstName("Correo");
        dto.setLastName("Repetido");
        dto.setDocumentId("999999");
        dto.setPhone("3009999999");
        dto.setEmail("ya@registrado.com");
        dto.setPassword("clave123");
        dto.setBirthDate(LocalDate.of(2000, 1, 1));

        when(usuarioHandler.createOwner(any(UsuarioRequestDto.class), anyString()))
                .thenThrow(new IllegalArgumentException("El correo ya está registrado."));

        mockMvc.perform(post("/users/owners")
                        .header("Authorization", "fake-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El correo ya está registrado."));
    }


    @Test
    @DisplayName("Debería retornar 400 si el email no es válido")
    void crearUsuario_emailInvalido() throws Exception {
        UsuarioRequestDto dto = buildUsuarioDto();
        dto.setEmail("correoSinArroba");

        mockMvc.perform(post("/users/owners")
                        .header("Authorization", "fake-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }

    // Método de utilidad para no repetir datos
    private UsuarioRequestDto buildUsuarioDto() {
        UsuarioRequestDto dto = new UsuarioRequestDto();
        dto.setFirstName("Nombre");
        dto.setLastName("Apellido");
        dto.setDocumentId("12345678");
        dto.setPhone("3009999999");
        dto.setBirthDate(LocalDate.of(2000, 1, 1));
        dto.setEmail("ejemplo@correo.com");
        dto.setPassword("clave123");
        return dto;
    }
}
