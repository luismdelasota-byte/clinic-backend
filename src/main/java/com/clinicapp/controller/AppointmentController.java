package com.clinicapp.controller;

import com.clinicapp.entity.Appointment;
import com.clinicapp.entity.Patient;
import com.clinicapp.service.AppointmentService;
import org.springframework.web.bind.annotation.*;
import com.clinicapp.entity.Doctor;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    //Atributo
    private final AppointmentService appointmentService;

    //Constructor
    public AppointmentController(AppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }

    //Metodos

    //POST
    @PostMapping
    public Appointment saveAppointment(@RequestBody Appointment appointment ){
        return appointmentService.saveAppointment(appointment);
    }

    //GET
    @GetMapping
    public List<Appointment> setAppointment(){
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public Appointment getAppointmentByID(@PathVariable Long id){
        return appointmentService.getAppointmentById(id);
    }

    //Actualizar(PUT)
    @PutMapping("/{id}")
    public Appointment updateAppointment(@PathVariable Long id, @RequestBody Appointment appointment){
        return appointmentService.updateAppointment(id, appointment);
    }

    //Eliminar(DELETE)
    @DeleteMapping("/{id}")
    public void  deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        return appointmentService.getAppointmentsByDoctor(doctorId);
    }

    @GetMapping("/patient/{patientId}")
    public List<Appointment> getAppointmentsByPatient(@PathVariable Long patientId){
        return appointmentService.getAppointmentsByPatient(patientId);
    }


}

