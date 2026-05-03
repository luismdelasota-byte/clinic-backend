package com.clinicapp.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "Username o Email requerido")
    private String usernameOrEmail;

    @NotBlank(message = "Password requerido")
    private String password;
}