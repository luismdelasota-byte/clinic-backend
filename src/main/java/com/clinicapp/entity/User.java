/*Clase que representa las tablas de mi base de datos
 * Se anotan con JPA/Hibernate para que Spring Boot pueda mapearlas a la BD
 * JPA/Hibernate -> En esta clase representado con @Entity, @Table, @Id*/
package com.clinicapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "activate")
    private Boolean activate = true;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    //Constructor
    public User(){};

    //Metodos

    //Getter(leer/obtener)

    public Long getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public  String getPassword(){
        return password;
    }

    public Boolean getActivate(){
        return activate;
    }

    public Role getRole(){
        return role;
    }

    //Setter(cambiar/asignar)

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setActivate(Boolean activate){
        this.activate = activate;
    }

    public void setRole(Role role){
        this.role = role;
    }

}
