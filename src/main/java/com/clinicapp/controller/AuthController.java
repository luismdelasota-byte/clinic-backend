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

import com.clinicapp.entity.Doctor;
import com.clinicapp.repository.DoctorRepository;
import com.clinicapp.entity.Patient;
import com.clinicapp.repository.PatientRepository;

import java.time.LocalDate;

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

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

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
                user.getRole().getName(),
                null,
                null
        );

        // Si el usuario es doctor
        if (user.getRole().getName().equals("DOCTOR")) {
            Doctor doctor = doctorRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));
            response.setDoctorId(doctor.getId());
        }

        // Si el usuario es paciente
        if(user.getRole().getName().equals("PATIENT")){
            Patient patient = patientRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
            response.setPatientId(patient.getId());
        }

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

        // BUSCAR EL ROLE EXISTENTE (ignora mayusculas/minusculas y espacios)
        Role role = roleRepository.findByName(request.getRole().trim().toUpperCase())
                .orElseGet(() -> {
                    System.out.println("ERROR: No se encontro el rol: '" + request.getRole() + "'");
                    System.out.println("Roles disponibles en la BD:");
                    roleRepository.findAll().forEach(r -> System.out.println("- '" + r.getName() + "' (largo: " + r.getName().length() + ")"));
                    throw new RuntimeException("Role no encontrado: " + request.getRole());
                });



        // Guardar usuario en la base de datos
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(role);
        newUser.setActivate(true);

        userRepository.save(newUser);

        // Crear entidad asociada según el rol
        if ("PATIENT".equals(role.getName())) {
            Patient patient = new Patient();
            patient.setUser(newUser);
            patient.setName(request.getUsername());
            patient.setEmail(request.getEmail());
            patient.setPhone("000000000");
            patient.setBirthDate(LocalDate.now());
            patientRepository.save(patient);
        } if ("DOCTOR".equals(role.getName())) {
            Doctor doctor = new Doctor();
            doctor.setUser(newUser);
            doctor.setName(request.getUsername());
            doctor.setEmail(request.getEmail());
            doctor.setPhone("000000000"); // valor por defecto si es obligatorio
            doctor.setSpeciality("General"); // si tu entidad lo requiere
            doctor.setCmp((int) (Math.random() * 90000) + 10000); // Asigna un CMP aleatorio para que no choque el Unique Constraint
            doctorRepository.save(doctor);
        } else if ("ADMIN".equals(role.getName())) {
            // Para ADMIN no se crea entidad adicional
            System.out.println("Usuario ADMIN registrado correctamente");
        }

        return ResponseEntity.ok("Usuario registrado");
    }
}