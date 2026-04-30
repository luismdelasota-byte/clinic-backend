package com.clinicapp.controller;

import com.clinicapp.entity.Appointment;
import com.clinicapp.service.AppointmentService;
import org.springframework.web.bind.annotation.*;

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
    public List<Appointment> setAppointmenst(){
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
}

