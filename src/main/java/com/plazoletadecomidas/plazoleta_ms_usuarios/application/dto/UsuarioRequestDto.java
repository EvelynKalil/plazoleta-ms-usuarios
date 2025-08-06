package com.plazoletadecomidas.plazoleta_ms_usuarios.application.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.*;
import java.time.LocalDate;


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

    public UsuarioRequestDto() {
        this.firstName = firstName;
        this.lastName = lastName;
        this.documentId = documentId;
        this.phone = phone;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
    }
}
