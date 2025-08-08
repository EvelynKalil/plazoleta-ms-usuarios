package com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioResponseDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.handler.UsuarioHandler;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @Test
    @DisplayName("Debería rechazar empleado menor de edad con error 400")
    void crearEmpleado_menorDeEdad_retorna400() throws Exception {
        UsuarioRequestDto dto = buildUsuarioDto();
        dto.setBirthDate(LocalDate.now().minusYears(10)); // menor de edad

        when(usuarioHandler.createEmployee(any(UsuarioRequestDto.class), anyString()))
                .thenThrow(new IllegalArgumentException("El usuario debe ser mayor de edad."));

        mockMvc.perform(post("/users/employees")
                        .header("Authorization", "fake-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El usuario debe ser mayor de edad."));
    }

    @Test
    @DisplayName("Debería rechazar email ya registrado para empleado con error 400")
    void crearEmpleado_emailDuplicado_retorna400() throws Exception {
        UsuarioRequestDto dto = buildUsuarioDto();
        dto.setEmail("empleado@correo.com");

        when(usuarioHandler.createEmployee(any(UsuarioRequestDto.class), anyString()))
                .thenThrow(new IllegalArgumentException("El correo ya está registrado."));

        mockMvc.perform(post("/users/employees")
                        .header("Authorization", "fake-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El correo ya está registrado."));
    }

    @Test
    @DisplayName("Debería rechazar formato de email inválido al crear empleado")
    void crearEmpleado_emailInvalido_retorna400() throws Exception {
        UsuarioRequestDto dto = buildUsuarioDto();
        dto.setEmail("sinArroba");

        mockMvc.perform(post("/users/employees")
                        .header("Authorization", "fake-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    @DisplayName("Debería crear cliente exitosamente")
    void crearCliente_valido_retorna201() throws Exception {
        UsuarioRequestDto dto = buildUsuarioDto();

        var responseJson = """
            {
              "id": "d290f1ee-6c54-4b01-90e6-d701748f0851",
              "firstName": "Nombre",
              "email": "ejemplo@correo.com",
              "role": "CLIENTE"
            }
            """;

        when(usuarioHandler.createClient(any(UsuarioRequestDto.class)))
                .thenReturn(new UsuarioResponseDto(
                        UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851"),
                        dto.getFirstName(),
                        dto.getEmail(),
                        Role.CLIENTE
                ));

        mockMvc.perform(post("/users/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(responseJson));
    }

    @Test
    @DisplayName("Debería rechazar cliente menor de edad con error 400")
    void crearCliente_menorDeEdad_retorna400() throws Exception {
        UsuarioRequestDto dto = buildUsuarioDto();
        dto.setBirthDate(LocalDate.now().minusYears(10)); // menor de edad

        when(usuarioHandler.createClient(any(UsuarioRequestDto.class)))
                .thenThrow(new IllegalArgumentException("El usuario debe ser mayor de edad."));

        mockMvc.perform(post("/users/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El usuario debe ser mayor de edad."));
    }

    @Test
    @DisplayName("Debería rechazar correo duplicado para cliente con error 400")
    void crearCliente_emailDuplicado_retorna400() throws Exception {
        UsuarioRequestDto dto = buildUsuarioDto();

        when(usuarioHandler.createClient(any(UsuarioRequestDto.class)))
                .thenThrow(new IllegalArgumentException("El correo ya está registrado."));

        mockMvc.perform(post("/users/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El correo ya está registrado."));
    }

    @Test
    @DisplayName("Debería rechazar email inválido al crear cliente")
    void crearCliente_emailInvalido_retorna400() throws Exception {
        UsuarioRequestDto dto = buildUsuarioDto();
        dto.setEmail("sinArroba");

        mockMvc.perform(post("/users/clients")
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
