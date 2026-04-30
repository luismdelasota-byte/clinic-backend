package com.clinicapp.security.service;

import com.clinicapp.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.*;
import com.clinicapp.entity.User;

import java.util.List;

//Interfaz UserDetailsService para buscar usuarios durante el login
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    //Constructor
    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    //Recibimos valor de user o email
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        //Buscamos usuario por email
        User user = userRepository.findByEmail(usernameOrEmail)
                .orElseGet(() ->
                        userRepository.findByUsername(usernameOrEmail)
                                .orElseThrow(()->
                                        new UsernameNotFoundException("usuario no encontrado")));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().getName()))
        );
    }

}
