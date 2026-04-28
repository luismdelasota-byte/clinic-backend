/* Capa para manejar el acceso a la base de datos, conecta la entidades con las operaciones CRUD*/

package com.clinicapp.repository;

import com.clinicapp.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//extends JpaRepository, Role clase importada de Entity, Long tipo de la clave primaria id
public interface UserRepository extends JpaRepository<User, Long> {

    //Buscamos usuario por email y username, devuelve objeto User
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    //Buscamos usuario por email y username, devuelve true o false
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
