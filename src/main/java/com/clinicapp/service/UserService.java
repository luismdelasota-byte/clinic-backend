package com.clinicapp.service;

import com.clinicapp.entity.User;
import com.clinicapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    //Constructor
    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Metodos
    public User saveUser(User user){

        //Validacion email
        if(user.getEmail() == null ||  user.getEmail().isBlank()){
            throw new IllegalArgumentException("Emial obligatorio");
        }

        //Validacion email unico
        if(userRepository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("Ya existe usuario con ese email...");
        }

        //Validacion username
        if(user.getUsername() == null || user.getUsername().isBlank()){
            throw new IllegalArgumentException("Usuario obligatorio...");
        }

        //Validacion username unico
        if(userRepository.existsByUsername(user.getUsername())){
            throw new IllegalArgumentException("Usuario ya existe... ");
        }

        //Validacion Password
        if(user.getPassword() == null || user.getPassword().length()<6){
            throw new IllegalArgumentException("Contraseña debe tener al menos s6 caracteres...");
        }

        //Transformacion
        user.setEmail(user.getEmail().toLowerCase());
        user.setUsername(user.getUsername().toLowerCase());

        //Encriptar password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUser(){
        return  userRepository.findAll();
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

}
