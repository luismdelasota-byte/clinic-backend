package com.clinicapp.controller;

import com.clinicapp.entity.Doctor;
import com.clinicapp.service.DoctorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    //Atributo
    private DoctorService doctorService;

    //Constructor
    public DoctorController(DoctorService doctorService){
        this.doctorService = doctorService;
    }

    //Metodos
    /*1.POST(Crear)*/
    @PostMapping
    public Doctor saveDoctor(@RequestBody Doctor doctor){
        return doctorService.saveDoctor(doctor);
    }

    //Obtener todos los datos
    @GetMapping
    public List<Doctor> getAllDoctor(){
        return  doctorService.getAllDoctors();
    }

    //Obtener doctor por id
    @GetMapping("/{id}")
    public Doctor getAllById(@PathVariable Long id){
        return doctorService.getDoctorById(id);
    }

    //Eliminar
    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id){
        doctorService.deleteDoctor(id);
    }

    //Actualizacion, PUT -> Modifica el recurso completo, es decir, en postman tenemos que poner todos los elementos y
    //Si queremos modificar un elemento solo modificamos esa columna pero los demas lo escribimos como estaba
    @PutMapping("/{id}")
    public Doctor updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor){
        return doctorService.updateDoctor(id, doctor);
    }
}
