package com.clinicapp.controller;

import com.clinicapp.repository.RoleRepository;
import com.clinicapp.security.dto.LoginRequestDTO;
import com.clinicapp.security.dto.LoginResponseDTO;
import com.clinicapp.security.dto.RegisterRequestDTO;
import com.clinicapp.security.jwt.JwtUtil;
import com.clinicapp.security.service.CustomUserDetailsService;
import com.clinicapp.entity.User;
import com.clinicapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.clinicapp.entity.Role;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    //Login
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {

        // Autenticación con username/email y password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        // Si autenticación exitosa, obtenemos el usuario
        User user = userRepository.findByEmail(request.getUsernameOrEmail())
                .orElseGet(() -> userRepository.findByUsername(request.getUsernameOrEmail())
                        .orElseThrow(() -> new RuntimeException("No se encontro")));

        // Generamos el token JWT
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().getName());

        // Devolvemos el token y la info básica
        LoginResponseDTO response = new LoginResponseDTO(
                token,
                user.getUsername(),
                user.getRole().getName()
        );

        return ResponseEntity.ok(response);
    }

    //REGISTRO -> No genera token
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO request) {

        // Verificar si ya existe el usuario o email
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username ya existe");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email ya existe");
        }

        // BUSCAR EL ROLE EXISTENTE
        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Role no encontrado"));

        // Guardar usuario en la base de datos
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(role);
        newUser.setActivate(true);

        userRepository.save(newUser);

        return ResponseEntity.ok("Usuario registrado");
    }
}