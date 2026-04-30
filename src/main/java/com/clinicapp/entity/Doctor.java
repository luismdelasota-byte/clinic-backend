package com.clinicapp.entity;

import jakarta.persistence.*;

@Entity
@Table (name = "doctors")
public class Doctor {

    /*Atributos : columnas de la tabla*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private int cmp;

    @Column(nullable = false, unique = true)
    private String name;

    private String speciality;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    /*Constructor*/
    public Doctor(){}




    /*Metodos o funciones que realizan una accion(void) o devuelven un valor(return)*/

    public Doctor(String name){
        this.name = name;
    }

    //GETTER : obtiene el valor de id, lee
    public Long getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public int getCmp(){
        return cmp;
    }

    public void setCmp(int cmp){
        this.cmp = cmp;
    }

    //SETTER : modifica o asigna valor
    public void setName(String name){
        this.name = name;
    }

    public String getSpeciality(){
        return speciality;
    }

    public void setSpeciality(String speciality){
        this.speciality = speciality;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }
}
