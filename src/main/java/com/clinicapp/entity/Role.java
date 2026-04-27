package com.clinicapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(nullable = false, unique = true)
    private String name;

    //Constructor
    public Role(){}

    //Constructor con parametro
    public Role(String name){
        this.name = name;
    }

    //Metodos

    public Long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

}
