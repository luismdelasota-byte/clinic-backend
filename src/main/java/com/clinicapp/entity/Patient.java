package com.clinicapp.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient {

    /*Atributos : Columnas de la tabla*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    /*Constructor : crea objetos*/

    public Patient(){}

    //Crea objetos mediante un atributo
    public Patient(String name){
        this.name = name;
    }

    /*Metodos*/

    public Long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPhone(){
        return  phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public LocalDate getBirthDate(){
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate){
        this.birthDate = birthDate;
    }
}
