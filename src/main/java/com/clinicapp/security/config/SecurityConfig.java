package com.clinicapp.security.config;

import com.clinicapp.security.jwt.JwtFilter;
import com.clinicapp.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.http.HttpMethod;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity //Activa Spring Security en la aplicacion
@RequiredArgsConstructor //Evita escribir el constructor manualmente
public class SecurityConfig {

    //Inyeccion de CustomUserDetailsService, esto hace que Spring Security use el servicio para buscar
    //usuarios en la base de datos
    private final CustomUserDetailsService userDetailsService;

    //Intercepta las peticiones para vlaidar el token JWT
    private final JwtFilter jwtFilter;

    // Encriptación de contraseñas y comparar en el login
    @Bean
    public PasswordEncoder passwordEncoder() { //Encripta la contrsaseña antes de guardarla
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager usado para login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Configuración principal de seguridad, aqui define como funciona la seguridad del sistema
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //CONFIGURACION

        http
                // desactiva CSRF
                .csrf(csrf -> csrf.disable())

                // JWT no usa sesiones, indica que la aplicaciones es STATELESS es decir sin estados
                //No se guardan sesiones en el servidor, cada request debe trear su JWT en los headers
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Configuración de rutas, reglas de autorizacion
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() //Acceso libre(login, registro)

                        // Pacientes
                        .requestMatchers(HttpMethod.POST, "/api/patients").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/patients/**").hasAnyAuthority("ADMIN", "DOCTOR", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/patients/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/patients/**").hasAuthority("ADMIN")

                        // Doctores
                        .requestMatchers(HttpMethod.POST, "/api/doctors").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/doctors").hasAnyAuthority("ADMIN", "USER", "DOCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/doctors/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/doctors/**").hasAuthority("ADMIN")

                        // Citas
                        .requestMatchers(HttpMethod.POST, "/api/appointments").hasAnyAuthority("ADMIN", "DOCTOR", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/appointments/**").hasAnyAuthority("ADMIN", "DOCTOR", "USER", "PATIENT")
                        .requestMatchers(HttpMethod.PUT, "/api/appointments/**").hasAnyAuthority("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/appointments/**").hasAuthority("ADMIN")

                        // Horarios
                        .requestMatchers(HttpMethod.POST, "/api/schedules").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/schedules/**").hasAnyAuthority("ADMIN", "DOCTOR", "USER", "PATIENT")
                        .requestMatchers(HttpMethod.PUT, "/api/schedules/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/schedules/**").hasAuthority("ADMIN")


                        .anyRequest().authenticated()

                )

                //Nevegador -> si aplica reglas de CORS, por ello usamos .cors()
                //.cors() en el HttpSecurity le dice a Spring Security: "habilita soporte para CORS en la capa de seguridad
                .cors(); //Habilitamos CORS
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean //Importante junto con .cors() define que origenes, metodos y headers estan permitidos
    //Sin esta configuracion, el navegador bloquea la peticion antes de que llegue al backend
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173"); // tu frontend
        configuration.addAllowedMethod("*"); // permite GET, POST, PUT, DELETE
        configuration.addAllowedHeader("*"); // permite todos los headers
        configuration.setAllowCredentials(true);

        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}