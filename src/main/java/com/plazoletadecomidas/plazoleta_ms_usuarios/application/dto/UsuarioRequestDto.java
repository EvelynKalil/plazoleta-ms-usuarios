package com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;


@Getter
@Setter
public class UsuarioRequestDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^\\d+$", message = "El documento solo debe contener números")
    private String documentId;

    @NotBlank
    @Pattern(regexp = "^\\+?\\d{1,13}$", message = "Celular debe tener máximo 13 dígitos y puede incluir +")
    private String phone;

    @NotNull
    private LocalDate birthDate;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private UUID restaurantId;
}
