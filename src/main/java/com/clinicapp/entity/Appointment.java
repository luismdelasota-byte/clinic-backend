// Entity = tabla de la base de datos en forma de clase Java

package com.clinicapp.entity;

import jakarta.persistence.*;
import  java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {
    /*Atributos*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) //Esto hace que el back envia los datos del doctor y no nos muestre NULL
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(name = "appointment_date")
    private LocalDateTime appointmentDate;

    @Column(name = "status")
    private String status = "SCHEDULED";

    /*Constructor*/

    public Appointment(){}


    /*Modulos*/

    public Long getId(){
        return id;
    }

    public Doctor getDoctor(){
        return  doctor;
    }

    public void setDoctor(Doctor doctor){
        this.doctor = doctor;
    }

    public Patient getPatient(){
        return patient;
    }

    public void setPatient(Patient patient){
        this.patient = patient;
    }

    public LocalDateTime getAppointmentDate(){
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate){
        this.appointmentDate = appointmentDate;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }
}
