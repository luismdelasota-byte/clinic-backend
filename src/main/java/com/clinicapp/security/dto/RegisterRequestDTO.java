package com.clinicapp.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "Username requerido")
    private String username;

    @Email(message = "El correo electrónico debe ser válido.")
    @NotBlank(message = "Email es requerido")
    private String email;

    @NotBlank(message = "Password requerido")
    private String password;

    @NotBlank(message = "Rol requerido")
    private String role; //"USER" o "ADMIN" o "DOCTOR"

}