package com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioClientRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioEmployeeRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioOwnerRequestDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto.UsuarioResponseDto;
import com.plazoletadecomidas.plazoleta_ms_usuarios.application.handler.UsuarioHandler;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Role;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.exceptionhandler.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsuarioController.class, excludeAutoConfiguration = { SecurityAutoConfiguration.class })
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UsuarioControllerValidationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private UsuarioHandler usuarioHandler;

    // ---------- OWNERS ----------

    @Test
    @DisplayName("Owners: rechaza menor de edad con 400 y mensaje")
    void owner_menorDeEdad_400() throws Exception {
        UsuarioOwnerRequestDto dto = ownerDto();
        dto.setBirthDate(LocalDate.now().minusYears(10)); // menor

        when(usuarioHandler.createOwner(any(UsuarioOwnerRequestDto.class), anyString()))
                .thenThrow(new IllegalArgumentException("El usuario debe ser mayor de edad."));

        mockMvc.perform(post("/users/owners")
                        .header("Authorization", "fake-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El usuario debe ser mayor de edad."));
    }

    @Test
    @DisplayName("Owners: rechaza correo duplicado con 400 y mensaje")
    void owner_correoDuplicado_400() throws Exception {
        UsuarioOwnerRequestDto dto = ownerDto();

        when(usuarioHandler.createOwner(any(UsuarioOwnerRequestDto.class), anyString()))
                .thenThrow(new IllegalArgumentException("El correo ya está registrado."));

        mockMvc.perform(post("/users/owners")
                        .header("Authorization", "fake-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El correo ya está registrado."));
    }

    @Test
    @DisplayName("Owners: valida formato de email (Bean Validation) → 400")
    void owner_emailInvalido_400() throws Exception {
        UsuarioOwnerRequestDto dto = ownerDto();
        dto.setEmail("correoSinArroba");

        mockMvc.perform(post("/users/owners")
                        .header("Authorization", "fake-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }

    // ---------- EMPLOYEES ----------

    @Test
    @DisplayName("Employees: rechaza menor de edad con 400 y mensaje")
    void employee_menorDeEdad_400() throws Exception {
        UsuarioEmployeeRequestDto dto = employeeDto();
        dto.setBirthDate(LocalDate.now().minusYears(10));

        when(usuarioHandler.createEmployee(any(UsuarioEmployeeRequestDto.class), anyString()))
                .thenThrow(new IllegalArgumentException("El usuario debe ser mayor de edad."));

        mockMvc.perform(post("/users/employees")
                        .header("Authorization", "fake-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El usuario debe ser mayor de edad."));
    }

    @Test
    @DisplayName("Employees: rechaza correo duplicado con 400 y mensaje")
    void employee_correoDuplicado_400() throws Exception {
        UsuarioEmployeeRequestDto dto = employeeDto();

        when(usuarioHandler.createEmployee(any(UsuarioEmployeeRequestDto.class), anyString()))
                .thenThrow(new IllegalArgumentException("El correo ya está registrado."));

        mockMvc.perform(post("/users/employees")
                        .header("Authorization", "fake-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El correo ya está registrado."));
    }

    @Test
    @DisplayName("Employees: valida formato de email (Bean Validation) → 400")
    void employee_emailInvalido_400() throws Exception {
        UsuarioEmployeeRequestDto dto = employeeDto();
        dto.setEmail("sinArroba");

        mockMvc.perform(post("/users/employees")
                        .header("Authorization", "fake-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }

    // ---------- CLIENTS ----------

    @Test
    @DisplayName("Clients: crea cliente válido → 201")
    void client_valido_201() throws Exception {
        UsuarioClientRequestDto dto = clientDto();

        UUID id = UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851");
        when(usuarioHandler.createClient(any(UsuarioClientRequestDto.class)))
                .thenReturn(new UsuarioResponseDto(
                        id, dto.getFirstName(), dto.getEmail(), Role.CLIENTE
                ));

        mockMvc.perform(post("/users/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.firstName").value(dto.getFirstName()))
                .andExpect(jsonPath("$.email").value(dto.getEmail()))
                .andExpect(jsonPath("$.role").value("CLIENTE"));
    }

    @Test
    @DisplayName("Clients: rechaza menor de edad con 400 y mensaje")
    void client_menorDeEdad_400() throws Exception {
        UsuarioClientRequestDto dto = clientDto();
        dto.setBirthDate(LocalDate.now().minusYears(10));

        when(usuarioHandler.createClient(any(UsuarioClientRequestDto.class)))
                .thenThrow(new IllegalArgumentException("El usuario debe ser mayor de edad."));

        mockMvc.perform(post("/users/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El usuario debe ser mayor de edad."));
    }

    @Test
    @DisplayName("Clients: rechaza correo duplicado con 400 y mensaje")
    void client_correoDuplicado_400() throws Exception {
        UsuarioClientRequestDto dto = clientDto();

        when(usuarioHandler.createClient(any(UsuarioClientRequestDto.class)))
                .thenThrow(new IllegalArgumentException("El correo ya está registrado."));

        mockMvc.perform(post("/users/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El correo ya está registrado."));
    }

    @Test
    @DisplayName("Clients: valida formato de email (Bean Validation) → 400")
    void client_emailInvalido_400() throws Exception {
        UsuarioClientRequestDto dto = clientDto();
        dto.setEmail("sinArroba");

        mockMvc.perform(post("/users/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }

    // ---------- Helpers ----------

    private UsuarioOwnerRequestDto ownerDto() {
        UsuarioOwnerRequestDto dto = new UsuarioOwnerRequestDto();
        dto.setFirstName("Dueño");
        dto.setLastName("Apellido");
        dto.setDocumentId("12345678");
        dto.setPhone("+573001112233");
        dto.setBirthDate(LocalDate.of(1990, 1, 1));
        dto.setEmail("dueno@correo.com");
        dto.setPassword("clave123");
        return dto;
    }

    private UsuarioEmployeeRequestDto employeeDto() {
        UsuarioEmployeeRequestDto dto = new UsuarioEmployeeRequestDto();
        dto.setFirstName("Empleado");
        dto.setLastName("Uno");
        dto.setDocumentId("87654321");
        dto.setPhone("+573009998877");
        dto.setBirthDate(LocalDate.of(1995, 5, 5));
        dto.setEmail("empleado@correo.com");
        dto.setPassword("clave123");
        dto.setRestaurantId(UUID.randomUUID());
        return dto;
    }

    private UsuarioClientRequestDto clientDto() {
        UsuarioClientRequestDto dto = new UsuarioClientRequestDto();
        dto.setFirstName("Cliente");
        dto.setLastName("Final");
        dto.setDocumentId("111222333");
        dto.setPhone("+573001112244");
        dto.setBirthDate(LocalDate.of(1998, 3, 10));
        dto.setEmail("cliente@correo.com");
        dto.setPassword("1234");
        return dto;
    }
}
