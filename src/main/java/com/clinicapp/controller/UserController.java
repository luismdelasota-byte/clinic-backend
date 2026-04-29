package com.clinicapp.controller;

import com.clinicapp.entity.User;
import com.clinicapp.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //Esta clase es un controlador
@RequestMapping("/api/users") //Ruta
public class UserController {

    private UserService userService;

    //Constructor
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public User saverUser(@RequestBody User user){
        return  userService.saveUser(user);
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUser();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }
}
