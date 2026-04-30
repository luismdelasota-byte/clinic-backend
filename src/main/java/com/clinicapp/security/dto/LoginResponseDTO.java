package com.clinicapp.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;      // JWT generado
    private String username;   // Nombre del usuario
    private String role;       // Rol del usuario (ej: ADMIN)
}